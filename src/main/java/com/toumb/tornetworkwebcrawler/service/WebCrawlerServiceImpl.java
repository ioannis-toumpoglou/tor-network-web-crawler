package com.toumb.tornetworkwebcrawler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toumb.tornetworkwebcrawler.dao.TorNetworkUrlRepository;
import com.toumb.tornetworkwebcrawler.entity.TorNetworkUrl;

@Service
public class WebCrawlerServiceImpl implements WebCrawlerService {

	private TorNetworkUrlRepository torNetworkUrlRepository;
	
	@Autowired
	public WebCrawlerServiceImpl(TorNetworkUrlRepository theTorNetworkUrlRepository) {
		torNetworkUrlRepository = theTorNetworkUrlRepository;
	}

	@Override
	public void crawl(TorNetworkUrl torNetworkUrl) {
		torNetworkUrlRepository.save(torNetworkUrl);
	}

}
