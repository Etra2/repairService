package com.repair_service.repairsystem;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TechnicianLoginTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testTechnicianLogin() throws Exception {
        // Endpoint logowania
        String url = "http://localhost:" + port + "/api/auth/login";

        //Dane technika z bazy (upewnienie się, że hasło jest zakodowane w BCrypt)
        Map<String, String> loginRequest = Map.of(
                "email", "technik1@repair.com",
                "password", "12345"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(loginRequest), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        // Sprawdzenie, czy status HTTP to 200 OK
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Sprawdzenie, czy odpowiedź zawiera token
        assertThat(response.getBody()).contains("token");

        System.out.println("Odpowiedź logowania technika: " + response.getBody());
    }
}
