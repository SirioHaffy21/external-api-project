package com.example.loginclient.service;

import com.example.loginclient.utils.ApiUrl;
import com.example.loginclient.utils.HashUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalLoginService {

    private final String LOGIN_URL = ApiUrl.apiUrl + "/login";

    public boolean authenticate(String username, String password, HttpSession session) {
        //String apiUrl = "https://sale.crmviet.vn:8444/crm/api/v1/login"; // URL API login bên ngoài

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String hashedPassword = HashUtil.toMD5(password);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("password", hashedPassword);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(LOGIN_URL, entity, String.class);
            if (response.getBody().contains("fails")) {
                return false;
            }
            return response.getStatusCode().is2xxSuccessful(); // Trả về true nếu login thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi (login thất bại)
        }
    }

    public String loginAndGetToken(String username, String password) {
        RestTemplate restTemplate = new RestTemplate();
        String hashedPassword = HashUtil.toMD5(password);
        // Tạo request body
        String requestBody = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, hashedPassword);
        
        // Tạo HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    LOGIN_URL, HttpMethod.POST, request, String.class);
            
            // Chuyển JSON response thành Java Object
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("token").asText(); // Lấy giá trị token từ JSON

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    } 
}
