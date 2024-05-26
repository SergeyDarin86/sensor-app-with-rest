package ru.sensor.springcourse.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.sensor.springcourse.model.Sensor;
import ru.sensor.springcourse.service.SensorService;

@Component
public class SensorValidator implements Validator {

    private final SensorService sensorService;

    public SensorValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Sensor sensor = (Sensor) target;

        if (sensorService.show(sensor.getName()).isPresent()) {
            if (sensorService.show(sensor.getName()).get().getSensorId() != sensor.getSensorId()) {
                errors.rejectValue("name", "", "Сенсор с таким названием уже существует");
            }
        }

    }

}
