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

class BookingFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void adminCreatesShow_and_userBooksTicketSuccessfully() {

        /* =========================
           1️⃣ REGISTER ADMIN + USER
           ========================= */

        String adminEmail = "admin_" + UUID.randomUUID() + "@test.com";
        String userEmail  = "user_" + UUID.randomUUID() + "@test.com";
        String password  = "password123";

        register(adminEmail, password, "ADMIN");
        register(userEmail, password, "USER");

        String adminToken = loginAndGetJwt(adminEmail, password);
        String userToken  = loginAndGetJwt(userEmail, password);

        /* =========================
           2️⃣ ADMIN CREATES VENUE
           ========================= */
        System.out.println("xyz1");
        HttpHeaders adminHeaders = authHeaders(adminToken);
        System.out.println(adminHeaders);
        Map<String, Object> venueReq = Map.of(
        "name", "PVR Orion",
        "city", "Bangalore"
        );


        Long venueId = postAndGetId("/venues", venueReq, adminHeaders);
        /* =========================
           3️⃣ ADMIN CREATES MOVIE
           ========================= */
        System.out.println("xyz2");
        Map<String, Object> movieReq = Map.of(
        "title", "Interstellar",
        "description", "Epic sci-fi movie",
        "director", "Christopher Nolan",
        "genre", "SCI_FI",
        "date", "2026-01-03",
        "location", "Bangalore",
        "totalSeats", 100,
        "availableSeats", 100,
        "price", 250,
        "venueId", venueId
        );

        restTemplate.postForEntity(
        "/movie",
        new HttpEntity<>(movieReq, adminHeaders),
        Void.class
        );

ResponseEntity<List<Map<String, Object>>> movieResponse =
    restTemplate.exchange(
        "/movie",
        HttpMethod.GET,
        new HttpEntity<>(adminHeaders),
        new ParameterizedTypeReference<>() {}
    );

assertThat(movieResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
assertThat(movieResponse.getBody()).isNotEmpty();

Long movieId = Long.valueOf(
    movieResponse.getBody().get(0).get("id").toString()
);

        /* =========================
           4️⃣ ADMIN CREATES SHOW
           ========================= */
        System.out.println("xyz3");
        Map<String, Object> showReq = Map.of(
        "movieId", movieId,
        "startTime", "2025-01-03T18:30:00",
        "endTime", "2026-01-03T21:30:00",
        "totalSeats", 100,
        "availableSeats", 100,
        "regularSeatPrice", 250,
        "premiumSeatPrice", 400
        );


        Long showId = postAndGetId("/shows", showReq, adminHeaders);

        /* =========================
           5️⃣ USER HOLDS SEATS
           ========================= */
        System.out.println("xyz4");
        HttpHeaders userHeaders = authHeaders(userToken);

        Map<String, Object> holdReq = Map.of(
                "showId", showId,
                "seats", List.of("A1", "A2")
        );
        System.out.println("xyz5");
ResponseEntity<String> holdResponse =
        restTemplate.postForEntity(
                "/movie/booking/hold",
                new HttpEntity<>(holdReq, userHeaders),
                String.class
        );

assertThat(holdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
System.out.println("Hold response: " + holdResponse.getBody());


        System.out.println("xyz6");

         Map<String, Object> bookReq = Map.of(
                "showId", showId,
                "seats", List.of("A1", "A2")
        );
        System.out.println("xyz7");
ResponseEntity<String> bookResponse =
        restTemplate.postForEntity(
                "/movie/booking/seats",
                new HttpEntity<>(bookReq, userHeaders),
                String.class
        );

assertThat(bookResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
System.out.println("Book response: " + bookResponse.getBody());
    }

    /* =====================================================
       🔽 HELPER METHODS (KEEP TEST CLEAN)
       ===================================================== */

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
