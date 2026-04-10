package com.RestTemplate.RestTemplate.Services;

import com.RestTemplate.RestTemplate.model.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class Communication {
    private final RestTemplate restTemplate;
    private final String URL = "http://94.198.50.185:7081/api/users";
    private String sessionID;

    public Communication(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public ResponseEntity<List<User>> getAllUsers() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<List<User>> response = restTemplate.exchange(
                URL,
                HttpMethod.GET,
                requestEntity,
                new org.springframework.core.ParameterizedTypeReference<List<User>>() {
                }
        );
        Cokie(response);
        return response;
    }

    public ResponseEntity<String> createUser(User user) {
        HttpHeaders headers = createSessionHeaders();
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                URL,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        return response;
    }

    public ResponseEntity<String> updateUser(User user) {
        HttpHeaders headers = createSessionHeaders();
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                URL,
                HttpMethod.PUT,
                requestEntity,
                String.class
        );
        return response;
    }

    public String deleteUser(Long userId) {
        HttpHeaders headers = createSessionHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return restTemplate.exchange(
                URL + "/" + userId, HttpMethod.DELETE, requestEntity, String.class).getBody();
    }

    private HttpHeaders createSessionHeaders() {
        HttpHeaders headers = new HttpHeaders();
        if (this.sessionID != null) {
            headers.add("Cookie", this.sessionID);
        }
        return headers;
    }

    public void Cokie(ResponseEntity<List<User>> response) {

        String setCookieHeader = response.getHeaders().getFirst("set-cookie");
        this.sessionID = setCookieHeader;
    }
}
