package ru.sensor.springcourse.util;

public class MeasurementErrorResponse {

    private String errorMessage;

    private long timestamp;

    public MeasurementErrorResponse(String errorMessage, long timestamp) {
        this.errorMessage = errorMessage;
        this.timestamp = timestamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
