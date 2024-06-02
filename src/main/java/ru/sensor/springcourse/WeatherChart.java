package ru.sensor.springcourse;

import org.knowm.xchart.CategoryChart;

public interface WeatherChart<C>{
    CategoryChart getChart();
}
