package ru.sensor.springcourse;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.sensor.springcourse.model.Sensor;

import java.util.*;


public class SensorRestClient {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
//        getAllSensors(restTemplate);
//        registerSensor(restTemplate);
//        addMeasurement(restTemplate);
//        getAllMeasurements(restTemplate);

        getRainyDaysCount(restTemplate);
    }

    // регистрация нового сенсора
    public static void registerSensor(RestTemplate restTemplate) {
        Map<String, String> request = new HashMap<>();
        request.put("name", "sensorC");
        String urlPost = "http://localhost:8080/sensor/registration";
        try {
            HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(request);
            String responseForPost = restTemplate.postForObject(urlPost, httpEntity, String.class);
            System.out.println(responseForPost);
        } catch (HttpClientErrorException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getAllSensors(RestTemplate restTemplate){
        String urlForSensor = "http://localhost:8080/allSensors";
        String responseForSensor = restTemplate.getForObject(urlForSensor, String.class);
        System.out.println(responseForSensor);
    }
    // получение всех измерений
    public static void getAllMeasurements(RestTemplate restTemplate) {
        String urlGet = "http://localhost:8080/measurements";
        String responseForGet = restTemplate.getForObject(urlGet,String.class);
        System.out.println(responseForGet);
    }

    // получение количества дождливых дней
    public static void getRainyDaysCount(RestTemplate restTemplate) {
        String urlGet = "http://localhost:8080/measurements/getRainyDaysCount";
        String responseForGet = restTemplate.getForObject(urlGet,String.class);
        System.out.println(responseForGet);
    }

    // добавление измерений
    public static void addMeasurement(RestTemplate restTemplate) {
        //создаем лист сенсоров
        // можно попробовать получить сенсоры из БД и уже их перебирать
        // создать нужный ендпоинт

        List<Sensor> sensorList = createSensorList();

        List<Boolean> listBoolean = new ArrayList<>();
        listBoolean.add(true);
        listBoolean.add(false);

        Random random = new Random();
        for (int i = 0; i < 1000; i++) {

            float generatedFloat = random.nextFloat(-100, 100);
            float newFloat = (float) (Math.round(generatedFloat * 10.0) / 10.0);

            int indexBoolean = random.nextInt(listBoolean.size());
            int indexSensor = random.nextInt(sensorList.size());

            Map<String, Object> request = new HashMap<>();
            request.put("raining", listBoolean.get(indexBoolean));
            request.put("value", newFloat);
            request.put("sensor", sensorList.get(indexSensor));
            String urlPost = "http://localhost:8080/measurements/add";
            try {
                HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request);
                String responseForPost = restTemplate.postForObject(urlPost, httpEntity, String.class);
                System.out.println(responseForPost);
            } catch (HttpClientErrorException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public static List<Sensor> createSensorList() {
        List<Sensor> sensorList = new ArrayList<>();
        Sensor sensorA = new Sensor();
        sensorA.setName("sensorA");
        Sensor sensorB = new Sensor();
        sensorB.setName("sensorB");
        Sensor sensorC = new Sensor();
        sensorC.setName("sensorC");
        sensorList.add(sensorA);
        sensorList.add(sensorB);
        sensorList.add(sensorC);
        return sensorList;
    }

}
