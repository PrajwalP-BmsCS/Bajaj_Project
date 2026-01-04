package com.cinema_package.cinema_project;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class AuthIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldRegisterUserSuccessfully() {
        // unique email to avoid conflicts
        String email = "user_" + UUID.randomUUID() + "@test.com";

        Map<String, Object> request = new HashMap<>();
        request.put("email", email);
        request.put("password", "password123");
        request.put("role", "USER");

        ResponseEntity<String> response =
                restTemplate.postForEntity("/auth/register", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotBlank();
    }

    @Test
    void shouldLoginSuccessfullyAndReturnJwt() {
        String email = "login_" + UUID.randomUUID() + "@test.com";
        String password = "password123";

        // 🔹 Register first
        Map<String, Object> register = new HashMap<>();
        register.put("email", email);
        register.put("password", password);
        register.put("role", "USER");

        restTemplate.postForEntity("/auth/register", register, String.class);

        // 🔹 Login
        Map<String, Object> login = new HashMap<>();
        login.put("email", email);
        login.put("password", password);

        ResponseEntity<String> response =
                restTemplate.postForEntity("/auth/login", login, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotBlank()
                .startsWith("ey"); // JWT tokens start with eyJ...
    }
}
