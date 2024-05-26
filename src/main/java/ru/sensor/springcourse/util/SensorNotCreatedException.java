package ru.sensor.springcourse.util;

public class SensorNotCreatedException extends RuntimeException{

    public SensorNotCreatedException(String errorMsg){
        super(errorMsg);
    }

}
