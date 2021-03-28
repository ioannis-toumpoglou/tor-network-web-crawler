package com.toumb.tornetworkwebcrawler.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toumb.tornetworkwebcrawler.entity.ThreatType;

public interface ThreatTypeRepository extends JpaRepository<ThreatType, Integer> {
	
	public List<ThreatType> findAllByOrderByTypeNameAsc();
	
}
