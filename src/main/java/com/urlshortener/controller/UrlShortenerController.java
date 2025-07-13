package com.urlshortener.controller;

import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urlshortener.dto.ShortenUrlResponse;
import com.urlshortener.dto.UrlRequest;
import com.urlshortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class UrlShortenerController {

	private static final Logger logger = LoggerFactory.getLogger(UrlShortenerController.class);
	
	@Autowired
	UrlShortenerService urlShortenerService;
	
	
	@PostMapping("/shorten")
	public ResponseEntity<ShortenUrlResponse> createShortUrl(@Valid @RequestBody UrlRequest urlRequest) {
	
		String inputUrl=urlRequest.getLongUrl();
		logger.info("Received Request to shorten URL: {}", inputUrl);
	    String shortCode = urlShortenerService.shortenUrl(inputUrl);
	    logger.debug("Generated short code: {}", shortCode);
		return new ResponseEntity<>(ShortenUrlResponse.builder()
                .longUrl(inputUrl)
                .shortcode(shortCode)
                .build(), HttpStatus.CREATED);
	}
	
	
	@GetMapping("/r/{shortCode}")
	public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
		
	  String longUrl = urlShortenerService.getOriginalUrl(shortCode);
	  logger.debug("Short code: {} maps to Long URL: {}", shortCode, longUrl);
	  return ResponseEntity.status(HttpStatus.FOUND)
				             .location(URI.create(longUrl))
				             .build();
	}
	
	
	 @GetMapping("/hello")
	    public String hello() {
	        return "Hello, Swagger!";
	    }
}
