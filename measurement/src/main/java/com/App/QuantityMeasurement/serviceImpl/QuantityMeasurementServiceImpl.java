package com.App.QuantityMeasurement.serviceImpl;

import com.App.QuantityMeasurement.dto.QuantityDTO;
import com.App.QuantityMeasurement.enums.IMeasurable;
import com.App.QuantityMeasurement.enumsimplm.LengthUnit;
import com.App.QuantityMeasurement.enumsimplm.TemperatureUnit;
import com.App.QuantityMeasurement.enumsimplm.VolumeUnit;
import com.App.QuantityMeasurement.enumsimplm.WeightUnit;
import com.App.QuantityMeasurement.model.Quantity;
import com.App.QuantityMeasurement.model.QuantityMeasurementEntity;
import com.App.QuantityMeasurement.repository.QuantityMeasurementRepository;
import com.App.QuantityMeasurement.service.QuantityMeasurementService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityMeasurementServiceImpl
        implements QuantityMeasurementService {

    private final QuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(
            QuantityMeasurementRepository repository) {

        this.repository = repository;
    }

    // ==========================================
    // Helper Method
    // ==========================================
    private Quantity<? extends IMeasurable>
    createQuantity(QuantityDTO dto) {

        String measurementType =
                dto.getMeasurementType();

        String unitName =
                dto.getUnit().toUpperCase();

        switch (measurementType) {

            case "LengthUnit":
                return new Quantity<>(
                        dto.getValue(),
                        LengthUnit.valueOf(unitName)
                );

            case "TemperatureUnit":
                return new Quantity<>(
                        dto.getValue(),
                        TemperatureUnit.valueOf(unitName)
                );

            case "WeightUnit":
                return new Quantity<>(
                        dto.getValue(),
                        WeightUnit.valueOf(unitName)
                );

            case "VolumeUnit":
                return new Quantity<>(
                        dto.getValue(),
                        VolumeUnit.valueOf(unitName)
                );

            default:
                throw new IllegalArgumentException(
                        "Unsupported measurement type: "
                                + measurementType
                );
        }
    }

    // ==========================================
    // ADD
    // ==========================================
    @Override
    @SuppressWarnings("unchecked")
    public QuantityDTO add(
            QuantityDTO first,
            QuantityDTO second) {

        Quantity<IMeasurable> q1 =
                (Quantity<IMeasurable>)
                        createQuantity(first);

        Quantity<IMeasurable> q2 =
                (Quantity<IMeasurable>)
                        createQuantity(second);

        Quantity<IMeasurable> result =
                q1.add(q2);

        return new QuantityDTO(
                result.getValue(),
                ((Enum<?>) result.getUnit()).name(),
                result.getUnit()
                        .getClass()
                        .getSimpleName()
        );
    }

    // ==========================================
    // SUBTRACT
    // ==========================================
    @Override
    @SuppressWarnings("unchecked")
    public QuantityDTO subtract(
            QuantityDTO first,
            QuantityDTO second) {

        Quantity<IMeasurable> q1 =
                (Quantity<IMeasurable>)
                        createQuantity(first);

        Quantity<IMeasurable> q2 =
                (Quantity<IMeasurable>)
                        createQuantity(second);

        Quantity<IMeasurable> result =
                q1.subtract(q2);

        return new QuantityDTO(
                result.getValue(),
                ((Enum<?>) result.getUnit()).name(),
                result.getUnit()
                        .getClass()
                        .getSimpleName()
        );
    }

    // ==========================================
    // DIVIDE
    // ==========================================
    @Override
    @SuppressWarnings("unchecked")
    public QuantityDTO divide(
            QuantityDTO first,
            QuantityDTO second) {

        Quantity<IMeasurable> q1 =
                (Quantity<IMeasurable>)
                        createQuantity(first);

        Quantity<IMeasurable> q2 =
                (Quantity<IMeasurable>)
                        createQuantity(second);

        double result = q1.divide(q2);

        return new QuantityDTO(
                result,
                "NUMBER",
                "Arithmetic"
        );
    }
    @Override
    @SuppressWarnings("unchecked")
    public QuantityDTO addAndConvert(
            QuantityDTO first,
            QuantityDTO second,
            String targetUnit) {

        Quantity<IMeasurable> q1 =
                (Quantity<IMeasurable>)
                        createQuantity(first);

        Quantity<IMeasurable> q2 =
                (Quantity<IMeasurable>)
                        createQuantity(second);

        QuantityDTO targetDto =
                new QuantityDTO(
                        0.0,
                        targetUnit,
                        first.getMeasurementType()
                );

        Quantity<IMeasurable> targetQuantity =
                (Quantity<IMeasurable>)
                        createQuantity(targetDto);

        Quantity<IMeasurable> result =
                q1.add(
                        q2,
                        targetQuantity.getUnit()
                );

        return new QuantityDTO(
                result.getValue(),
                ((Enum<?>) result.getUnit()).name(),
                result.getUnit()
                        .getClass()
                        .getSimpleName()
        );
    }

    // ==========================================
    // COMPARE
    // ==========================================
    @Override
    @SuppressWarnings("unchecked")
    public QuantityDTO compareEquality(
            QuantityDTO first,
            QuantityDTO second) {

        Quantity<IMeasurable> q1 =
                (Quantity<IMeasurable>)
                        createQuantity(first);

        Quantity<IMeasurable> q2 =
                (Quantity<IMeasurable>)
                        createQuantity(second);

        boolean isEqual = q1.equals(q2);

        return new QuantityDTO(
                isEqual ? 0.0 : 1.0,
                "BOOLEAN",
                "Comparison"
        );
    }

    // ==========================================
    // CONVERT
    // ==========================================
    @Override
    @SuppressWarnings("unchecked")
    public QuantityDTO convert(
            QuantityDTO source,
            String targetUnit) {

        Quantity<IMeasurable> quantity =
                (Quantity<IMeasurable>)
                        createQuantity(source);

        QuantityDTO targetDto =
                new QuantityDTO(
                        0.0,
                        targetUnit,
                        source.getMeasurementType()
                );

        Quantity<IMeasurable> targetQuantity =
                (Quantity<IMeasurable>)
                        createQuantity(targetDto);

        Quantity<IMeasurable> converted =
                quantity.convertTo(
                        targetQuantity.getUnit()
                );

        return new QuantityDTO(
                converted.getValue(),
                ((Enum<?>) converted.getUnit()).name(),
                converted.getUnit()
                        .getClass()
                        .getSimpleName()
        );
    }

    // ==========================================
    // HISTORY
    // ==========================================
    @Override
    public List<QuantityMeasurementEntity>
    getAllMeasurements() {

        return repository.findAll();
    }

    @Override
    public List<QuantityMeasurementEntity>
    getHistoryByOperation(
            String operation) {

        return repository.findAll();
    }

    @Override
    public long getCountByOperation(
            String operation) {

        return repository.findAll().size();
    }
}