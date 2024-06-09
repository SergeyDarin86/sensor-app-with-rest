package ru.sensor.springcourse.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sensor.springcourse.dto.MeasurementDTO;
import ru.sensor.springcourse.dto.SearchDTO;
import ru.sensor.springcourse.service.MeasurementService;
import ru.sensor.springcourse.util.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MeasurementController {

    MeasurementService measurementService;

    MeasurementValidator measurementValidator;

    SearchDTOValidator searchDTOValidator;

    @GetMapping("/measurements")
    public MeasurementResponse getAllMeasurement() {
        return measurementService.getAllMeasurements();
    }

    //Пример работы с @RequestBody
    @PostMapping("/findMeasurements")
    public MeasurementResponse getMeasurementsBetweenDates(@RequestBody @Valid SearchDTO searchDTO,
                                                           BindingResult bindingResult) {
        searchDTOValidator.validate(searchDTO, bindingResult);

        ExceptionBuilder.buildErrorMessageForClient(bindingResult);

        return measurementService.getMeasurementsBetweenDatesWithSearchDTO(searchDTO);
    }

    //Пример работы с @PathVariable
    @GetMapping("/findMeasurements/{dateFrom}/{dateTo}")
    public List<MeasurementDTO> getMeasurementsBetweenDates(@PathVariable(value = "dateFrom") String dateFrom,
                                                            @PathVariable(value = "dateTo") String dateTo) {
        return measurementService.getMeasurementsBetweenDates(dateFrom, dateTo);
    }

    @PostMapping("/measurements/add")
    public ResponseEntity<HttpStatus> createMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                        BindingResult bindingResult) {
        measurementValidator.validate(measurementDTO, bindingResult);

        ExceptionBuilder.buildErrorMessageForClient(bindingResult);

        measurementService.createMeasurement(measurementDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/measurements/getRainyDaysCount")
    public String getRainyDaysCount() {
        return "The count of rainy days: " + measurementService.getRainyDaysCount(true);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> measurementHandlerException(MeasurementException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        // В HTTP-ответе будет тело ответа (response) и статус в заголовке http-ответа
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
