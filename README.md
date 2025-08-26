#  URL Shortener Project

A lightweight URL Shortener application built with Spring Boot, designed to convert long URLs into short, easy-to-share links. Features include URL redirection, Redis caching, Input Validation, Centralized exception handling and Docker support for containerized deployment.


##  Features
- Create short URLs from long ones
- Redirect short URLs to the original
- Redis caching for fast retrieval
- Input validation & custom exception handling
- API Documentation via Swagger UI
- Basic logging using SLF4J
- Unit Tests for Controller and Service Layers
- Dockerized setup for Spring Boot, MySQL and Redis


## Tech Stack
- Java 21
- Spring Boot 3.x
- Spring Web, Spring Data JPA, Spring Validation
- MySQL 8
- Redis 7
- Swagger UI (springdoc-openapi)
- SLF4J Logging
- Maven
- JUnit & Mockito


##  API Endpoints

### `POST /api/shorten`
creates a short URL from a long one.

**Request Body:**
```json
{
  "longUrl": "https://developer.mozilla.org/en-US/docs/Web/JavaScript"
}
```
URL:
http://localhost:8082/api/shorten

**Response:**
{
    "longUrl": "https://developer.mozilla.org/en-US/docs/Web/JavaScript",
    "shortcode": "c"
}


### `GET /api/r/{shortCode}`

http://localhost:8082/api/r/a

## Usage
Redirects to the original long URL based on `shortCode`.


## Testing
 Unit tested with JUnit 5 & Mockito

### Test Coverage:
- POST and GET controller endpoints
- Service layer logic (with Redis and DB interaction)
- Validated with Postman and Swagger UI

## Docker Setup and Usage
This project supports Docker Compose for setup of MySQL, Redis and Spring Boot.

Steps to run with Docker:-

1: Clone the repository-

  ```
git clone https://github.com/rahul-kumar-java/urlshortener.git
cd urlshortener
```
   

2: Build the Spring Boot Docker image-

    sudo docker-compose build
    
    
3: Start the containers

    sudo docker-compose up
    
4: Services:

 Spring Boot app -> http://localhost:8082
 
 MySQL -> port 3307 (from .env)
 
 Redis -> port 6380 (from .env)

✅ MySQL data is persisted using a Docker volume (mysql_data).

✅ Sensitive environment variables (passwords, ports) are managed via .env file.

## Swagger UI

Access API documentation at:

http://localhost:8082/swagger-ui.html


## Testing
- Verified with Postman and Swagger UI

## What I Learned
 - Best practices in Docker Compose (using .env and volumes)
 - Handling service dependencies in Docker Compose using `healthchecks` and `depends_on`
 - Securing sensitive info via environment variables
 - Clean API desing using Spring Boot + Swagger
 - Using Redis caching for performance optimization



## Author
  Rahul Kumar

