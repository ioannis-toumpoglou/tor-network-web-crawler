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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.toumb.tornetworkwebcrawler.entity.TorNetworkUrl;
import com.toumb.tornetworkwebcrawler.service.TorNetworkUrlService;

@Controller
@RequestMapping("/tor-urls")
public class WebCrawlerController {
	
	private TorNetworkUrlService torNetworkUrlService;
	
	public WebCrawlerController(TorNetworkUrlService theTorNetworkUrlService) {
		this.torNetworkUrlService = theTorNetworkUrlService;
	}

	@GetMapping("/list")
	public String listTorUrls(Model model) {
		// Get the Tor URL list from the Service
		List<TorNetworkUrl> torUrls = torNetworkUrlService.findAll();
		
		// Add the URL list to the model
		model.addAttribute("torUrls", torUrls);
		
		return "tor_urls/list-tor-urls";
	}
	
	@PostMapping("/save")
	public String saveTorUrl(@ModelAttribute("torUrl") TorNetworkUrl torNetworkUrl, BindingResult result) throws IOException {
		String urlTarget = torNetworkUrl.getUrl();
		HttpURLConnection conn = establishConnectionToWebPage(urlTarget);
		getLocalIpAddress();
		getIpAddressOnWeb();
		torNetworkUrl.setUrl(urlTarget);
		torNetworkUrl.setStatus(webPageStatus(conn));
		torNetworkUrlService.save(torNetworkUrl);
		
		List<String> hrefLinks = retrieveHrefLinks(urlTarget);
		
		for (String hrefLink : hrefLinks) {
			conn = establishConnectionToWebPage(hrefLink);
			torNetworkUrl.setUrl(hrefLink);
			torNetworkUrl.setStatus(webPageStatus(conn));
			torNetworkUrlService.save(torNetworkUrl);
		}
		
		return "redirect:/tor-urls/list";
	}
	
	@GetMapping("/delete")
	public String deleteTorUrl(@RequestParam("torUrlId") int id) {
		// Delete the Tor URL record
		torNetworkUrlService.deleteById(id);
		
		return "redirect:/tor-urls/list";
	}
	
	public HttpURLConnection establishConnectionToWebPage(String urlTarget) throws IOException {
		URL url;
		HttpURLConnection conn;
		
        try {
        	url = new URL(urlTarget);
	        conn = (HttpURLConnection) url.openConnection();
        	url.getProtocol();
        } catch (Exception e) {
        	urlTarget = "https://" + urlTarget;
        	url = new URL(urlTarget);
	        conn = (HttpURLConnection) url.openConnection();
        }
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
		Elements paragraphs = doc.select("p");	// Get the HTML paragraph elements
		String webPageText = "";
		
		for (Element paragraph : paragraphs) {
			String text = paragraph.text() + "\n\n";
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
		if (conn.getResponseCode() == 200) {
			return "Alive";
		}
		return "Offline";
	}
	
}
