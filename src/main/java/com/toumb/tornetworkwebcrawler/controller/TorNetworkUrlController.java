package com.toumb.tornetworkwebcrawler.controller;

import java.io.IOException;
import java.util.List;

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
@RequestMapping("/tor-network-urls")
public class TorNetworkUrlController {
	
	private TorNetworkUrlService torNetworkUrlService;
	
	public TorNetworkUrlController(TorNetworkUrlService theTorNetworkUrlService) {
		this.torNetworkUrlService = theTorNetworkUrlService;
	}

	@GetMapping("/list")
	public String listTorNetWorkUrls(Model model) {
		// Get the Tor Network URL list from the Service
		List<TorNetworkUrl> torNetworkUrls = torNetworkUrlService.findAll();
		
		// Add the Tor Network URL list to the model
		model.addAttribute("torNetworkUrls", torNetworkUrls);
		
		return "tor_network_urls/list-tor-network-urls";
	}
	
	@GetMapping("/showAddForm")
	public String showAddForm(Model model) {
		// Create model attribute to bind form data
		TorNetworkUrl torNetworkUrl = new TorNetworkUrl();
		
		model.addAttribute("torNetworkUrl", torNetworkUrl);
		
		return "tor_network_urls/tor-network-url-form";
	}
	
	@PostMapping("/save")
	public String saveThreatType(@ModelAttribute("torNetworkUrl") TorNetworkUrl torNetworkUrl, BindingResult result) throws IOException {
		// Save the Tor Network URL using the service
		torNetworkUrlService.save(torNetworkUrl);
		
		return "redirect:/tor-network-urls/list";
	}
	
	@GetMapping("/showUpdateForm")
	public String showUpdateForm(@RequestParam("torNetworkUrlId") int id, Model model) {
		// Get the Tor Network URL from the service
		TorNetworkUrl torNetworkUrl = torNetworkUrlService.findById(id);
		// Set the Tor Network URL as a model attribute to pre-populate the form
		model.addAttribute("torNetworkUrl", torNetworkUrl);
		// Send to the form		
		return "tor_network_urls/tor-network-url-form";
	}
	
	@GetMapping("/delete")
	public String deleteTorUrl(@RequestParam("torNetworkUrlId") int id) {
		// Delete the Tor Network URL record
		torNetworkUrlService.deleteById(id);
		
		return "redirect:/tor-network-urls/list";
	}
	
}
