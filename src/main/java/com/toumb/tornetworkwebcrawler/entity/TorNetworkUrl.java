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
@Table(name="tor_network_url")
@Getter @Setter
public class TorNetworkUrl {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="url")
	private String url;

	@Column(name="status")
	private String status;
	
	public TorNetworkUrl() {}

	public TorNetworkUrl(int id, String url, String status) {
		this.id = id;
		this.url = url;
		this.status = status;
	}

}
