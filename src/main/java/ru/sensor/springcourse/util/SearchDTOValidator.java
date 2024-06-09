package ru.sensor.springcourse.util;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.sensor.springcourse.dto.SearchDTO;

@Component
public class SearchDTOValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SearchDTO.class.equals(clazz);
    }

    @SneakyThrows
    @Override
    public void validate(Object target, Errors errors) {
        SearchDTO searchDTO = (SearchDTO) target;
        if (searchDTO.getDateFrom().isAfter(searchDTO.getDateTo()) || searchDTO.getDateFrom().equals(searchDTO.getDateTo())) {
            errors.rejectValue("dateFrom", "", " dateFrom должна быть до dateTo");
        }
    }

}
