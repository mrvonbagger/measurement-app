package com.measurement_server.dto.measurements;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Introspected
@Serdeable

public record CreateMeasurementRequest(
                @NotNull(message = "Patient ID is required") Long patientId,

                @NotNull(message = "Result is required") @DecimalMin(value = "50.0", message = "Measurement result must be at least 50.0") @DecimalMax(value = "100.0", message = "Measurement result must be at most 100.0") Double result) {
}