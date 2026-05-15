package com.App.QuantityMeasurement.enumsimplm;

import com.App.QuantityMeasurement.enums.IMeasurable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum LengthUnit implements IMeasurable {

    FEET(1.0),
    INCH(1.0 / 12),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private static final Logger logger =
            LoggerFactory.getLogger(LengthUnit.class);

    private final double toFeetFactor;

    LengthUnit(double toFeetFactor) {
        this.toFeetFactor = toFeetFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        if (!Double.isFinite(value)) {
            logger.error("Invalid length value: {}", value);
            throw new IllegalArgumentException("Invalid value");
        }

        double result = value * toFeetFactor;
        logger.debug("Converted {} {} to {} FEET",
                value, this.name(), result);
        return result;
    }

    @Override
    public double convertFromBaseUnit(double value) {
        if (!Double.isFinite(value)) {
            logger.error("Invalid base length value: {}", value);
            throw new IllegalArgumentException("Invalid value");
        }

        double result = value / toFeetFactor;
        logger.debug("Converted {} FEET to {} {}",
                value, result, this.name());
        return result;
    }
    public double getConversionFactor() {
        return toFeetFactor;
    }

    @Override
    public String getUnitName() {
        return this.name();
    }
}