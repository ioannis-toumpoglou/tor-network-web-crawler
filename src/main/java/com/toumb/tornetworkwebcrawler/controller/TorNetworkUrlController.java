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
@RequestMapping("/tor-urls")
public class TorNetworkUrlController {
	
	private TorNetworkUrlService torNetworkUrlService;
	
	public TorNetworkUrlController(TorNetworkUrlService theTorNetworkUrlService) {
		this.torNetworkUrlService = theTorNetworkUrlService;
	}

	@GetMapping("/list")
	public String listTorNetWorkUrls(Model model) {
		// Get the Tor URL list from the Service
		List<TorNetworkUrl> torUrls = torNetworkUrlService.findAll();
		
		// Add the Tor Network URL list to the model
		model.addAttribute("torUrls", torUrls);
		
		return "tor_urls/list-tor-urls";
	}
	
	@GetMapping("/showAddForm")
	public String showAddForm(Model model) {
		// Create model attribute to bind form data
		TorNetworkUrl torUrl = new TorNetworkUrl();
		
		model.addAttribute("torUrl", torUrl);
		model.addAttribute("title", "Add Tor URL");
		
		return "tor_urls/tor-url-form";
	}
	
	@PostMapping("/save")
	public String saveTorUrl(@ModelAttribute("torUrl") TorNetworkUrl torUrl, BindingResult result) throws IOException {
		// Save the Tor URL using the service
		torNetworkUrlService.save(torUrl);
		
		return "redirect:/tor-urls/list";
	}
	
	@GetMapping("/showUpdateForm")
	public String showUpdateForm(@RequestParam("torUrlId") int id, Model model) {
		// Get the Tor URL from the service
		TorNetworkUrl torUrl = torNetworkUrlService.findById(id);
		// Set the Tor URL as a model attribute to pre-populate the form
		model.addAttribute("torUrl", torUrl);
		model.addAttribute("title", "Update Tor URL");
		// Send to the form		
		return "tor_urls/tor-url-form";
	}
	
	@GetMapping("/delete")
	public String deleteTorUrl(@RequestParam("torUrlId") int id) {
		// Delete the Tor URL record
		torNetworkUrlService.deleteById(id);
		
		return "redirect:/tor-urls/list";
	}
	
}
