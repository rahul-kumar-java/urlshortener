package com.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UrlRequest {

	@NotBlank(message = "Long Url can not be empty")
	private String longUrl;
}
