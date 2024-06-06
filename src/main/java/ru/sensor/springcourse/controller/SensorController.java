package ru.sensor.springcourse.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.sensor.springcourse.dto.MeasurementDTO;
import ru.sensor.springcourse.dto.SearchDTO;
import ru.sensor.springcourse.dto.SensorDTO;
import ru.sensor.springcourse.service.MeasurementService;
import ru.sensor.springcourse.service.SensorService;
import ru.sensor.springcourse.util.*;
import ru.sensor.springcourse.util.searchDTOException.SearchDTOErrorResponse;
import ru.sensor.springcourse.util.searchDTOException.SearchDTONotValidException;
import ru.sensor.springcourse.util.searchDTOException.SearchDTOValidator;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SensorController {

    SensorService sensorService;

    MeasurementService measurementService;

    SensorValidator sensorValidator;

    MeasurementValidator measurementValidator;

    SearchDTOValidator searchDTOValidator;

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

    //Пример работы с @RequestBody
    @PostMapping("/findMeasurements")
    public List<MeasurementDTO>getMeasurementsBetweenDates(@RequestBody @Valid SearchDTO searchDTO,
                                                           BindingResult bindingResult){
        searchDTOValidator.validate(searchDTO,bindingResult);

        System.out.println(searchDTO.getDateFrom() + " <- controller");

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errorList = bindingResult.getFieldErrors();
            errorList.stream().forEach(fieldError -> errorMsg
                    .append(fieldError.getField())
                    .append(" - ").append(fieldError.getDefaultMessage())
                    .append(";"));

            throw new SearchDTONotValidException(errorMsg.toString());
        }
        return measurementService.getMeasurementsBetweenDatesWithSearchDTO(searchDTO);
    }

    //Пример работы с @PathVariable
    @GetMapping("/findMeasurements/{dateFrom}/{dateTo}")
    public List<MeasurementDTO>getMeasurementsBetweenDates(@PathVariable(value = "dateFrom") String dateFrom,
                                                           @PathVariable(value = "dateTo") String dateTo){
        return measurementService.getMeasurementsBetweenDates(dateFrom,dateTo);
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

    @ExceptionHandler
    private ResponseEntity<SearchDTOErrorResponse> searchDTOHandlerException(SearchDTONotValidException e) {
        SearchDTOErrorResponse response = new SearchDTOErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
