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
@Table(name="threat_type")
@Getter @Setter
public class ThreatType {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="type_name")
	private String typeName;
	
	@Column(name="description")
	private String description;
	
	public ThreatType() {}

	public ThreatType(int id, String typeName, String description) {
		this.id = id;
		this.typeName = typeName;
		this.description = description;
	}

}
