package com.urlshortener.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShortenUrlResponse {

    private String longUrl;
    private String shortcode;
}
