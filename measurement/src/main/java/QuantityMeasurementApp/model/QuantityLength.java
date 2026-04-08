package QuantityMeasurementApp.model;
import QuantityMeasurementApp.enums.LengthUnit;
public class QuantityLength{

    private static final double epsilon = 1e-6;
    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        if(unit == null) throw new IllegalArgumentException("Unit shouldn't be null");
        this.value = value;
        this.unit = unit;
    }

    public double toconvert (LengthUnit targetUnit){
        return convert(this.value,this.unit,targetUnit);
    }

    public static double convert(double value,LengthUnit sourceUnit,LengthUnit targetUnit){
        if(sourceUnit==null || targetUnit==null){
            throw new IllegalArgumentException("Unit should't be empty");
        }
        if(!Double.isFinite(value)){
            throw new IllegalArgumentException("Invaild numeric value");
        }
        double valueInFeet= sourceUnit.toFeet(value);
        return targetUnit.fromfeet(valueInFeet);
    }


    public double getValue() {
        return value;
    }

//    overloadMethod
    QuantityLength add(QuantityLength Q1,QuantityLength Q2)
    {
        return  Q1.add(Q2);
    }

    public QuantityLength add(QuantityLength other) {
        if (other == null) {
            throw new IllegalArgumentException("Second operand cannot be null");
        }
        // Convert both to base (feet)
        double thisInFeet = this.unit.toFeet(this.value);
        double otherInFeet = other.unit.toFeet(other.getValue());
        // Add
        double sumInFeet = thisInFeet + otherInFeet;
        // Convert back to unit of first operand
        double resultValue = this.unit.fromfeet(sumInFeet);
        return new QuantityLength(resultValue, this.unit);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }


        QuantityLength other = (QuantityLength) obj;
        double thisInFeet = this.unit.toFeet(this.value);
        double otherInFeet = other.unit.toFeet(other.value);

        return Math.abs(thisInFeet - otherInFeet) < epsilon;
    }
}