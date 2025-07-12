package com.urlshortener.service;

public interface UrlShortenerService {

	public String shortenUrl(String longUrl);
	
    public String getOriginalUrl(String shortCode);
	
}
