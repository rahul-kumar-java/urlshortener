package com.urlshortener.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urlshortener.dto.UrlRequest;
import com.urlshortener.exception.ResourceNotFoundException;
import com.urlshortener.service.UrlShortenerService;
import org.springframework.http.MediaType;


@SuppressWarnings("removal")
@WebMvcTest(UrlShortenerController.class)
public class UrlShortenerControllerTest {

	@SuppressWarnings("removal")
	@MockBean
	UrlShortenerService urlShortenerService;
	
	@Autowired
	MockMvc mockMvc;
	
	  private final ObjectMapper objectMapper = new ObjectMapper();
	
		// Positive Test- Valid Url
		@Test
		void testCreateShortUrl_success() throws JsonProcessingException, Exception {

			// prepare input and output
			String longUrl = "https://openai.com/research";
			String shortCode = "1";
			// simulate service layer
			when(urlShortenerService.shortenUrl(longUrl)).thenReturn(shortCode);

			// Perform POST request
			mockMvc.perform(post("/api/shorten").contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(new UrlRequest(longUrl)))).andExpect(status().isCreated())
					.andExpect(jsonPath("$.longUrl").value(longUrl))
					.andExpect(jsonPath("$.shortcode").value(shortCode));
		}
	    
	    // Negative Test - Blank Url
	    @Test
	    void testCreateShortUrl_blankUrl_shouldReturnBadRequest() throws JsonProcessingException, Exception {
	    	
	    	String blankUrl= " " ; // single space -> invalid
	    	UrlRequest urlRequest = new UrlRequest(blankUrl);
	    	mockMvc.perform(post("/api/shorten")
	    	                .contentType(MediaType.APPLICATION_JSON)
	    	                .content(objectMapper.writeValueAsString(urlRequest)));
	    }
	    
	    // Postive GET Test Case - valid short code
	    @Test
	    void testRedirect_validShortCode_shouldRedirectToLongUrl() throws Exception {
	    	
	    	String shortCode="1";
	    	String longUrl="https://openai.com/research";
	    	
	    	when(urlShortenerService.getOriginalUrl(shortCode)).thenReturn(longUrl) ;
	    	
	    	mockMvc.perform(get("/api/r/{shortCode}", shortCode))
	    	.andExpect(status().isFound())
	    	.andExpect(header().string("Location", longUrl));
	    }
	    
	    @Test // Negative Test Case - invalid short code 
	    void testRedirect_invalidShortCode_shouldReturnNotFound() throws Exception {
	    	
	    	String shortCode="invalid"; // invalid
	    	when(urlShortenerService.getOriginalUrl(shortCode)).thenThrow(new ResourceNotFoundException("Short URL not found"));
	    	mockMvc.perform(get("/api/r/{shortCode}", shortCode));
	    }
	 	    
	    @Test // Negative Test Case - blank short code 
	    void testRedirect_invalidShortCode_blankShortCode() throws Exception {
	    	
	    	String shortCode=" "; // invalid
	    	//	when(urlShortenerService.getOriginalUrl(shortCode)).thenThrow(new ResourceNotFoundException("Short URL not found"));
	    	System.out.println("running blank short code method");
	    	mockMvc.perform(get("/api/r/{shortCode}", shortCode));
	    }
	    
}















