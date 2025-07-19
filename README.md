Title:  URL Shortener Project

Short Description: A lightweight URL Shortener application built using Spring Boot. It is designed to convert long URLs into short, manageable codes. Supports redirection, caching with Redis, validation, and full unit testing coverage.


##  Features

- Create short URLs from long ones
- Redirect short URLs to the original
- Redis caching for fast retrieval
- Input validation & custom exception handling
- API Documentation via Swagger UI
- Basic logging using SLF4J
- Unit Tests for Controller and Service Layers
- Deployed on local server
- Project pushed to GitHub


## Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Web, Spring Data JPA, Spring Validation
- MySQL
- Redis
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



## Run Locally:

1. Clone the repo

git clone https://github.com/rahul-kumar-java/urlshortener.git
cd urlshortener


2. Configure MySQL and Redis
Update your `application.properties` with the correct credentials.

3. Run the application
./mvnw spring-boot:run


## Swagger UI

Access API documentation at:

http://localhost:8082/swagger-ui.html


## Testing

- Tested using Postman
- Validated with Swagger UI

## Author

  Rahul Kumar

