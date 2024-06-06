package ru.sensor.springcourse.util.searchDTOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.sensor.springcourse.dto.SearchDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class SearchDTOValidator implements Validator {

//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//    DateValidatorUsingLocalDate dateValidatorUsingLocalDate = new DateValidatorUsingLocalDate(formatter);

    @Override
    public boolean supports(Class<?> clazz) {
        return SearchDTO.class.equals(clazz);
    }

    @SneakyThrows
    @Override
    public void validate(Object target, Errors errors) {
        SearchDTO searchDTO = (SearchDTO) target;

//        if (!dateValidatorUsingLocalDate.isValid(searchDTO.getDateFrom().toString())){
//            errors.rejectValue("dateFrom", "", " Невалид");
//        }

        if (searchDTO.getDateFrom().isAfter(searchDTO.getDateTo()) || searchDTO.getDateFrom().equals(searchDTO.getDateTo())) {
            errors.rejectValue("dateFrom", "", " dateFrom должна быть до dateTo");
        }
    }

}
