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

import com.toumb.tornetworkwebcrawler.entity.WebPageContent;
import com.toumb.tornetworkwebcrawler.service.WebPageContentService;

@Controller
@RequestMapping("/web-page-content")
public class WebPageContentController {
	
	private WebPageContentService webPageContentService;
	
	public WebPageContentController(WebPageContentService theWebPageContentService) {
		this.webPageContentService = theWebPageContentService;
	}

	@GetMapping("/list")
	public String listWebPageContent(Model model) {
		// Get the Web Page Content list from the Service
		List<WebPageContent> webPageContent = webPageContentService.findAll();
		
		// Add the Web Page Content list to the model
		model.addAttribute("webPageContent", webPageContent);
		
		return "web_page_content/list-web-page-content";
	}
	
	@GetMapping("/showAddForm")
	public String showAddForm(Model model) {
		// Create model attribute to bind form data
		WebPageContent webPageContent = new WebPageContent();
		
		model.addAttribute("webPageContent", webPageContent);
		model.addAttribute("title", "Add Web Page Content");
		
		return "web_page_content/web-page-content-form";
	}
	
	@PostMapping("/save")
	public String saveWebPageContent(@ModelAttribute("webPageContent") WebPageContent webPageContent, BindingResult result) throws IOException {
		// Save the Web Page Content using the service
		webPageContentService.save(webPageContent);
		
		return "redirect:/web-page-content/list";
	}
	
	@GetMapping("/showUpdateForm")
	public String showUpdateForm(@RequestParam("webPageContentId") int id, Model model) {
		// Get Web Page Content from the service
		WebPageContent webPageContent = webPageContentService.findById(id);
		// Set Web Page Content as a model attribute to pre-populate the form
		model.addAttribute("webPageContent", webPageContent);
		model.addAttribute("title", "Update Web Page Content");
		// Send to the form		
		return "web_page_content/web-page-content-form";
	}
	
	@GetMapping("/delete")
	public String deleteWebPageContent(@RequestParam("webPageContentId") int id) {
		// Delete the Web Page Content record
		webPageContentService.deleteById(id);
		
		return "redirect:/web-page-content/list";
	}
	
}
