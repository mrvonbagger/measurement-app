package com.measurement_server.controller;

import com.measurement_server.domain.Measurement;
import com.measurement_server.repository.MeasurementRepository;
import io.micronaut.http.annotation.*;

import java.util.List;

@Controller("/api/measurements")
public class MeasurementController {

    private final MeasurementRepository repository;

    public MeasurementController(MeasurementRepository repository) {
        this.repository = repository;
    }

    @Get("/")
    public List<Measurement> list() {
        return (List<Measurement>) repository.findAll();
    }

    @Post("/")
    public Measurement create(@Body Measurement measurement) {
        return repository.save(measurement);
    }

    @Get("/{id}")
    public Measurement getById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @Delete("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
