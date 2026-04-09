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
    //private HttpHeaders headers;

    public Communication(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }





    public ResponseEntity<List<User>> getAllUsers() {
        HttpHeaders headers = new HttpHeaders();
        // На первом запросе cookie не нужны
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<User>> response = restTemplate.exchange(
                URL,
                HttpMethod.GET,
                requestEntity,
                new org.springframework.core.ParameterizedTypeReference<List<User>>() {
                } // Важно для работы с коллекциями
        );
        Cokie(response);

        return response;
    }

    public ResponseEntity<String> createUser(User user) {
        HttpHeaders headers = createSessionHeaders();
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);

        // POST запрос на /api/users
        ResponseEntity<String> response = restTemplate.exchange(
                URL,
                HttpMethod.POST,
                requestEntity,
                String.class // Ожидаем String (первую часть кода)
        );
        //Cookie(response);
        return response;
    }

    public ResponseEntity<String> updateUser(User user) {
        HttpHeaders headers = createSessionHeaders();
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);

        // PUT запрос на /api/users
        ResponseEntity<String> response = restTemplate.exchange(
                URL,
                HttpMethod.PUT,
                requestEntity,
                String.class // Ожидаем String (вторую часть кода)
        );
        Cookie(response);

        return response;
    }

    public String deleteUser(Long userId) {
        HttpHeaders headers = createSessionHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(headers); // Для DELETE тела обычно нет
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        // DELETE запрос на /api/users/{id}
        /*String url = URL + "/" + userId;
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                requestEntity,
                String.class // Ожидаем String (третью часть кода)
        );
        return response;*/

        return restTemplate.exchange(
                URL + "/" + userId, HttpMethod.DELETE, requestEntity, String.class).getBody();
    }

    private HttpHeaders createSessionHeaders() {
        HttpHeaders headers = new HttpHeaders();
        if (this.sessionID != null) {
            // Вставляем полученный session id в заголовок Cookie
            //System.out.println(this.sessionID);
            headers.add("Cookie", this.sessionID);
        }
        return headers;
    }

    public void Cokie(ResponseEntity<List<User>> response) {

        String setCookieHeader = response.getHeaders().getFirst("set-cookie");
        /*if (setCookieHeader != null) {
            String[] parts = setCookieHeader.split(";");
            this.sessionID = parts[0];
            System.out.println("Сохранен Session ID: " + this.sessionID);
        }*/
        this.sessionID = setCookieHeader;
    }

    public void Cookie(ResponseEntity<String> response) {

        String setCookieHeader = response.getHeaders().getFirst("set-cookie");
        /*if (setCookieHeader != null) {
            String[] parts = setCookieHeader.split(";");
            this.sessionID = parts[0];
            System.out.println("Сохранен Session ID: " + this.sessionID);
        }*/
        this.sessionID = setCookieHeader;
    }
}
