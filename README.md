Title:  URL Shortener Project

Short Description: A simple URL Shortener application built using Spring Boot. It allows users to shorten long URLs and redirect using short codes.


##  Features

- Create short URLs from long ones
- Redirect short URLs to the original
- Redis caching for fast retrieval
- Input validation & custom exception handling
- API Documentation with Swagger UI
- Basic logging using SLF4J
- Deployed on local server
- Project pushed to GitHub


## Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Web, Spring Data JPA, Spring Validation
- MySQL
- Redis
- Swagger (springdoc-openapi)
- SLF4J Logging
- Maven


##  API Endpoints

### `POST /api/shorten`
http://localhost:8082/api/shorten

**Request Body:**
```json
{
  "longUrl": "https://developer.mozilla.org/en-US/docs/Web/JavaScript"
}
```

**Response:**
{
    "longUrl": "https://developer.mozilla.org/en-US/docs/Web/JavaScript",
    "shortcode": "c"
}


### `GET /api/r/{shortCode}`

http://localhost:8082/api/r/a

## Usage

Redirects to the original long URL based on `shortCode`.

Run Locally:

1. Clone the repo

git clone https://github.com/your-username/urlshortener.git
cd urlshortener
```

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

