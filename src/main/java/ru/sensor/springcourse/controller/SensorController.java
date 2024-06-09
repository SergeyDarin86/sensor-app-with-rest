package ru.sensor.springcourse.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sensor.springcourse.dto.SensorDTO;
import ru.sensor.springcourse.service.SensorService;
import ru.sensor.springcourse.util.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SensorController {

    SensorService sensorService;

    SensorValidator sensorValidator;

    @GetMapping("/allSensors")
    public List<SensorDTO> getAllSensors() {
        return sensorService.getAllSensors();
    }

    @PostMapping("/sensor/registration")
    public ResponseEntity<HttpStatus> createSensor(@RequestBody @Valid SensorDTO sensorDTO,
                                                   BindingResult bindingResult) {
        sensorValidator.validate(sensorService.convertToSensor(sensorDTO), bindingResult);

        ExceptionBuilder.buildErrorMessageForClient(bindingResult);

        sensorService.createSensor(sensorDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> sensorHandlerException(MeasurementException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        // В HTTP-ответе будет тело ответа (response) и статус в заголовке http-ответа
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
