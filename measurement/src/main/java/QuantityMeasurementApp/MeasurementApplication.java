
import QuantityMeasurementApp.enumsimplm.LengthUnit;
import QuantityMeasurementApp.enumsimplm.WeightUnit;
import QuantityMeasurementApp.model.Quantity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MeasurementApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeasurementApplication.class, args);

        // LENGTH
        Quantity<LengthUnit> length1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> length2 = new Quantity<>(12.0, LengthUnit.INCH);

        boolean isEqualLength = length1.equals(length2);
        Quantity<LengthUnit> sumLength = length1.add(length2);
        Quantity<LengthUnit> convertedLength = length1.convertTo(LengthUnit.INCH);

        // WEIGHT
        Quantity<WeightUnit> weight1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> weight2 = new Quantity<>(1000.0, WeightUnit.GRAM);

        boolean isEqualWeight = weight1.equals(weight2);
        Quantity<WeightUnit> sumWeight = weight1.add(weight2);
        Quantity<WeightUnit> convertedWeight = weight1.convertTo(WeightUnit.GRAM);


    }
}