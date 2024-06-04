package ru.sensor.springcourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sensor.springcourse.model.Measurement;

import java.time.LocalDateTime;
import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement,Integer> {

    int countAllByRainingIs(Boolean raining);

    List<Measurement>findMeasurementByMeasurementDateBetween(LocalDateTime dateFrom, LocalDateTime dateTo);

}
