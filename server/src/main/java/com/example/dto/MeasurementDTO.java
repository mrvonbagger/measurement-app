package com.example.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Introspected
@Serdeable
public class MeasurementDTO {
    private String uuid;
    private Long patientId;
    private Double result;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public MeasurementDTO() {
    }

    public MeasurementDTO(String uuid, Long patientId, Double result, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.uuid = uuid;
        this.patientId = patientId;
        this.result = result;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

@Introspected
@Serdeable
class CreateMeasurementRequest {
    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Result is required")
    @DecimalMin(value = "50.0", message = "Measurement result must be at least 50.0")
    @DecimalMax(value = "100.0", message = "Measurement result must be at most 100.0")
    private Double result;

    // Constructors
    public CreateMeasurementRequest() {
    }

    public CreateMeasurementRequest(Long patientId, Double result) {
        this.patientId = patientId;
        this.result = result;
    }

    // Getters and Setters
    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }
}

@Introspected
@Serdeable
class UpdateMeasurementRequest {
    @DecimalMin(value = "50.0", message = "Measurement result must be at least 50.0")
    @DecimalMax(value = "100.0", message = "Measurement result must be at most 100.0")
    private Double result;

    // Constructors
    public UpdateMeasurementRequest() {
    }

    public UpdateMeasurementRequest(Double result) {
        this.result = result;
    }

    // Getters and Setters
    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }
}