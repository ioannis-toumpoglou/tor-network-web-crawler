package com.toumb.tornetworkwebcrawler.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class ThreatTypeTest {
	
	@Test
	void getIdTest() {
		// Manually set ID
		ThreatType threat = new ThreatType();
		threat.setId(1);
		int id = threat.getId();
		assertEquals(1, id);
		
		// ID set in constructor
		ThreatType secondThreat = new ThreatType(2, "Spam", "Irrelevant or unsolicited messages sent over the internet, "
				+ "typically to a large number of users, for the purposes of advertising, phishing, spreading malware, etc.");
		int secondId = secondThreat.getId();
		assertEquals(2, secondId);
		assertNotEquals(3, secondId);
	}
	
	@Test
	void getThreatTypeTest() {
		// Manually set type
		ThreatType threat = new ThreatType();
		threat.setTypeName("Malware");
		String type = threat.getTypeName();
		assertEquals("Malware", type);
		assertNotEquals("Spam", type);
		
		// Type set in constructor
		ThreatType secondThreat = new ThreatType(2, "Spam", "Irrelevant or unsolicited messages sent over the internet, "
				+ "typically to a large number of users, for the purposes of advertising, phishing, spreading malware, etc.");
		String secondType = secondThreat.getTypeName();
		assertEquals("Spam", secondType);
		assertNotEquals("Malware", secondType);
	}
	
	@Test
	void getThreatDescriptionTest() {
		// Manually set description
		ThreatType threat = new ThreatType();
		threat.setTypeName("Malware");
		threat.setDescription("Irrelevant or unsolicited messages sent over the internet, typically to a large number of users, "
				+ "for the purposes of advertising, phishing, spreading malware, etc.");
		String threatDescription = threat.getDescription();
		assertEquals("Irrelevant or unsolicited messages sent over the internet, typically to a large number of users, "
				+ "for the purposes of advertising, phishing, spreading malware, etc.", threatDescription);
		
		// Description set in constructor
		ThreatType secondThreat = new ThreatType(2, "Spam", "Irrelevant or unsolicited messages sent over the internet, "
				+ "typically to a large number of users, for the purposes of advertising, phishing, spreading malware, etc.");
		String secondThreatDescription = secondThreat.getDescription();
		assertEquals("Irrelevant or unsolicited messages sent over the internet, typically to a large number of users, "
				+ "for the purposes of advertising, phishing, spreading malware, etc.", secondThreatDescription);
		assertNotEquals("Malware", secondThreatDescription);
	}
	
}
