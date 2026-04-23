package QuantityMeasurementApp.model;
import QuantityMeasurementApp.enumsimplm.LengthUnit;

public class QuantityLength {

    private final Quantity<LengthUnit> quantity;

    public QuantityLength(double value, LengthUnit unit) {
        this.quantity = new Quantity<>(value, unit);
    }

    public double getValue() {
        return quantity.getValue();
    }

    public LengthUnit getUnit() {
        return quantity.getUnit();
    }

    public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {

        if (other == null || targetUnit == null) {
            throw new IllegalArgumentException("Other quantity and target unit must not be null");
        }

        Quantity<LengthUnit> result =this.quantity.add(other.quantity, targetUnit);

        return new QuantityLength(result.getValue(), result.getUnit());
    }

    // ADD
    public QuantityLength add(QuantityLength other) {
        return add(other, this.getUnit());
    }

    // CONVERT
    public QuantityLength toConvert(LengthUnit targetUnit) {
        Quantity<LengthUnit> result =this.quantity.convertTo(targetUnit);
        return new QuantityLength(result.getValue(), result.getUnit());
    }

    // EQUALS
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuantityLength)) return false;
        QuantityLength other = (QuantityLength) obj;
        return this.quantity.equals(other.quantity);
    }

    @Override
    public int hashCode() {
        return quantity.hashCode();
    }
}