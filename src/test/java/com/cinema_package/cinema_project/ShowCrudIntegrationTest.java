package com.cinema_package.cinema_project;


import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

class ShowCrudIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void admin_can_crud_show() {

        String email = "admin_" + UUID.randomUUID() + "@test.com";
        register(email, "password", "ADMIN");
        String token = loginAndGetJwt(email, "password");
        HttpHeaders headers = authHeaders(token);


        Map<String, Object> createReq = Map.of(
                "name", "PVR Forum",
                "city", "Bangalore"
        );

        ResponseEntity<Map> createRes =
                restTemplate.postForEntity(
                        "/venues",
                        new HttpEntity<>(createReq, headers),
                        Map.class
                );

        assertThat(createRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        Long venueId = Long.valueOf(createRes.getBody().get("id").toString());

        // CREATE (no body returned)
        Map<String, Object> movieReq = Map.of(
                "title", "Inception",
                "description", "Dream thriller",
                "director", "Nolan",
                "genre", "SCI_FI",
                "date", "2026-01-03",
                "price", 300,
                "venueId", venueId
        );

        restTemplate.postForEntity(
                "/movie",
                new HttpEntity<>(movieReq, headers),
                Void.class
        );

        ResponseEntity<List<Map<String, Object>>> movieResponse =
    restTemplate.exchange(
        "/movie",
        HttpMethod.GET,
        new HttpEntity<>(headers),
        new ParameterizedTypeReference<>() {}
    );

assertThat(movieResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
assertThat(movieResponse.getBody()).isNotEmpty();

Long movieId = Long.valueOf(
    movieResponse.getBody().get(0).get("id").toString()
);

        // CREATE
        Map<String, Object> showReq = Map.of(
                "movieId", movieId,
                "startTime", "2026-01-03T18:30:00",
                "endTime", "2026-01-03T21:30:00",
                "totalSeats", 100,
                "availableSeats", 100,
                "regularSeatPrice", 250,
                "premiumSeatPrice", 400
        );

        ResponseEntity<Map> createResS =
                restTemplate.postForEntity(
                        "/shows",
                        new HttpEntity<>(showReq, headers),
                        Map.class
                );

        Long showId = Long.valueOf(createResS.getBody().get("id").toString());

        // READ
ResponseEntity<List<Map<String, Object>>> getRes =
        restTemplate.exchange(
                "/shows/movie/" + movieId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );

assertThat(getRes.getStatusCode()).isEqualTo(HttpStatus.OK);
assertThat(getRes.getBody()).isNotEmpty();


        // DELETE
        // ResponseEntity<Void> deleteRes =
        //         restTemplate.exchange(
        //                 "/shows/" + showId,
        //                 HttpMethod.DELETE,
        //                 new HttpEntity<>(headers),
        //                 Void.class
        //         );

        // assertThat(deleteRes.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
        // Helper methods for registration and login
        private void register(String email, String password, String role) {
        Map<String, Object> req = Map.of(
                "email", email,
                "password", password,
                "role", role
        );
        restTemplate.postForEntity("/auth/register", req, String.class);
    }

    private String loginAndGetJwt(String email, String password) {
        Map<String, Object> req = Map.of(
                "email", email,
                "password", password
        );

        ResponseEntity<String> response =
                restTemplate.postForEntity("/auth/login", req, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }

    private HttpHeaders authHeaders(String jwt) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }


    private Long postAndGetId(String url, Object body, HttpHeaders headers) {
        ResponseEntity<Map> response =
                restTemplate.postForEntity(
                        url,
                        new HttpEntity<>(body, headers),
                        Map.class
                );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return Long.valueOf(response.getBody().get("id").toString());
    }

}
