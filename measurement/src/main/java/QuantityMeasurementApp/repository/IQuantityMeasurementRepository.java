package QuantityMeasurementApp.repository;
import QuantityMeasurementApp.entity.QuantityMeasurementEntity;
import java.util.List;
public interface IQuantityMeasurementRepository {
    void save(QuantityMeasurementEntity entity);
    List<QuantityMeasurementEntity> findAll();
}
