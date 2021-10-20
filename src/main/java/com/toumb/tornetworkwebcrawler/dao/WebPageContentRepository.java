package com.toumb.tornetworkwebcrawler.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toumb.tornetworkwebcrawler.entity.WebPageContent;

public interface WebPageContentRepository extends JpaRepository<WebPageContent, Integer> {
	
	public List<WebPageContent> findAllByOrderByUrlAsc();
	
	public List<WebPageContent> findAllByOrderByThreatTypeAsc();

	public Optional<WebPageContent> findByUrl(String url);

	public void deleteByUrl(String url);
	
	@Query("FROM WebPageContent WHERE LOWER(url) LIKE %?1% OR LOWER(text) LIKE %?1% OR LOWER(threat_type) LIKE %?1%")
	public List<WebPageContent> findByKeyword(String keyword);

}
