package QuantityMeasurementApp.model;

import QuantityMeasurementApp.enums.IMeasurable;

import java.util.Objects;

public class Quantity<U extends IMeasurable> {
    private static final double EPSILON = 1e-6;
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");

        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Invalid value");

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    // Convert
    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double base = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(base);

        return new Quantity<>(converted, targetUnit);
    }

    // Add (same unit)
    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    // Add (target unit)
    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        if (other == null || targetUnit == null) {
            throw new IllegalArgumentException("Other quantity and target unit cannot be null");
        }

        double sumBase =
                this.unit.convertToBaseUnit(this.value) +
                        other.unit.convertToBaseUnit(other.value);

        double result = targetUnit.convertFromBaseUnit(sumBase);

        return new Quantity<>(result, targetUnit);
    }

    // ================= UC12: SUBTRACT =================

    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        if (other == null || targetUnit == null) {
            throw new IllegalArgumentException("Other quantity and target unit cannot be null");
        }
        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException("Cross-category operations not allowed");
        }

        double diffBase =
                this.unit.convertToBaseUnit(this.value) -
                        other.unit.convertToBaseUnit(other.value);

        double result = targetUnit.convertFromBaseUnit(diffBase);

        return new Quantity<>(
                Math.round(result * 100.0) / 100.0,
                targetUnit
        );
    }

    // ================= UC12: DIVIDE =================

    public double divide(Quantity<U> other) {
        if (other == null) {
            throw new IllegalArgumentException("Other quantity cannot be null");
        }
        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException("Cross-category operations not allowed");
        }

        double divisorBase = other.unit.convertToBaseUnit(other.value);

        if (Math.abs(divisorBase) < EPSILON) {
            throw new ArithmeticException("Division by zero");
        }

        double dividendBase = this.unit.convertToBaseUnit(this.value);

        return Math.round((dividendBase / divisorBase) * 100.0) / 100.0;
    }

    // Equals with cross-category safety
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Quantity<?> that = (Quantity<?>) obj;

        if (this.unit.getClass() != that.unit.getClass()) return false;

        double v1 = this.unit.convertToBaseUnit(this.value);
        double v2 = that.unit.convertToBaseUnit(that.value);

        return Math.abs(v1 - v2) < EPSILON;
    }

    @Override
    public int hashCode() {
        double base = unit.convertToBaseUnit(value);
        return Objects.hash(Math.round(base / EPSILON));
    }

    @Override
    public String toString() {
        return value + " " + unit.getUnitName();
    }

}