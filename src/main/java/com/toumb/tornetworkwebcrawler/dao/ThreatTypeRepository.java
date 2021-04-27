package com.toumb.tornetworkwebcrawler.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toumb.tornetworkwebcrawler.entity.ThreatType;

public interface ThreatTypeRepository extends JpaRepository<ThreatType, Integer> {
	
	public List<ThreatType> findAllByOrderByTypeNameAsc();
	
	@Query("FROM ThreatType WHERE LOWER(type_name) LIKE %?1% OR LOWER(description) LIKE %?1%")
	public List<ThreatType> findByKeyword(String keyword);
	
}
