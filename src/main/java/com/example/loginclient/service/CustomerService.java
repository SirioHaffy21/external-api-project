package com.example.loginclient.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.loginclient.model.Customer;
import com.example.loginclient.utils.ApiUrl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CustomerService {
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Map<String, Object>> getCustomers(HttpSession session) {
        String apiUrl = ApiUrl.apiUrl + "/customers/all"; // API danh sách khách hàng
        String token = (String) session.getAttribute("authToken");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Nhận phản hồi dạng String thay vì List
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
            String responseBody = response.getBody();

            // Kiểm tra nếu phản hồi là JSON rồi parse nó
            ObjectMapper objectMapper = new ObjectMapper();
            if (responseBody != null && responseBody.contains("success")) { // Nếu bắt đầu với [ có thể là JSON Array
                List<Map<String, Object>> customers = objectMapper.readValue(responseBody, List.class);
                return customers; // Trả về danh sách khách hàng
            } else {
                System.out.println("API không trả về JSON như mong đợi. Nội dung phản hồi:");
                System.out.println(responseBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Customer> fetchCustomers(String token) {
        String url = ApiUrl.apiUrl + "/customers/all"; // Thay URL API thật của bạn

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class);
            // Parse JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            JsonNode customersNode = jsonNode.get("customers");

            return objectMapper.readValue(customersNode.toString(), new TypeReference<List<Customer>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Bean
    public RestTemplate restTemplate() {
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter()); // Thêm converter xử lý text
        return restTemplate;
    }

}
