package com.toumb.tornetworkwebcrawler.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toumb.tornetworkwebcrawler.entity.WebPageContent;

public interface WebPageContentRepository extends JpaRepository<WebPageContent, Integer> {
	
	public List<WebPageContent> findAllByOrderByUrlAsc();
	
	public List<WebPageContent> findAllByOrderByThreatTypeAsc();

}