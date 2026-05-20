

package com.App.QuantityMeasurement.service;

import com.App.QuantityMeasurement.dto.QuantityDTO;
import com.App.QuantityMeasurement.model.QuantityMeasurementEntity;

import java.util.List;

public interface QuantityMeasurementService {

    // Arithmetic Operations
    QuantityDTO add(
            QuantityDTO first,
            QuantityDTO second
    );

    QuantityDTO subtract(
            QuantityDTO first,
            QuantityDTO second
    );

    QuantityDTO divide(
            QuantityDTO first,
            QuantityDTO second
    );

    // Comparison and Conversion
    QuantityDTO compareEquality(
            QuantityDTO first,
            QuantityDTO second
    );

    QuantityDTO convert(
            QuantityDTO source,
            String targetUnit
    );

    // History Methods
    List<QuantityMeasurementEntity> getAllMeasurements();

    List<QuantityMeasurementEntity> getHistoryByOperation(
            String operation
    );

    long getCountByOperation(
            String operation
    );
}