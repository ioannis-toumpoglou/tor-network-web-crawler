package com.toumb.tornetworkwebcrawler.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.script.ScriptException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.toumb.tornetworkwebcrawler.entity.TorNetworkUrl;
import com.toumb.tornetworkwebcrawler.entity.WebPageContent;
import com.toumb.tornetworkwebcrawler.service.TorNetworkUrlService;
import com.toumb.tornetworkwebcrawler.service.WebPageContentService;

@Controller
@RequestMapping("/tor-web-crawler")
public class WebCrawlerController {
	
	private TorNetworkUrlService torNetworkUrlService;
	private WebPageContentService webPageContentService;
	
	private final static Logger LOG = Logger.getLogger(WebCrawlerController.class.getName());
	
	public WebCrawlerController(TorNetworkUrlService theTorNetworkUrlService, WebPageContentService theWebPageContentService) {
		this.torNetworkUrlService = theTorNetworkUrlService;
		this.webPageContentService = theWebPageContentService;
	}
	
	@PostMapping("/crawl-url")
	public String crawlUrl(@ModelAttribute("torUrl") TorNetworkUrl torUrl, Integer urlPageNo, boolean overwritePage, BindingResult result, RedirectAttributes redirectAttributes) throws IOException, ScriptException {
		boolean isDuplicate = false;
		boolean isNotValid = false;
		boolean isOffline = false;

		String urlTarget = getFullUrl(torUrl.getUrl());
		
		try {
			List<TorNetworkUrl> torUrls = torNetworkUrlService.findAll();
			List<String> urlList = new ArrayList<>();
			
			torUrls.forEach(item -> urlList.add(item.getUrl()));
			
			if (urlList.contains(urlTarget)) {
				if (!overwritePage) {
					isDuplicate = true; 
					redirectAttributes.addFlashAttribute("isDuplicate", isDuplicate);
					return "redirect:/tor-urls/list";
				} else {
					TorNetworkUrl tempUrl = torNetworkUrlService.findByUrl(urlTarget);
					int urlId = tempUrl.getId();
					torNetworkUrlService.deleteById(urlId);
				}
			}
			
			HttpURLConnection conn = establishConnectionToWebPage(urlTarget);
			connectToTorNetwork();
			
			String webPageStatus = webPageStatus(conn);
			
			if ("Access Denied".equals(webPageStatus)) {
				isNotValid = true; 
				redirectAttributes.addFlashAttribute("isNotValid", isNotValid);
				torUrl.setStatus(webPageStatus);
				torNetworkUrlService.save(torUrl);
				return "redirect:/tor-urls/list";
			}
	
			getLocalIpAddress();
			getIpAddressOnWeb();
			torUrl.setUrl(urlTarget);
			torUrl.setStatus(webPageStatus(conn));
			torNetworkUrlService.save(torUrl);
			
			List<WebPageContent> webContents = webPageContentService.findAll();
			List<String> webContentList = new ArrayList<>();
			
			webContents.forEach(item -> webContentList.add(item.getUrl()));
			
			if (webContentList.contains(urlTarget)) {
				if (!overwritePage) {
					isDuplicate = true; 
					redirectAttributes.addFlashAttribute("isDuplicate", isDuplicate);
					return "redirect:/tor-urls/list";
				} else {
					WebPageContent tempWebPageContent = webPageContentService.findByUrl(urlTarget);
					int urlId = tempWebPageContent.getId();
					webPageContentService.deleteById(urlId);
				}
			}
			
			WebPageContent webPageContent = new WebPageContent();
			webPageContent.setUrl(urlTarget);
			webPageContent.setHtmlCode(retrieveHtmlSourceCode(conn, urlTarget));
			webPageContent.setText(retrieveWebPageText(urlTarget));
			webPageContentService.save(webPageContent);
			
			List<String> hrefLinks = retrieveHrefLinks(urlTarget);
			LOG.info("There are " + hrefLinks.size() + " links in this website");
			
			if (urlPageNo == null) {
				urlPageNo = 0;
			} else {
				if (hrefLinks.size() > 0) {
					int i = 1;
					for (String hrefLink : hrefLinks) {
						if (i == urlPageNo) {
							break;
						}
						if (urlList.contains(hrefLink)) {
							continue;
						}
						try {
							conn = establishConnectionToWebPage(hrefLink);
							TorNetworkUrl tempUrl = new TorNetworkUrl();
							tempUrl.setUrl(hrefLink);
							tempUrl.setStatus(webPageStatus(conn));
							torNetworkUrlService.save(tempUrl);
							WebPageContent tempWebPageContent = new WebPageContent();
							tempWebPageContent.setUrl(hrefLink);
							tempWebPageContent.setHtmlCode(retrieveHtmlSourceCode(conn, hrefLink));
							tempWebPageContent.setText(retrieveWebPageText(hrefLink));
							webPageContentService.save(tempWebPageContent);
							i++;
						} catch (Exception e) {
							continue;
						}
					}
				}
			}
			LOG.info("\nCrawl completed successfully\n");
			return "redirect:/tor-urls/list";
		} catch (Exception e) {
			if (!torUrl.getUrl().contains(".onion")) {
				isNotValid = true;
				redirectAttributes.addFlashAttribute("isNotValid", isNotValid);
				LOG.info("The URL format is not correct");
				LOG.info("\nError retrieving the web page..");
				LOG.info("Please try again");
				return "redirect:/tor-urls/list";				
			} else {
				isOffline = true;
				redirectAttributes.addFlashAttribute("isOffline", isOffline);
				LOG.info("Web Page offline");
				torUrl.setStatus("Offline");
				torNetworkUrlService.save(torUrl);
				LOG.info("Error retrieving the web page..");
				LOG.info("Please try again");
				return "redirect:/tor-urls/list";
			}
		} finally {
			Process process = Runtime.getRuntime().exec(new String[]{"python", "src/main/resources/static/python_scripts/KMeans_classifier.py", urlTarget});
			LOG.info(process.toString());
			ProcessBuilder processBuilder = new ProcessBuilder("src/main/resources/static/backup_db.bat");
			processBuilder.start();
			LOG.info(processBuilder.toString());
			LOG.info("Database backup completed successfully");

		}
	}
	
	public String getFullUrl(String urlTarget) throws IOException {
		String fullUrl = "http://" + urlTarget;
        return urlTarget.contains("http") ? urlTarget : fullUrl;
	}
	
	public HttpURLConnection establishConnectionToWebPage(String urlTarget) throws IOException {
		URL url;
		HttpURLConnection conn;
		
        url = new URL(getFullUrl(urlTarget));
	    conn = (HttpURLConnection) url.openConnection();
        url.getProtocol();
        	
        // Print URL information while retrieving the data
        LOG.info("protocol = " + url.getProtocol());
        LOG.info("authority = " + url.getAuthority());
        LOG.info("host = " + url.getHost());
        LOG.info("port = " + url.getPort());
        LOG.info("path = " + url.getPath());
        LOG.info("query = " + url.getQuery());
        LOG.info("filename = " + url.getFile());
        LOG.info("ref = " + url.getRef());
        LOG.info("status code = " + conn.getResponseCode());
        
        return conn;
	}
	
	public void connectToTorNetwork() {
		System.getProperties().put("proxySet", "true");
    	System.getProperties().put("socksProxyHost", "127.0.0.1");
    	System.getProperties().put("socksProxyPort", "9150");
	}
	
	public void getLocalIpAddress() throws IOException {
		System.out.println(InetAddress.getLocalHost());
	}
	
	public void getIpAddressOnWeb() throws IOException {
		URL whatismyip = new URL("http://checkip.amazonaws.com");
    	BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

    	String ipOnWeb = in.readLine();
    	LOG.info("IP on web: " + ipOnWeb);
	}
	
	public String retrieveHtmlSourceCode(HttpURLConnection conn, String urlTarget) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		String htmlSourceCode = "";
		
		while ((inputLine = br.readLine()) != null) {
			htmlSourceCode += inputLine;
		}
		
		LOG.info("\nWebpage HTML code successfully retrieved");
		
		return htmlSourceCode;
	}
	
	public String retrieveWebPageText(String urlTarget) throws IOException {
		Document doc = Jsoup.connect(urlTarget).get();	// Get the webpage
		Elements paragraphs = doc.select("p");  // Get the HTML paragraph elements
		if (paragraphs.size() == 0) {
			paragraphs = doc.select("div");  // Get the HTML div elements if paragraph elements empty
		}
		
		String webPageText = "";
		
		for (Element paragraph : paragraphs) {
			String text = paragraph.text() + "\n\n";

			webPageText += text;
		}
		
		LOG.info("\nWebpage text extraction successfully completed");
	    
	    return webPageText;
	}
	
	public List<String> retrieveHrefLinks(String urlTarget) throws IOException {
		Document doc = Jsoup.connect(urlTarget).get();	// Get the webpage
		Elements links = doc.select("a[href]");	// Get the HTML href elements
		List<String> hrefLinks = new ArrayList<>();
		
		for (Element link : links) {
			String formattedLink = link.attr("href");
			if (formattedLink.contains("http")) {
				hrefLinks.add(formattedLink);
			} else if (!formattedLink.contains("www")) {
				String tempLink = urlTarget + formattedLink;
				hrefLinks.add(tempLink);
			}
		}
		
		LOG.info("\nWebpage links extraction successfully completed");
		
		return hrefLinks;
	}
	
	public String webPageStatus(HttpURLConnection conn) throws IOException {
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 236) {
			return "Alive";
		} else if (conn.getResponseCode() >= 300 && conn.getResponseCode() <= 308) {
			return "Alive";
		} else if (conn.getResponseCode() >= 500 && conn.getResponseCode() <= 599) {
			return "Offline";
		}
		return "Access Denied";
	}
	
}
