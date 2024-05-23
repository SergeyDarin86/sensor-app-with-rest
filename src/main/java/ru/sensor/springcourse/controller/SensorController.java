package ru.sensor.springcourse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SensorController {

    @PostMapping("/sensor/registration")
    public String getAllSensors(){
        //TODO: добавляет новый сенсор в БД
        return "Information about sensors";
    }

    @PostMapping("/measurements/add")
    public String addMeasurement(){
        //TODO: добавляет измерения в БД
        return "";
    }

    @GetMapping("/measurements")
    public String getMeasurements(){
        //TODO: возвращает все измерения из БД
        return "";
    }

    @GetMapping("/measurements/getRainyDaysCount")
    public String getRainyDaysCount(){
        //TODO: возвращает количество дождливых дней из БД
        return "";
    }

}
