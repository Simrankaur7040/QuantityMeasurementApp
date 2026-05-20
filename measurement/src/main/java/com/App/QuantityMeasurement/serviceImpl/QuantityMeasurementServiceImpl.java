package com.App.QuantityMeasurement.serviceImpl;
import com.App.QuantityMeasurement.dto.QuantityDTO;
import com.App.QuantityMeasurement.model.QuantityMeasurementEntity;
import com.App.QuantityMeasurement.repository.QuantityMeasurementRepository;
import com.App.QuantityMeasurement.service.QuantityMeasurementService;
import org.springframework.stereotype.Service;
@Service
public class QuantityMeasurementServiceImpl
        implements QuantityMeasurementService {
    private final QuantityMeasurementRepository repository;
    public QuantityMeasurementServiceImpl(QuantityMeasurementRepository
                                                  repository) {
        this.repository = repository;
    }
    @Override
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {
// Replace with your UC16 logic
        QuantityDTO result = new QuantityDTO(
                q1.getValue() + q2.getValue(),
                q1.getUnit(),
                q1.getMeasurementType()
        );
        repository.save(new QuantityMeasurementEntity(
                null,
                "ADD",
                q1.toString() + ", " + q2.toString(),
                result.toString(),
                false
        ));
        return result;

    }
    @Override
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2) {
        return null; // Paste UC16 logic
    }
    @Override
    public QuantityDTO divide(QuantityDTO q1, QuantityDTO q2) {
        return null;
    }
    @Override
    public QuantityDTO convert(QuantityDTO input, String targetUnit) {
        return null;
    }
    @Override
    public QuantityDTO compareEquality(QuantityDTO q1, QuantityDTO q2) {
        return null;
    }
}
