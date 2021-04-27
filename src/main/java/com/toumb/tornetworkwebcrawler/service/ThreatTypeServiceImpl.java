package com.toumb.tornetworkwebcrawler.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toumb.tornetworkwebcrawler.dao.ThreatTypeRepository;
import com.toumb.tornetworkwebcrawler.entity.ThreatType;

@Service
public class ThreatTypeServiceImpl implements ThreatTypeService {

	private ThreatTypeRepository threatTypeRepository;
	
	@Autowired
	public ThreatTypeServiceImpl(ThreatTypeRepository theThreatTypeRepository) {
		threatTypeRepository = theThreatTypeRepository;
	}
	
	@Override
	public List<ThreatType> findAll() {
		return threatTypeRepository.findAllByOrderByTypeNameAsc();
	}

	@Override
	public ThreatType findById(int id) {
		Optional<ThreatType> result = threatTypeRepository.findById(id);
		
		ThreatType threatType = null;
		// Check if the threat type exists
		if (result.isPresent()) {
			threatType = result.get();
		} else {
			throw new RuntimeException("Unable to find the threat type id - " + id);
		}
		
		return threatType;
	}

	@Override
	public void save(ThreatType threatType) {
		threatTypeRepository.save(threatType);
	}

	@Override
	public void deleteById(int id) {		
		threatTypeRepository.deleteById(id);
	}
	
	@Override
	public List<ThreatType> findByKeyword(String keyword) {
		return threatTypeRepository.findByKeyword(keyword);
	}

}
