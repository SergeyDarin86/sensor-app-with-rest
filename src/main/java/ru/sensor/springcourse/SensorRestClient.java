package ru.sensor.springcourse;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


public class SensorRestClient {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String urlForSensor = "http://localhost:8080/allSensors";
        String responseForSensor = restTemplate.getForObject(urlForSensor, String.class);
        System.out.println(responseForSensor);

        Map<String,String> request = new HashMap<>();
        request.put("name", "weatherSensor");

        // чтобы отправить json, нам необходимо упаковать нашу мапу
        // мы не можем просто так отправить наш Map по сети
        String urlPost = "http://localhost:8080/sensor/registration";
        HttpEntity<Map<String,String>> httpEntity = new HttpEntity<>(request);
        System.out.println("==========================================================");
        String responseForPost = restTemplate.postForObject(urlPost,httpEntity,String.class);
        System.out.println(responseForPost);
    }
}
