package ru.sensor.springcourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sensor.springcourse.dto.MeasurementDTO;
import ru.sensor.springcourse.model.Measurement;
import ru.sensor.springcourse.model.Sensor;
import ru.sensor.springcourse.repository.MeasurementRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    private final SensorService sensorService;

    private final ModelMapper modelMapper;

    public MeasurementService(MeasurementRepository measurementRepository, SensorService sensorService, ModelMapper modelMapper) {
        this.measurementRepository = measurementRepository;
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
    }

//    @Transactional
//    public void createMeasurement(MeasurementDTO measurementDTO){
//
//        Optional<Sensor>optionalSensor = sensorService.show(measurementDTO.getSensor().getName());
//        if (optionalSensor.isPresent()){
//            measurementDTO.getSensor().setSensorId(optionalSensor.get().getSensorId());
//            Measurement measurement = convertToMeasurement(measurementDTO);
//            measurement.setMeasurementDate(LocalDateTime.now());
//            measurementRepository.save(measurement);
//        }
//
//    }

    // TODO: переделать данный метод
    @Transactional
    public void createMeasurement(MeasurementDTO measurementDTO){

        Optional<Sensor>optionalSensor = sensorService.show(measurementDTO.getSensor().getName());
        if (optionalSensor.isPresent()){
            measurementDTO.getSensor().setSensorId(optionalSensor.get().getSensorId());
            Measurement measurement = convertToMeasurement(measurementDTO);
            measurement.setMeasurementDate(LocalDateTime.now());
            measurementRepository.save(measurement);
        }

    }

    public Measurement convertToMeasurement(MeasurementDTO measurementDTO){
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    public MeasurementDTO convertToMeasurementDto(Measurement measurement){
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    public List<MeasurementDTO>getAllMeasurements(){
        return measurementRepository.findAll().stream().map(this::convertToMeasurementDto).toList();
    }

    public void enrichMeasurement(Measurement measurement){
        measurement.setMeasurementDate(LocalDateTime.now());
    }

}
