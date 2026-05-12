package QuantityMeasurementApp.serviceImpl;

import QuantityMeasurementApp.dto.QuantityDTO;
import QuantityMeasurementApp.entity.QuantityMeasurementEntity;
import QuantityMeasurementApp.enums.IMeasurable;
import QuantityMeasurementApp.enumsimplm.LengthUnit;
import QuantityMeasurementApp.enumsimplm.TemperatureUnit;
import QuantityMeasurementApp.enumsimplm.VolumeUnit;
import QuantityMeasurementApp.enumsimplm.WeightUnit;
import QuantityMeasurementApp.exception.QuantityMeasurementException;
import QuantityMeasurementApp.model.Quantity;
import QuantityMeasurementApp.repository.IQuantityMeasurementRepository;
import QuantityMeasurementApp.service.IQuantityMeasurementService;

public class QuantityMeasurementServiceImpl
        implements IQuantityMeasurementService {

    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(
            IQuantityMeasurementRepository repository) {

        this.repository = repository;
    }

    // =========================================================
    // COMPARE
    // =========================================================

    @Override
    public QuantityDTO compare(QuantityDTO q1, QuantityDTO q2) {

        try {

            Quantity quantity1 = buildQuantity(q1);

            Quantity quantity2 = buildQuantity(q2);
            if (!q1.getMeasurementType()
                    .equalsIgnoreCase(q2.getMeasurementType())) {

                throw new IllegalArgumentException(
                        "Cross-category comparison not allowed"
                );
            }
            boolean result = quantity1.equals(quantity2);

            QuantityDTO dto =
                    new QuantityDTO(
                            result ? 1 : 0,
                            "BOOLEAN",
                            "BOOLEAN"
                    );

            repository.save(
                    new QuantityMeasurementEntity(
                            "COMPARE",
                            String.valueOf(result)
                    )
            );

            return dto;

        } catch (Exception e) {

            repository.save(
                    new QuantityMeasurementEntity(
                            e.getMessage()
                    )
            );

            return QuantityDTO.error(e.getMessage());
        }
    }

    // =========================================================
    // CONVERT
    // =========================================================

    @Override
    public QuantityDTO convert(QuantityDTO q1, String targetUnit) {

        try {

            Quantity quantity =
                    buildQuantity(q1);

            IMeasurable unit =
                    (IMeasurable) parseUnit(
                            q1.getMeasurementType(),
                            targetUnit
                    );

            Quantity result =
                    quantity.convertTo(unit);

            QuantityDTO dto =
                    new QuantityDTO(
                            result.getValue(),
                            targetUnit,
                            q1.getMeasurementType()
                    );

            repository.save(
                    new QuantityMeasurementEntity(
                            "CONVERT",
                            dto.toString()
                    )
            );

            return dto;

        } catch (Exception e) {

            repository.save(
                    new QuantityMeasurementEntity(
                            e.getMessage()
                    )
            );

            return QuantityDTO.error(e.getMessage());
        }
    }

    // =========================================================
    // ADD
    // =========================================================

    @Override
    public QuantityDTO add(
            QuantityDTO q1,
            QuantityDTO q2,
            String targetUnit) {

        try {

            Quantity quantity1 =
                    buildQuantity(q1);

            Quantity quantity2 =
                    buildQuantity(q2);

            IMeasurable target =
                    (IMeasurable) parseUnit(
                            q1.getMeasurementType(),
                            targetUnit
                    );

            Quantity result =
                    quantity1.add(quantity2, target);

            QuantityDTO dto =
                    new QuantityDTO(
                            result.getValue(),
                            targetUnit,
                            q1.getMeasurementType()
                    );

            repository.save(
                    new QuantityMeasurementEntity(
                            "ADD",
                            dto.toString()
                    )
            );

            return dto;

        } catch (Exception e) {

            repository.save(
                    new QuantityMeasurementEntity(
                            e.getMessage()
                    )
            );

            return QuantityDTO.error(e.getMessage());
        }
    }

    // =========================================================
    // SUBTRACT
    // =========================================================

    @Override
    public QuantityDTO subtract(
            QuantityDTO q1,
            QuantityDTO q2,
            String targetUnit) {

        try {

            Quantity quantity1 =
                    buildQuantity(q1);

            Quantity quantity2 =
                    buildQuantity(q2);

            IMeasurable target =
                    (IMeasurable) parseUnit(
                            q1.getMeasurementType(),
                            targetUnit
                    );

            Quantity result =
                    quantity1.subtract(quantity2, target);

            QuantityDTO dto =
                    new QuantityDTO(
                            result.getValue(),
                            targetUnit,
                            q1.getMeasurementType()
                    );

            repository.save(
                    new QuantityMeasurementEntity(
                            "SUBTRACT",
                            dto.toString()
                    )
            );

            return dto;

        } catch (Exception e) {

            repository.save(
                    new QuantityMeasurementEntity(
                            e.getMessage()
                    )
            );

            return QuantityDTO.error(e.getMessage());
        }
    }

    // =========================================================
    // DIVIDE
    // =========================================================

    @Override
    public QuantityDTO divide(QuantityDTO q1, QuantityDTO q2) {

        try {

            Quantity quantity1 =
                    buildQuantity(q1);

            Quantity quantity2 =
                    buildQuantity(q2);

            double result =
                    quantity1.divide(quantity2);

            QuantityDTO dto =
                    new QuantityDTO(
                            result,
                            "SCALAR",
                            "SCALAR"
                    );

            repository.save(
                    new QuantityMeasurementEntity(
                            "DIVIDE",
                            dto.toString()
                    )
            );

            return dto;

        } catch (Exception e) {

            repository.save(
                    new QuantityMeasurementEntity(
                            e.getMessage()
                    )
            );

            return QuantityDTO.error(e.getMessage());
        }
    }

    // =========================================================
    // BUILD QUANTITY
    // =========================================================

    private Quantity buildQuantity(QuantityDTO dto) {

        if (dto == null)
            throw new IllegalArgumentException(
                    "QuantityDTO cannot be null"
            );

        IMeasurable unit =
                (IMeasurable) parseUnit(
                        dto.getMeasurementType(),
                        dto.getUnit()
                );

        return new Quantity(
                dto.getValue(),
                unit
        );
    }

    // =========================================================
    // PARSE UNIT
    // =========================================================

    private Object parseUnit(
            String measurementType,
            String unitName) {

        switch (measurementType.toUpperCase()) {

            case "LENGTH":
                return LengthUnit.valueOf(unitName);

            case "WEIGHT":
                return WeightUnit.valueOf(unitName);

            case "VOLUME":
                return VolumeUnit.valueOf(unitName);

            case "TEMPERATURE":
                return TemperatureUnit.valueOf(unitName);

            default:
                throw new QuantityMeasurementException(
                        "Unknown measurement type"
                );
        }
    }
}