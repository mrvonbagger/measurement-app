package com.measurement_server.dto.measurements;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import java.time.LocalDateTime;

@Introspected
@Serdeable
public record MeasurementDTO(
                String uuid,
                Long patientId,
                Double result,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {
}