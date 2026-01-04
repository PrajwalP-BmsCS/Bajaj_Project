package com.cinema_package.cinema_project;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

class VenueCrudIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void admin_can_crud_venue() {

        // 🔐 Register + login admin
        String email = "admin_" + UUID.randomUUID() + "@test.com";
        register(email, "password", "ADMIN");
        String token = loginAndGetJwt(email, "password");
        HttpHeaders headers = authHeaders(token);

        // CREATE
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
        System.out.println("xyz1");
        // READ
        ResponseEntity<Map> getRes =
                restTemplate.exchange(
                        "/venues/" + venueId,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        Map.class
                );

        assertThat(getRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getRes.getBody().get("name")).isEqualTo("PVR Forum");
        System.out.println("xyz2");
        // UPDATE
        // Map<String, Object> updateReq = Map.of(
        //         "name", "PVR Forum Mall",
        //         "city", "Bangalore"
        // );

        // ResponseEntity<Void> updateRes =
        //         restTemplate.exchange(
        //                 "/venues/" + venueId,
        //                 HttpMethod.PUT,
        //                 new HttpEntity<>(updateReq, headers),
        //                 Void.class
        //         );

        // assertThat(updateRes.getStatusCode()).isEqualTo(HttpStatus.OK);

        // DELETE
        ResponseEntity<Void> deleteRes =
                restTemplate.exchange(
                        "/venues/" + venueId,
                        HttpMethod.DELETE,
                        new HttpEntity<>(headers),
                        Void.class
                );

        assertThat(deleteRes.getStatusCode()).isEqualTo(HttpStatus.OK);
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
