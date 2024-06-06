package ru.sensor.springcourse.util.searchDTOException;

public class SearchDTONotValidException extends RuntimeException{
    public SearchDTONotValidException(String errorMsg){
        super(errorMsg);
    }
}
