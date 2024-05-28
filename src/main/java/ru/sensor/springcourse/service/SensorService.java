package ru.sensor.springcourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sensor.springcourse.dto.SensorDTO;
import ru.sensor.springcourse.model.Sensor;
import ru.sensor.springcourse.repository.SensorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;

    private final ModelMapper modelMapper;

    public SensorService(SensorRepository sensorRepository, ModelMapper modelMapper) {
        this.sensorRepository = sensorRepository;
        this.modelMapper = modelMapper;
    }
    @Transactional
    public void createSensor(SensorDTO sensorDTO){
        sensorRepository.save(convertToSensor(sensorDTO));
    }

    public Sensor convertToSensor(SensorDTO sensorDTO){
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    public SensorDTO convertToSensorDto(Sensor sensor){
        return modelMapper.map(sensor, SensorDTO.class);
    }

    public Optional<Sensor> show(String name){
        return sensorRepository.findSensorByName(name);
    }

    //TODO: возможно этот метод лишний и его можно будет убрать
//    public Sensor show(int sensorId){
//        return sensorRepository.findById(sensorId).orElse(null);
//    }

    public List<SensorDTO>getAllSensors(){
        return sensorRepository.findAll().stream().map(this::convertToSensorDto).toList();
    }

}
