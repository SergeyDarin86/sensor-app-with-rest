package ru.sensor.springcourse.util;

import ru.sensor.springcourse.dto.MeasurementDTO;

import java.util.List;

public class MeasurementResponse {

    public MeasurementResponse() {
    }

    public MeasurementResponse(List<MeasurementDTO> measurementDTOS) {
        this.measurementDTOS = measurementDTOS;
    }

    private List<MeasurementDTO> measurementDTOS;

    public List<MeasurementDTO> getMeasurementDTOS() {
        return measurementDTOS;
    }

    public void setMeasurementDTOS(List<MeasurementDTO> measurementDTOS) {
        this.measurementDTOS = measurementDTOS;
    }
}
