package ru.sensor.springcourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sensor.springcourse.model.Measurement;

public interface MeasurementRepository extends JpaRepository<Measurement,Integer> {
}
