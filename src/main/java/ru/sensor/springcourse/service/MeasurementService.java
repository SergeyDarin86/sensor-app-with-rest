package ru.sensor.springcourse.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sensor.springcourse.dto.MeasurementDTO;
import ru.sensor.springcourse.dto.SearchDTO;
import ru.sensor.springcourse.model.Measurement;
import ru.sensor.springcourse.repository.MeasurementRepository;
import ru.sensor.springcourse.util.MeasurementResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MeasurementService {

    MeasurementRepository measurementRepository;

    SensorService sensorService;

    ModelMapper modelMapper;

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

    public MeasurementResponse getAllMeasurements() {
        log.info("Start method getAllMeasurements");
        return new MeasurementResponse(measurementRepository.findAll().stream().map(this::convertToMeasurementDto).toList());
    }

    public Integer getRainyDaysCount(Boolean raining) {
        log.info("Start method getRainyDaysCount; raining is: {}", raining);
        return measurementRepository.countAllByRainingIs(raining);
    }

    /**
     * работа с SearchDTO
     */
    public MeasurementResponse getMeasurementsBetweenDatesWithSearchDTO(SearchDTO searchDTO) {
        log.info("Start method getMeasurementsBetweenDatesWithSearchDTO of measurementService; dateFrom is: {}, dateTo is: {}", searchDTO.getDateFrom(), searchDTO.getDateTo());
        return new MeasurementResponse(measurementRepository.findMeasurementByMeasurementDateBetween(
                searchDTO.getDateFrom().atStartOfDay(), searchDTO.getDateTo().atStartOfDay()
        ).stream().map(this::convertToMeasurementDto).toList());
    }

    // метод работает с переменными из PathVariable
    public List<MeasurementDTO> getMeasurementsBetweenDates(String dateFrom, String dateTo) {
        log.info("Start method getMeasurementsBetweenDates of measurementService; dateFrom is: {}, dateTo is: {}", dateFrom, dateTo);
        return measurementRepository.findMeasurementByMeasurementDateBetween(
                convertedStringToLocalDate(dateFrom),
                convertedStringToLocalDate(dateTo)
        ).stream().map(this::convertToMeasurementDto).toList();
    }

    public LocalDateTime convertedStringToLocalDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(stringDate, formatter).atStartOfDay();
    }

}
