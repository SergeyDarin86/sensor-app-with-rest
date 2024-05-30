package ru.sensor.springcourse.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.sensor.springcourse.dto.MeasurementDTO;
import ru.sensor.springcourse.dto.SensorDTO;
import ru.sensor.springcourse.repository.MeasurementRepository;
import ru.sensor.springcourse.service.MeasurementService;
import ru.sensor.springcourse.service.SensorService;
import ru.sensor.springcourse.util.*;

import java.util.List;

@RestController
public class SensorController {

    private final SensorService sensorService;

    private final MeasurementService measurementService;

    private final SensorValidator sensorValidator;

    private final MeasurementValidator measurementValidator;

    public SensorController(SensorService sensorService, MeasurementService measurementService, SensorValidator sensorValidator, MeasurementValidator measurementValidator) {
        this.sensorService = sensorService;
        this.measurementService = measurementService;
        this.sensorValidator = sensorValidator;
        this.measurementValidator = measurementValidator;
    }

    /**
     * Данный метод сделал для себя
     * @return
     */
    @GetMapping("/allSensors")
    public List<SensorDTO>getAllSensors(){
        return sensorService.getAllSensors();
    }

    @GetMapping("/measurements")
    public List<MeasurementDTO>getAllMeasurement(){
        return measurementService.getAllMeasurements();
    }

    @PostMapping("/sensor/registration")
    public ResponseEntity<HttpStatus> createSensor(@RequestBody @Valid SensorDTO sensorDTO,
                                                   BindingResult bindingResult) {
        sensorValidator.validate(sensorService.convertToSensor(sensorDTO),bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errorList = bindingResult.getFieldErrors();
            errorList.stream().forEach(fieldError -> errorMsg
                    .append(fieldError.getField())
                    .append(" - ").append(fieldError.getDefaultMessage())
                    .append(";"));

            throw new SensorNotCreatedException(errorMsg.toString());
        }
        sensorService.createSensor(sensorDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

//    @PostMapping("/measurements/add")
//    public MeasurementDTO addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
//                                         BindingResult bindingResult) {
//        //TODO: добавляет измерения в БД
//        measurementValidator.validate(measurementDTO,bindingResult);
//        if (bindingResult.hasErrors()){
//            System.out.println(bindingResult.getAllErrors());
//        }
//        measurementService.createMeasurement(measurementDTO);
//        return measurementDTO;
//    }

    @PostMapping("/measurements/add")
    public ResponseEntity<HttpStatus> createMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                         BindingResult bindingResult) {
        measurementValidator.validate(measurementDTO,bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errorList = bindingResult.getFieldErrors();
            errorList.stream().forEach(fieldError -> errorMsg
                    .append(fieldError.getField())
                    .append(" - ").append(fieldError.getDefaultMessage())
                    .append(";"));

            throw new MeasurementNotCreatedException(errorMsg.toString());
        }
        measurementService.createMeasurement(measurementDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/measurements/getRainyDaysCount")
    public String getRainyDaysCount() {
        return "The count of rainy days: " + measurementService.getRainyDaysCount(true);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> sensorHandlerException(SensorNotCreatedException e) {
        SensorErrorResponse response = new SensorErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        // В HTTP-ответе будет тело ответа (response) и статус в заголовке http-ответа
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> measurementHandlerException(MeasurementNotCreatedException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        // В HTTP-ответе будет тело ответа (response) и статус в заголовке http-ответа
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
