package com.App.QuantityMeasurement.model;

import com.App.QuantityMeasurement.enumsimplm.LengthUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuantityLength {

    private static final Logger logger =
            LoggerFactory.getLogger(QuantityLength.class);

    private final Quantity<LengthUnit> quantity;

    public QuantityLength(double value, LengthUnit unit) {

        if (!Double.isFinite(value)) {
            logger.error("Invalid length value: {}", value);
            throw new IllegalArgumentException("Invalid value");
        }

        logger.debug("Creating QuantityLength: {} {}", value, unit);

        this.quantity = new Quantity<>(value, unit);
    }

    public double getValue() {
        return quantity.getValue();
    }

    public LengthUnit getUnit() {
        return quantity.getUnit();
    }

    public QuantityLength add(
            QuantityLength other,
            LengthUnit targetUnit) {

        if (other == null || targetUnit == null) {
            logger.error("Other quantity or target unit is null");
            throw new IllegalArgumentException(
                    "Other quantity and target unit must not be null"
            );
        }

        Quantity<LengthUnit> result =
                this.quantity.add(
                        other.quantity,
                        targetUnit
                );

        logger.info("Added {} {} and {} {} = {} {}",
                this.getValue(),
                this.getUnit(),
                other.getValue(),
                other.getUnit(),
                result.getValue(),
                result.getUnit());

        return new QuantityLength(
                result.getValue(),
                result.getUnit()
        );
    }

    // ADD
    public QuantityLength add(QuantityLength other) {
        return add(other, this.getUnit());
    }

    // CONVERT
    public QuantityLength toConvert(
            LengthUnit targetUnit) {

        if (targetUnit == null) {
            logger.error("Target unit cannot be null");
            throw new IllegalArgumentException(
                    "Target unit must not be null"
            );
        }

        Quantity<LengthUnit> result =
                this.quantity.convertTo(targetUnit);

        logger.info("Converted {} {} to {} {}",
                this.getValue(),
                this.getUnit(),
                result.getValue(),
                result.getUnit());

        return new QuantityLength(
                result.getValue(),
                result.getUnit()
        );
    }

    // EQUALS
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            logger.debug("Comparing same QuantityLength instance");
            return true;
        }

        if (!(obj instanceof QuantityLength)) {
            logger.debug(
                    "Comparison failed: object is not QuantityLength"
            );
            return false;
        }

        QuantityLength other = (QuantityLength) obj;
        boolean result =
                this.quantity.equals(other.quantity);

        logger.debug("Equality check result: {}", result);

        return result;
    }

    @Override
    public int hashCode() {
        return quantity.hashCode();
    }
}