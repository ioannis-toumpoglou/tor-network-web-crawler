package com.toumb.tornetworkwebcrawler.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toumb.tornetworkwebcrawler.dao.TorNetworkUrlRepository;
import com.toumb.tornetworkwebcrawler.entity.TorNetworkUrl;

@Service
public class TorNetworkUrlServiceImpl implements TorNetworkUrlService {

	private TorNetworkUrlRepository torNetworkUrlRepository;
	
	@Autowired
	public TorNetworkUrlServiceImpl(TorNetworkUrlRepository theTorNetworkUrlRepository) {
		torNetworkUrlRepository = theTorNetworkUrlRepository;
	}
	
	@Override
	public List<TorNetworkUrl> findAll() {
		return torNetworkUrlRepository.findAllByOrderByUrlAsc();
	}

	@Override
	public TorNetworkUrl findById(int id) {
		Optional<TorNetworkUrl> result = torNetworkUrlRepository.findById(id);
		
		TorNetworkUrl torNetworkUrl = null;
		// Check if the URL exists
		if (result.isPresent()) {
			torNetworkUrl = result.get();
		} else {
			throw new RuntimeException("Unable to find the URL id - " + id);
		}
		
		return torNetworkUrl;
	}

	@Override
	public void save(TorNetworkUrl torNetworkUrl) {
		torNetworkUrlRepository.save(torNetworkUrl);
	}

	@Override
	public void deleteById(int id) {		
		torNetworkUrlRepository.deleteById(id);
	}
	
	@Override
	public List<TorNetworkUrl> findByKeyword(String keyword) {
		return torNetworkUrlRepository.findByKeyword(keyword);
	}

}
