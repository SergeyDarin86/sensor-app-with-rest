package ru.sensor.springcourse.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import ru.sensor.springcourse.model.Sensor;

import java.time.LocalDateTime;

@Component
public class MeasurementDTO {

    @NotNull(message = "Поле raining должно быть заполнено")
    private Boolean raining;

    @NotNull(message = "Поле value должно быть заполнено")
    @Min(value = -100, message = "Минимальное значение не может быть ниже -100")
    @Max(value = 100, message = "Максимальное значение должно быть не больше 100")
    private Float value;

    @NotNull(message = "Поле sensor должно быть заполнено")
    private Sensor sensor;

    private LocalDateTime measurementDate;

//    @JsonIgnore
    public LocalDateTime getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(LocalDateTime measurementDate) {
        this.measurementDate = measurementDate;
    }

    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public String toString() {
        return "MeasurementDTO{" +
                "raining=" + raining +
                ", value=" + value +
                ", sensor=" + sensor.getName() +
                '}';
    }
}
