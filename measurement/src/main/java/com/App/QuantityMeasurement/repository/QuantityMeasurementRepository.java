
package com.App.QuantityMeasurement.repository;

import com.App.QuantityMeasurement.model.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuantityMeasurementRepository
        extends JpaRepository<QuantityMeasurementEntity, Long> {

    // Find all records for a given operation
    List<QuantityMeasurementEntity> findByOperation(
            String operation
    );
}