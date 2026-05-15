// TemperatureUnit.java
package com.App.QuantityMeasurement.enumsimplm;

import com.App.QuantityMeasurement.enums.IMeasurable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public enum TemperatureUnit implements IMeasurable {

    CELSIUS(
            c -> c,
            c -> c
    ),
    FAHRENHEIT(
            f -> (f - 32) * 5.0 / 9.0,
            c -> (c * 9.0 / 5.0) + 32
    ),
    KELVIN(
            k -> k - 273.15,
            c -> c + 273.15
    );

    private static final Logger logger =
            LoggerFactory.getLogger(TemperatureUnit.class);

    private final Function<Double, Double> toBase;
    private final Function<Double, Double> fromBase;

    TemperatureUnit(Function<Double, Double> toBase,
                    Function<Double, Double> fromBase) {
        this.toBase = toBase;
        this.fromBase = fromBase;
    }

    @Override
    public double convertToBaseUnit(double value) {
        double result = toBase.apply(value);
        logger.debug("Converted {} {} to {} CELSIUS",
                value, this.name(), result);
        return result;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        double result = fromBase.apply(baseValue);
        logger.debug("Converted {} CELSIUS to {} {}",
                baseValue, result, this.name());
        return result;
    }

    @Override
    public SupportsArithmetic supportsArithmetic() {
        return () -> false;
    }

    @Override
    public void validateOperationSupport(String operation) {
        logger.warn("Unsupported temperature operation attempted: {}", operation);
        throw new UnsupportedOperationException(
                "Operation '" + operation +
                        "' is not supported for Temperature measurements."
        );
    }
}