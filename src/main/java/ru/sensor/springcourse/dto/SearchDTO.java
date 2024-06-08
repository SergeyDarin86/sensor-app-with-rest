package ru.sensor.springcourse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

//@Data
public class SearchDTO implements Serializable {

    @Past(message = "Дата должна быть в прошлом")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @PastOrPresent(message = "Дата должна быть в настоящем или прошлом")
    private LocalDate dateTo;

    public LocalDate getDateFrom() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = null;
        try {
            localDate = LocalDate.parse(this.dateFrom.toString(),formatter);
        }catch (DateTimeParseException e){
            System.out.println("Невалид");
        }

        return localDate;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }
}
