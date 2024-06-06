package ru.sensor.springcourse.util.searchDTOException;

import lombok.Data;

@Data
public class SearchDTOErrorResponse {

    private String errorMessage;

    private long timestamp;

    public SearchDTOErrorResponse(String errorMessage, long timestamp) {
        this.errorMessage = errorMessage;
        this.timestamp = timestamp;
    }
}
