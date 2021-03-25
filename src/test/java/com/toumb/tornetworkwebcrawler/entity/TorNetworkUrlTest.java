package com.toumb.tornetworkwebcrawler.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class TorNetworkUrlTest {
	
	@Test
	void getIdTest() {
		TorNetworkUrl url = new TorNetworkUrl();
		url.setId(1);
		int id = url.getId();
		assertEquals(1, id);
		
		TorNetworkUrl secondUrl = new TorNetworkUrl(2, "www.in.gr", "Alive");
		int secondId = secondUrl.getId();
		assertEquals(2, secondId);
		assertNotEquals(3, secondId);
	}
	
	@Test
	void getUrlStatusTest() {
		TorNetworkUrl url = new TorNetworkUrl();
		url.setStatus("Active");
		String status = url.getStatus();
		assertEquals("Active", status);
		assertNotEquals("Offline", status);
		
		TorNetworkUrl secondUrl = new TorNetworkUrl(2, "www.in.gr", "Alive");
		String secondStatus = secondUrl.getStatus();
		assertEquals("Alive", secondStatus);
		assertNotEquals("Offline", secondStatus);
	}
	
}
