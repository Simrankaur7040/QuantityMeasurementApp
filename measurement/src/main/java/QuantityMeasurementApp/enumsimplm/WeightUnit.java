package QuantityMeasurementApp.enumsimplm;

import QuantityMeasurementApp.enums.IMeasurable;

public enum WeightUnit implements IMeasurable {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(1.0/2.20462);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }


    public double getConversionFactor() {
        return conversionFactor;
    }


    public double convertToBaseUnit(double value) {
        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Invalid value");

        return value * conversionFactor;
    }


    public double convertFromBaseUnit(double value) {
        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Invalid value");

        return value / conversionFactor;
    }

    @Override
    public String getUnitName() {
        return this.name();
    }
}