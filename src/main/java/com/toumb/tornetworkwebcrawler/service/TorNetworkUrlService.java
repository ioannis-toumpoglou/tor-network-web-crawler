package com.toumb.tornetworkwebcrawler.service;

import java.util.List;

import com.toumb.tornetworkwebcrawler.entity.TorNetworkUrl;

public interface TorNetworkUrlService {
	
	public List<TorNetworkUrl> findAll();
	
	public TorNetworkUrl findById(int id);
	
	public void save(TorNetworkUrl torNetorkUrl);
	
	public void deleteById(int id);
	
	public List<TorNetworkUrl> findByKeyword(String keyword);

}
