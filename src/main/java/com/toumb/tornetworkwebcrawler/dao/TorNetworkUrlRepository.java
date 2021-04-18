package com.toumb.tornetworkwebcrawler.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toumb.tornetworkwebcrawler.entity.TorNetworkUrl;

public interface TorNetworkUrlRepository extends JpaRepository<TorNetworkUrl, Integer> {
	
	public List<TorNetworkUrl> findAllByOrderByUrlAsc();
	
	public List<TorNetworkUrl> findAllByOrderByStatusAsc();

	@Query("FROM TorNetworkUrl WHERE LOWER(url) LIKE %?1% OR LOWER(status) LIKE %?1%")
	public List<TorNetworkUrl> findByKeyword(String keyword);

	public Optional<TorNetworkUrl> findByUrl(String url);
	
}
