package ru.sensor.springcourse.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.sensor.springcourse.dto.MeasurementDTO;
import ru.sensor.springcourse.service.SensorService;

@Component
public class MeasurementValidator implements Validator {

    private final SensorService sensorService;

    public MeasurementValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MeasurementDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        MeasurementDTO measurementDTO = (MeasurementDTO) target;
        if (sensorService.show(measurementDTO.getSensor().getName()).isEmpty()) {
            errors.rejectValue("sensor", "", "Сенсора с таким названием нет в БД");
        }

    }

}
