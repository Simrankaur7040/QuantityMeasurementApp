// VolumeUnit.java
package com.App.QuantityMeasurement.enumsimplm;

import com.App.QuantityMeasurement.enums.IMeasurable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum VolumeUnit implements IMeasurable {

    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private static final Logger logger =
            LoggerFactory.getLogger(VolumeUnit.class);

    private final double factor;

    VolumeUnit(double factor) {
        this.factor = factor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        if (!Double.isFinite(value)) {
            logger.error("Invalid volume value: {}", value);
            throw new IllegalArgumentException("Invalid value");
        }

        double result = value * factor;
        logger.debug("Converted {} {} to {} LITRE",
                value, this.name(), result);
        return result;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        if (!Double.isFinite(baseValue)) {
            logger.error("Invalid base volume value: {}", baseValue);
            throw new IllegalArgumentException("Invalid value");
        }

        double result = baseValue / factor;
        logger.debug("Converted {} LITRE to {} {}",
                baseValue, result, this.name());
        return result;
    }
    public double getConversionFactor() {
        return factor;
    }

    @Override
    public String getUnitName() {
        return this.name();
    }
}