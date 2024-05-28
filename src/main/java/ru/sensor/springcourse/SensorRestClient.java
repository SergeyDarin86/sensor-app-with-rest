package ru.sensor.springcourse;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


public class SensorRestClient {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String urlForSensor = "http://localhost:8080/allSensors";
        String responseForSensor = restTemplate.getForObject(urlForSensor, String.class);
        System.out.println(responseForSensor);
        System.out.println("==========================================================");

        registerSensor(restTemplate);
    }

    public static void registerSensor(RestTemplate restTemplate) {
        Map<String, String> request = new HashMap<>();
        request.put("name", "sens");
        String urlPost = "http://localhost:8080/sensor/registration";
        try {
            HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(request);
            String responseForPost = restTemplate.postForObject(urlPost, httpEntity, String.class);
            System.out.println(responseForPost);
        }catch (HttpClientErrorException e){
            System.out.println(e.getMessage());
        }
    }

}
