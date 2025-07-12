package com.urlshortener.controller;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.urlshortener.dto.UrlRequest;
import com.urlshortener.service.UrlShortenerService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class UrlShortenerController {

	@Autowired
	UrlShortenerService urlShortenerService;
	
	
	@PostMapping("/shorten")
	public ResponseEntity<String> createShortUrl(@Valid @RequestBody UrlRequest urlRequest) {
		
	String shortCode = urlShortenerService.shortenUrl(urlRequest.getLongUrl());
		return ResponseEntity.ok(shortCode);
	}
	
	
	@GetMapping("/r/{shortCode}")
	public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
		
	String longUrl = urlShortenerService.getOriginalUrl(shortCode);
		return ResponseEntity.status(HttpStatus.FOUND)
				             .location(URI.create(longUrl))
				             .build();
	}
}
