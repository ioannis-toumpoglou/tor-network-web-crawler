package com.toumb.tornetworkwebcrawler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="web_page_content")
@Getter @Setter
public class WebPageContent {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="url")
	private String url;
	
	@Column(name = "threat_type")
	private String threatType;
	
	@Column(name="html_code")
	private String htmlCode;
	
	@Column(name="text")
	private String text;
	
	public WebPageContent() {}

	public WebPageContent(int id, String url, String threatType, String htmlCode, String text) {
		this.id = id;
		this.url = url;
		this.threatType = threatType;
		this.htmlCode = htmlCode;
		this.text = text;
	}

}
