package com.toumb.tornetworkwebcrawler.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
	
	public WebCrawlerController(TorNetworkUrlService theTorNetworkUrlService, WebPageContentService theWebPageContentService) {
		this.torNetworkUrlService = theTorNetworkUrlService;
		this.webPageContentService = theWebPageContentService;
	}
	
	@PostMapping("/crawl-url")
	public String crawlUrl(@ModelAttribute("torUrl") TorNetworkUrl torUrl, Integer urlPageNo, BindingResult result, RedirectAttributes redirectAttributes) throws IOException {
		boolean isDuplicate = false;
		boolean isNotValid = false;
		
		String urlTarget = getFullUrl(torUrl.getUrl());
		List<TorNetworkUrl> torUrls = torNetworkUrlService.findAll();
		List<String> urlList = new ArrayList<>();
		
		torUrls.forEach(item -> urlList.add(item.getUrl()));
		
		if (urlList.contains(urlTarget)) {
			isDuplicate = true; 
			redirectAttributes.addFlashAttribute("isDuplicate", isDuplicate);
			return "redirect:/tor-urls/list";
		}
		
		HttpURLConnection conn = establishConnectionToWebPage(urlTarget);
		
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
		
		WebPageContent webPageContent = new WebPageContent();
		webPageContent.setUrl(urlTarget);
		webPageContent.setHtmlCode(retrieveHtmlSourceCode(conn, urlTarget));
		webPageContent.setText(retrieveWebPageText(urlTarget));
		webPageContentService.save(webPageContent);
		
		List<String> hrefLinks = retrieveHrefLinks(urlTarget);
		
		if (urlPageNo == null) {
			urlPageNo = 0;
		} else {
			if (hrefLinks.size() > 0) {
				int i = 1;
				for (String hrefLink : hrefLinks) {
					if (urlList.contains(hrefLink)) {
						continue;
					}
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
					
					if (i == urlPageNo) {
						break;
					} else {
						i++;
					}
				}
			}
		}
		System.out.println("\nCrawl completed successfully.\n");
		return "redirect:/tor-urls/list";
	}
	
	public String getFullUrl(String urlTarget) throws IOException {
		
		if (!urlTarget.contains("http")) {
			urlTarget = "https://" + urlTarget;
		}
        return urlTarget;
	}
	
	public HttpURLConnection establishConnectionToWebPage(String urlTarget) throws IOException {
		URL url;
		HttpURLConnection conn;
		
        url = new URL(getFullUrl(urlTarget));
	    conn = (HttpURLConnection) url.openConnection();
        url.getProtocol();
        	
        // Print URL information while retrieving the data
        System.out.println();
        System.out.println("protocol = " + url.getProtocol());
        System.out.println("authority = " + url.getAuthority());
        System.out.println("host = " + url.getHost());
        System.out.println("port = " + url.getPort());
        System.out.println("path = " + url.getPath());
        System.out.println("query = " + url.getQuery());
        System.out.println("filename = " + url.getFile());
        System.out.println("ref = " + url.getRef());
        System.out.println("status code = " + conn.getResponseCode());
        
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
    	System.out.println("IP on web: " + ipOnWeb);
	}
	
	public String retrieveHtmlSourceCode(HttpURLConnection conn, String urlTarget) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		String htmlSourceCode = "";
		
		while ((inputLine = br.readLine()) != null) {
			htmlSourceCode += inputLine;
		}
		
		System.out.println("\nWebpage HTML code successfully retrieved.");
		
		return htmlSourceCode;
	}
	
	public String retrieveWebPageText(String urlTarget) throws IOException {
		Document doc = Jsoup.connect(urlTarget).get();	// Get the webpage
		Elements paragraphs = doc.select("div");	// Get the HTML div elements
		String webPageText = "";
		
		for (Element paragraph : paragraphs) {
			String text = paragraph.text().trim() + "\n\n";
			webPageText += text;
		}
		
	    System.out.println("\nWebpage text extraction successfully completed.");
	    
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
			}
		}
		
		System.out.println("\nWebpage links extraction successfully completed.");
		
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
