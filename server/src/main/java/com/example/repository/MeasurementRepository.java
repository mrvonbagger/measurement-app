package com.example.repository;

import com.example.domain.Measurement;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    // Find all non-deleted measurements
    @Query("SELECT m FROM Measurement m WHERE m.isDeleted = false ORDER BY m.createdAt DESC")
    List<Measurement> findAllActive();

    // Find measurement by UUID (non-deleted only)
    @Query("SELECT m FROM Measurement m WHERE m.uuid = :uuid AND m.isDeleted = false")
    Optional<Measurement> findByUuid(String uuid);

    // Find measurements by patient ID (non-deleted only)
    @Query("SELECT m FROM Measurement m WHERE m.patientId = :patientId AND m.isDeleted = false ORDER BY m.createdAt DESC")
    List<Measurement> findByPatientId(Long patientId);

    // Check if UUID exists (including deleted records)
    @Query("SELECT COUNT(m) > 0 FROM Measurement m WHERE m.uuid = :uuid")
    boolean existsByUuid(String uuid);

    // // Find by UUID (including deleted records) - for internal use
    // Optional<Measurement> findByUuidIncludingDeleted(String uuid);

    // @Query("SELECT m FROM Measurement m WHERE m.uuid = :uuid")
    // Optional<Measurement> findByUuidIncludingDeleted(String uuid);
}