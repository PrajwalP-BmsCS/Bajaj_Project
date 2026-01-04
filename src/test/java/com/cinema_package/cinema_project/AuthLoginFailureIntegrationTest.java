package com.cinema_package.cinema_project;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class AuthLoginFailureIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldFailLoginWhenPasswordIsWrong() {

        // 🔹 Arrange: register user
        String email = "wrongpass_" + UUID.randomUUID() + "@test.com";
        String correctPassword = "password123";

        Map<String, Object> register = new HashMap<>();
        register.put("email", email);
        register.put("password", correctPassword);
        register.put("role", "USER");

        restTemplate.postForEntity("/auth/register", register, String.class);

        // 🔹 Act: login with WRONG password
        Map<String, Object> login = new HashMap<>();
        login.put("email", email);
        login.put("password", "incorrectPassword");

        ResponseEntity<String> response =
                restTemplate.postForEntity("/auth/login", login, String.class);

        // 🔴 Assert: authentication fails
        assertThat(response.getStatusCode())
                .isIn(HttpStatus.UNAUTHORIZED, HttpStatus.BAD_REQUEST,HttpStatus.INTERNAL_SERVER_ERROR);

        // 🔒 Ensure JWT is NOT returned
        assertThat(response.getBody()).doesNotContain("ey");
    }
}
