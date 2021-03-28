package com.toumb.tornetworkwebcrawler.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toumb.tornetworkwebcrawler.dao.WebPageContentRepository;
import com.toumb.tornetworkwebcrawler.entity.WebPageContent;

@Service
public class WebPageContentServiceImpl implements WebPageContentService {

	private WebPageContentRepository webPageContentRepository;
	
	@Autowired
	public WebPageContentServiceImpl(WebPageContentRepository theWebPageContentRepository) {
		webPageContentRepository = theWebPageContentRepository;
	}
	
	@Override
	public List<WebPageContent> findAll() {
		return webPageContentRepository.findAllByOrderByUrlAsc();
	}

	@Override
	public WebPageContent findById(int id) {
		Optional<WebPageContent> result = webPageContentRepository.findById(id);
		
		WebPageContent webPageContent = null;
		// Check if the web page content exists
		if (result.isPresent()) {
			webPageContent = result.get();
		} else {
			throw new RuntimeException("Unable to find the web page content id - " + id);
		}
		
		return webPageContent;
	}

	@Override
	public void save(WebPageContent webPageContent) {
		webPageContentRepository.save(webPageContent);
	}

	@Override
	public void deleteById(int id) {		
		webPageContentRepository.deleteById(id);
	}

}
