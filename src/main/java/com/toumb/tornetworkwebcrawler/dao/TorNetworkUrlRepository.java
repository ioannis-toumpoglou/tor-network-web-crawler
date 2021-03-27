package com.toumb.tornetworkwebcrawler.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toumb.tornetworkwebcrawler.entity.TorNetworkUrl;

public interface TorNetworkUrlRepository extends JpaRepository<TorNetworkUrl, Integer> {
	
	public List<TorNetworkUrl> findAllByOrderByUrlAsc();
	
	public List<TorNetworkUrl> findAllByOrderByStatusAsc();

}
