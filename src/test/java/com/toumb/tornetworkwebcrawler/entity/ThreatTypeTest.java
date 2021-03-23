package com.toumb.tornetworkwebcrawler.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class ThreatTypeTest {
	
	@Test
	void getIdTest() {
		ThreatType threat = new ThreatType();
		threat.setId(1);
		int id = threat.getId();
		assertEquals(1, id);
		
		ThreatType secondThreat = new ThreatType(2, "Spam");
		int secondId = secondThreat.getId();
		assertEquals(2, secondId);
		assertNotEquals(3, secondId);
	}
	
	@Test
	void getThreatTypeTest() {
		ThreatType threat = new ThreatType();
		threat.setThreatΤype("Malware");
		String type = threat.getThreatΤype();
		assertEquals("Malware", type);
		assertNotEquals("Spam", type);
		
		ThreatType secondThreat = new ThreatType(2, "Spam");
		String secondType = secondThreat.getThreatΤype();
		assertEquals("Spam", secondType);
		assertNotEquals("Malware", secondType);
	}
	
}
