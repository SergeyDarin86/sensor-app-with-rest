package ru.sensor.springcourse.util;

public class MeasurementNotCreatedException extends RuntimeException{

    public MeasurementNotCreatedException(String errorMsg){
        super(errorMsg);
    }

}
