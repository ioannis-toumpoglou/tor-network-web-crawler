package com.toumb.tornetworkwebcrawler.service;

import java.util.List;

import com.toumb.tornetworkwebcrawler.entity.ThreatType;

public interface ThreatTypeService {
	
	public List<ThreatType> findAll();
	
	public ThreatType findById(int id);
	
	public void save(ThreatType threatType);
	
	public void deleteById(int id);

}
