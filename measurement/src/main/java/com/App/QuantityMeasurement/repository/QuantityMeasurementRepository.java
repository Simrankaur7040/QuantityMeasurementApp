package com.App.QuantityMeasurement.repository;
import com.App.QuantityMeasurement.entity.QuantityMeasurementEntity;
import java.util.List;
public interface QuantityMeasurementRepository {
    void save(QuantityMeasurementEntity entity);
    List<QuantityMeasurementEntity> getAllMeasurements();
    void deleteAll();
    int getTotalCount();
}