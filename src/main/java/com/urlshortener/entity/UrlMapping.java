package com.urlshortener.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "url_mapping")
public class UrlMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(unique = true)
	private String shortCode;
	
	@Column(nullable=false, columnDefinition = "TEXT")
	private String longUrl;
	
	private LocalDateTime createdAt;
	
	@Column(nullable = false)
	private int clickCount;
	
}
