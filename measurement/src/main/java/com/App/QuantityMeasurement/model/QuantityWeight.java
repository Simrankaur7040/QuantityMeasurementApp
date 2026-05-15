package com.App.QuantityMeasurement.model;
import com.App.QuantityMeasurement.enumsimplm.WeightUnit;

public class QuantityWeight {

    private final Quantity<WeightUnit> quantity;

    public QuantityWeight(double value, WeightUnit unit) {
        this.quantity = new Quantity<>(value, unit);
    }

    public double getValue() {
        return quantity.getValue();
    }

    public WeightUnit getUnit() {
        return quantity.getUnit();
    }

    // CONVERT
    public QuantityWeight convertTo(WeightUnit targetUnit) {
        Quantity<WeightUnit> result = quantity.convertTo(targetUnit);
        return new QuantityWeight(result.getValue(), result.getUnit());
    }

    // ADD
    public QuantityWeight add(QuantityWeight other) {
        return add(other, this.getUnit());
    }

    // ADD with target unit
    public QuantityWeight add(QuantityWeight other, WeightUnit targetUnit) {
        Quantity<WeightUnit> result =this.quantity.add(other.quantity, targetUnit);
        return new QuantityWeight(result.getValue(), result.getUnit());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuantityWeight)) return false;

        QuantityWeight other = (QuantityWeight) obj;
        return this.quantity.equals(other.quantity);
    }

    @Override
    public int hashCode() {
        return quantity.hashCode();
    }

    @Override
    public String toString() {
        return quantity.toString();
    }
}