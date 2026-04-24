package QuantityMeasurementApp.enumsimplm;

import QuantityMeasurementApp.enums.IMeasurable;

public enum VolumeUnit implements IMeasurable {

    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double factor;

    VolumeUnit(double factor) {
        this.factor = factor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Invalid value");

        return value * factor;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        if (!Double.isFinite(baseValue))
            throw new IllegalArgumentException("Invalid value");

        return baseValue / factor;
    }

    @Override
    public double getConversionFactor() {
        return factor;
    }

    @Override
    public String getUnitName() {
        return this.name();
    }
}