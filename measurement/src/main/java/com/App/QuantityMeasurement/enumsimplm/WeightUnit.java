// WeightUnit.java
package com.App.QuantityMeasurement.enumsimplm;

import com.App.QuantityMeasurement.enums.IMeasurable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum WeightUnit implements IMeasurable {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(1.0 / 2.20462);

    private static final Logger logger =
            LoggerFactory.getLogger(WeightUnit.class);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        if (!Double.isFinite(value)) {
            logger.error("Invalid weight value: {}", value);
            throw new IllegalArgumentException("Invalid value");
        }

        double result = value * conversionFactor;
        logger.debug("Converted {} {} to {} KILOGRAM",
                value, this.name(), result);
        return result;
    }

    @Override
    public double convertFromBaseUnit(double value) {
        if (!Double.isFinite(value)) {
            logger.error("Invalid base weight value: {}", value);
            throw new IllegalArgumentException("Invalid value");
        }

        double result = value / conversionFactor;
        logger.debug("Converted {} KILOGRAM to {} {}",
                value, result, this.name());
        return result;
    }
    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public String getUnitName() {
        return this.name();
    }
}