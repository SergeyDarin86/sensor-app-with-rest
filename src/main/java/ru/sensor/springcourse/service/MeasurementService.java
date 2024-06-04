package ru.sensor.springcourse.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sensor.springcourse.dto.MeasurementDTO;
import ru.sensor.springcourse.dto.SearchDTO;
import ru.sensor.springcourse.model.Measurement;
import ru.sensor.springcourse.repository.MeasurementRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    public List<MeasurementDTO> getAllMeasurements() {
        return measurementRepository.findAll().stream().map(this::convertToMeasurementDto).toList();
    }

    public Integer getRainyDaysCount(Boolean raining) {
        return measurementRepository.countAllByRainingIs(raining);
    }

    /**
     * работа с SearchDTO
     */
//    public List<MeasurementDTO> getMeasurementsBetweenDates(SearchDTO searchDTO) {
//        return measurementRepository.findMeasurementByMeasurementDateBetween(
//                convertedStringToLocalDate(searchDTO.getDateFrom()),
//                convertedStringToLocalDate(searchDTO.getDateTo())
//        ).stream().map(this::convertToMeasurementDto).toList();
//    }

    // метод работает с переменными из PathVariable
    public List<MeasurementDTO> getMeasurementsBetweenDates(String dateFrom, String dateTo) {
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
