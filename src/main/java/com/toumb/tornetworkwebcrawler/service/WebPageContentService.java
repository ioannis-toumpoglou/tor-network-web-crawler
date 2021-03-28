package com.toumb.tornetworkwebcrawler.service;

import java.util.List;

import com.toumb.tornetworkwebcrawler.entity.WebPageContent;

public interface WebPageContentService {
	
	public List<WebPageContent> findAll();
	
	public WebPageContent findById(int id);
	
	public void save(WebPageContent webPageContent);
	
	public void deleteById(int id);

}
