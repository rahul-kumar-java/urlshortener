package com.urlshortener.service;

import java.time.LocalDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.urlshortener.entity.UrlMapping;
import com.urlshortener.exception.ResourceNotFoundException;
import com.urlshortener.repository.UrlRepository;
import com.urlshortener.util.Base62Encoder;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {

	private static final Logger logger = LoggerFactory.getLogger(UrlShortenerServiceImpl.class);
	
	@Autowired
	UrlRepository urlRepository;
	
	@Autowired
	RedisTemplate<String, String> redisTemplate;
	
	@Override
	public String shortenUrl(String longUrl) {
       
		// check longUrl is exist or not in db
	Optional<UrlMapping> object=urlRepository.findByLongUrl(longUrl);
    String shortCode=object.map(UrlMapping::getShortCode).orElseGet(() ->
	{
		UrlMapping urlMapper = new UrlMapping();		
			
			urlMapper.setLongUrl(longUrl);
			urlMapper.setCreatedAt(LocalDateTime.now());
			urlMapper.setClickCount(0);
			urlMapper = urlRepository.save(urlMapper);

			String code = 	Base62Encoder.encode(urlMapper.getId());
		urlMapper.setShortCode(code);	
		urlRepository.save(urlMapper);
		return code;
	});
		return shortCode;
	}

	@Override
	public String getOriginalUrl(String shortCode) {

//	Optional<UrlMapping> optionalObj=urlRepository.findByShortCode(shortCode);
		/*
		 * optionalObj.ifPresent(m -> {
		 * 
		 * m.setClickCount(m.getClickCount()+1); urlRepository.save(m); }); return
		 * optionalObj.map(UrlMapping::getLongUrl).orElse(null);
		 */
		String cachedUrl = redisTemplate.opsForValue().get(shortCode);
		if (cachedUrl != null) {
			logger.debug("Received Long URL from cache: {}", cachedUrl);
			// update click count even on cache hit
			Optional<UrlMapping> mapping = urlRepository.findByShortCode(shortCode);
			mapping.ifPresent(m -> {
				m.setClickCount(m.getClickCount() + 1);
				urlRepository.save(m);
			});
			return cachedUrl;
		} else {
			logger.warn("Cache missed for short code: {}. Falling back to DB", shortCode);
			Optional<UrlMapping> optionalObj = urlRepository.findByShortCode(shortCode);
			optionalObj.ifPresent(m -> {
				m.setClickCount(m.getClickCount() + 1);
				urlRepository.save(m);
			});
// return	optionalObj.map(UrlMapping::getShortCode).orElse(null);
			return optionalObj.map(obj -> {
				// cache in Redis
				redisTemplate.opsForValue().set(shortCode, obj.getLongUrl());
				return obj.getLongUrl();
			}).orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));
		}
	}
  }









