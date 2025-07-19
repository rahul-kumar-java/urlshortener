package com.urlshortener.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.urlshortener.controller.UrlShortenerController;
import com.urlshortener.entity.UrlMapping;
import com.urlshortener.exception.ResourceNotFoundException;
import com.urlshortener.repository.UrlRepository;
import com.urlshortener.util.Base62Encoder;

@SuppressWarnings("removal")
@ExtendWith(MockitoExtension.class)
public class UrlShortenerServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(UrlShortenerServiceTest.class);

	@Mock
	UrlRepository urlRepository;

	@InjectMocks
	private UrlShortenerServiceImpl urlShortenerService;

	@Mock
	RedisTemplate<String, String> redisTemplate;

	@Mock
	ValueOperations<String, String> valueOperations;

	@Test
	void testShortenUrl_Success() {

		// Arrange
		String longUrl = "https://www.google.com/search?q=spring+boot+microservices";
		UrlMapping savedMapping = new UrlMapping();
		savedMapping.setLongUrl(longUrl);
		savedMapping.setId(3L);
		savedMapping.setClickCount(0);
		savedMapping.setCreatedAt(LocalDateTime.now());

		// // Mock: URL not found in DB
		when(urlRepository.findByLongUrl(longUrl)).thenReturn(Optional.empty());

		// // Mock: First save returns object with ID
		when(urlRepository.save(any(UrlMapping.class))).thenReturn(savedMapping);
		String result = urlShortenerService.shortenUrl(longUrl);

		assertNotNull(result);
		assertEquals(Base62Encoder.encode(3L), result);

		// Verify
		verify(urlRepository).findByLongUrl(longUrl);
		verify(urlRepository, times(2)).save(any(UrlMapping.class));
		logger.info("Generated Short Code:  {} for longUrl:  {}", result, longUrl);
	}

	@Test
	void testShortenUrl_existingLongUrl_shouldReturnExistingShortCode() {

		String longUrl = "https://docs.spring.io/spring-boot/docs/current/reference/html/index.html";
		String expectedShortCode = "4";
		UrlMapping existingMapping = new UrlMapping();
		existingMapping.setId(4);
		existingMapping.setClickCount(0);
		existingMapping.setCreatedAt(LocalDateTime.now());
		existingMapping.setShortCode(expectedShortCode);

		when(urlRepository.findByLongUrl(longUrl)).thenReturn(Optional.of(existingMapping));
		String result = urlShortenerService.shortenUrl(longUrl);

		// Assert
		assertNotNull(result);
		assertEquals(expectedShortCode, result);

		// Verify: no save should happen because it already exists
		verify(urlRepository).findByLongUrl(longUrl);
		verify(urlRepository, never()).save(any(UrlMapping.class));
		logger.info("Returned existing Short Code:  {} for longUrl:  {}", result, longUrl);
	}

	@Test
	void testGetOriginalUrl_CacheHit_ShouldReturnFromRedis() {

		String shortCode = "1";
		String cachedUrl = "https://openai.com/research";

		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(shortCode)).thenReturn(cachedUrl);

		UrlMapping mockMapping = new UrlMapping();
		mockMapping.setShortCode(shortCode);
		mockMapping.setLongUrl(cachedUrl);
		mockMapping.setClickCount(1);
		when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(mockMapping));
		// Act
		String result = urlShortenerService.getOriginalUrl(shortCode);
		// Assert
		assertEquals(cachedUrl, result);
		verify(urlRepository).findByShortCode(shortCode);
		verify(urlRepository).save(mockMapping); // to update click count
		logger.info("cachedUrl as a result from redis:  {}  ", cachedUrl);
	}

	@Test
	void testGetOriginalUrl_CacheMiss_DBHit_ShouldReturnAndCache() {

		String shortCode = "a";
		String longUrl = "https://stackoverflow.com/questions/ask";

		UrlMapping mapping = new UrlMapping();
		mapping.setShortCode(shortCode);
		mapping.setLongUrl(shortCode);
		mapping.setLongUrl(longUrl);
		mapping.setClickCount(5);

		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(shortCode)).thenReturn(null);
		when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(mapping));

		// Act
		String result = urlShortenerService.getOriginalUrl(shortCode);

		// Assert
		assertEquals(longUrl, result);

		verify(urlRepository).save(mapping); // to update click count
		verify(valueOperations).set(shortCode, longUrl);
		logger.info("DB longUrl : {} for shortCode : {} ", longUrl, shortCode);
	}

	@Test
	void testGetOriginalUrl_NotFound_ShouldThrowException() {

		String shortCode = "invalid123";
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(shortCode)).thenReturn(null);
		when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> urlShortenerService.getOriginalUrl(shortCode));
		verify(urlRepository, never()).save(any());
		verify(valueOperations, never()).set(any(), any());
		logger.info("short code : {} is not found in DB. Throws ResourceNotFoundException", shortCode);
	}

}









