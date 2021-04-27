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

import com.toumb.tornetworkwebcrawler.entity.ThreatType;
import com.toumb.tornetworkwebcrawler.service.ThreatTypeService;

@Controller
@RequestMapping("/threat-types")
public class ThreatTypeController {
	
	private ThreatTypeService threatTypeService;
	
	public ThreatTypeController(ThreatTypeService theThreatTypeService) {
		this.threatTypeService = theThreatTypeService;
	}

	@GetMapping("/list")
	public String listThreatTypes(Model model) {
		// Get the Threat Type list from the Service
		List<ThreatType> threatTypes = threatTypeService.findAll();
		
		// Add the Threat Type list to the model
		model.addAttribute("threatTypes", threatTypes);
		
		return "threat_types/list-threat-types";
	}
	
	@GetMapping("/showAddForm")
	public String showAddForm(Model model) {
		// Create model attribute to bind form data
		ThreatType threatType = new ThreatType();
		
		model.addAttribute("threatType", threatType);
		model.addAttribute("title", "Add Threat Type");
		
		return "threat_types/threat-type-form";
	}
	
	@PostMapping("/save")
	public String saveThreatType(@ModelAttribute("threatType") ThreatType threatType, BindingResult result) throws IOException {
		// Save the Threat Type using the service
		threatTypeService.save(threatType);
		
		return "redirect:/threat-types/list";
	}
	
	@GetMapping("/showUpdateForm")
	public String showUpdateForm(@RequestParam("threatTypeId") int id, Model model) {
		// Get Threat Type from the service
		ThreatType threatType = threatTypeService.findById(id);
		// Set Threat Type as a model attribute to pre-populate the form
		model.addAttribute("threatType", threatType);
		model.addAttribute("title", "Update Threat Type");
		// Send to the form		
		return "threat_types/threat-type-form";
	}
	
	@GetMapping("/delete")
	public String deleteTorUrl(@RequestParam("threatTypeId") int id) {
		// Delete the Threat Type record
		threatTypeService.deleteById(id);
		
		return "redirect:/threat-types/list";
	}
	
	@GetMapping("/search")
	public String searchThreatTypes(Model model, @RequestParam("keyword") String keyword) {
		if (keyword != null && keyword.trim().length() > 0) {
			// Get the URL list from the Service
			List<ThreatType> threatTypes = threatTypeService.findByKeyword(keyword);
			// Add the URL list to the model
			model.addAttribute("threatTypes", threatTypes);
		} else {
			listThreatTypes(model);
		}
		
		return "threat_types/list-threat-types";
	}
	
}
