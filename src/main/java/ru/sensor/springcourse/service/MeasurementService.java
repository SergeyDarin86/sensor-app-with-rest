package ru.sensor.springcourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sensor.springcourse.dto.MeasurementDTO;
import ru.sensor.springcourse.model.Measurement;
import ru.sensor.springcourse.repository.MeasurementRepository;

import java.time.LocalDateTime;
import java.util.List;

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

    @Transactional
    public void createMeasurement(MeasurementDTO measurementDTO) {
        measurementDTO.getSensor().setSensorId(sensorService.show(measurementDTO.getSensor().getName()).get().getSensorId());
        Measurement measurement = convertToMeasurement(measurementDTO);
        measurement.setMeasurementDate(LocalDateTime.now());
        measurementRepository.save(measurement);
    }

    public Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    public MeasurementDTO convertToMeasurementDto(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    public List<MeasurementDTO> getAllMeasurements() {
        return measurementRepository.findAll().stream().map(this::convertToMeasurementDto).toList();
    }

    public void enrichMeasurement(Measurement measurement) {
        measurement.setMeasurementDate(LocalDateTime.now());
    }

    public Integer getRainyDaysCount(Boolean raining) {
        return measurementRepository.countAllByRainingIs(raining);
    }

}
