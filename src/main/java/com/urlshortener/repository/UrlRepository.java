package com.urlshortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.urlshortener.entity.UrlMapping;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlMapping, Long> {

	// find by short code
	Optional<UrlMapping> findByShortCode(String shortCode);
	
	Optional<UrlMapping> findByLongUrl(String longUrl);
}
