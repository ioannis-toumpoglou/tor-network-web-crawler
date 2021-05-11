package com.toumb.tornetworkwebcrawler.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebCrawlerControllerTest {

	@Test
	public void webPageStatusTest() throws IOException {
		URL url = new URL("https://www.google.com");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		assertEquals(conn.getResponseCode(), 200);
		assertNotEquals(conn.getResponseCode(), 500);
	}
	
	@Test
	public void webPageStatusAliveTest() throws IOException {
		URL url = new URL("https://www.google.com");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		String status = null;
		
		if (conn.getResponseCode() == 200) {
			status = "Alive";
		}
		
		assertEquals(status, "Alive");	
	}
	
	@Test
	public void webPageStatusOfflineTest() throws IOException {
		URL url = new URL("https://www.qwertal.gr");
		String status = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.getResponseCode();
		} catch (Exception e) {
			status = "Offline";
		}
		
		assertEquals(status, "Offline");
	}

}
