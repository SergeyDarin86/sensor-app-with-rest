package ru.sensor.springcourse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchDTO {

    @Past(message = "Дата должна быть в прошлом")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @PastOrPresent(message = "Дата должна быть в настоящем или прошлом")
    private LocalDate dateTo;
}
