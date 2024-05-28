package ru.sensor.springcourse.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.sensor.springcourse.dto.MeasurementDTO;
import ru.sensor.springcourse.dto.SensorDTO;
import ru.sensor.springcourse.service.MeasurementService;
import ru.sensor.springcourse.service.SensorService;
import ru.sensor.springcourse.util.SensorErrorResponse;
import ru.sensor.springcourse.util.SensorNotCreatedException;
import ru.sensor.springcourse.util.SensorValidator;

import java.util.List;

@RestController
public class SensorController {

    private final SensorService sensorService;

    private final MeasurementService measurementService;

    private final SensorValidator sensorValidator;

    public SensorController(SensorService sensorService, MeasurementService measurementService, SensorValidator sensorValidator) {
        this.sensorService = sensorService;
        this.measurementService = measurementService;
        this.sensorValidator = sensorValidator;
    }

    /**
     * Данный метод сделал для себя
     * @return
     */
    @GetMapping("/allSensors")
    public List<SensorDTO>getAllSensors(){
        return sensorService.getAllSensors();
    }

    @GetMapping("/allMeasurements")
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

    @PostMapping("/measurements/add")
    public MeasurementDTO addMeasurement(@RequestBody MeasurementDTO measurementDTO) {
        //TODO: добавляет измерения в БД
        measurementService.createMeasurement(measurementDTO);
        return measurementDTO;
    }

    @GetMapping("/measurements")
    public String getMeasurements() {
        //TODO: возвращает все измерения из БД
        return "";
    }

    @GetMapping("/measurements/getRainyDaysCount")
    public String getRainyDaysCount() {
        //TODO: возвращает количество дождливых дней из БД
        return "";
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handlerException(SensorNotCreatedException e) {
        SensorErrorResponse response = new SensorErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        // В HTTP-ответе будет тело ответа (response) и статус в заголовке http-ответа
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
