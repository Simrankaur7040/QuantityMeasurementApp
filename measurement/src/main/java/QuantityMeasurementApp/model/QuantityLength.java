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

    public double toFeet() {
        return unit.toFeet(value);
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


    private double toBaseUnit(){
        return  unit.toFeet(value);
    }

    public  QuantityLength add(QuantityLength other, LengthUnit targetUnit) {

        if (other == null || targetUnit == null ) {
            throw new IllegalArgumentException("Operands and target unit cannot be null");
        }
        if (!Double.isFinite(other.value)) {
            throw new IllegalArgumentException("Invalid numeric value");
        }
        double thisInFeet = this.toBaseUnit();
        double otherInFeet = other.toBaseUnit();

        double sumInFeet = thisInFeet + otherInFeet;
        double result = targetUnit.fromfeet(sumInFeet);

        return new QuantityLength(result, targetUnit);
    }
//overload version
    public QuantityLength add(QuantityLength other){
        return add(other,this.unit);
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