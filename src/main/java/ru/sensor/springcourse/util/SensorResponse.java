package ru.sensor.springcourse.util;

import ru.sensor.springcourse.dto.SensorDTO;

import java.util.List;

public class SensorResponse {

    private List<SensorDTO>sensorDTOList;

    public SensorResponse(List<SensorDTO> sensorDTOList) {
        this.sensorDTOList = sensorDTOList;
    }

    public List<SensorDTO> getSensorDTOList() {
        return sensorDTOList;
    }

    public void setSensorDTOList(List<SensorDTO> sensorDTOList) {
        this.sensorDTOList = sensorDTOList;
    }
}
