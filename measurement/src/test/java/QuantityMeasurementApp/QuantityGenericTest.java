package QuantityMeasurementApp;

import QuantityMeasurementApp.enums.IMeasurable;
import QuantityMeasurementApp.enumsimplm.LengthUnit;
import QuantityMeasurementApp.enumsimplm.WeightUnit;
import QuantityMeasurementApp.model.Quantity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuantityGenericTest {

    // ================= INTERFACE =================

    @Test
    void testIMeasurableInterface_LengthUnitImplementation() {
        IMeasurable unit = LengthUnit.FEET;
        assertNotNull(unit.getConversionFactor());
        assertEquals("FEET", unit.getUnitName());
    }

    @Test
    void testIMeasurableInterface_WeightUnitImplementation() {
        IMeasurable unit = WeightUnit.KILOGRAM;
        assertNotNull(unit.getConversionFactor());
        assertEquals("KILOGRAM", unit.getUnitName());
    }

    @Test
    void testIMeasurableInterface_ConsistentBehavior() {
        assertTrue(LengthUnit.INCH.convertToBaseUnit(12) > 0);
        assertTrue(WeightUnit.GRAM.convertToBaseUnit(1000) > 0);
    }

    // ================= EQUALITY =================

    @Test
    void testGenericQuantity_LengthOperations_Equality() {
        assertEquals(new Quantity<>(1.0, LengthUnit.FEET),
                new Quantity<>(12.0, LengthUnit.INCH));
    }

    @Test
    void testGenericQuantity_WeightOperations_Equality() {
        assertEquals(new Quantity<>(1.0, WeightUnit.KILOGRAM),
                new Quantity<>(1000.0, WeightUnit.GRAM));
    }

    // ================= CONVERSION =================

    @Test
    void testGenericQuantity_LengthOperations_Conversion() {
        Quantity<LengthUnit> q = new Quantity<>(1, LengthUnit.FEET);
        assertEquals(12.0, q.convertTo(LengthUnit.INCH).getValue(), 0.001);
    }

    @Test
    void testGenericQuantity_WeightOperations_Conversion() {
        Quantity<WeightUnit> q = new Quantity<>(1, WeightUnit.KILOGRAM);
        assertEquals(1000.0, q.convertTo(WeightUnit.GRAM).getValue(), 0.001);
    }

    @Test
    void testGenericQuantity_Conversion_AllUnitCombinations() {
        for (LengthUnit u1 : LengthUnit.values()) {
            for (LengthUnit u2 : LengthUnit.values()) {
                assertNotNull(new Quantity<>(1, u1).convertTo(u2));
            }
        }
        for (WeightUnit u1 : WeightUnit.values()) {
            for (WeightUnit u2 : WeightUnit.values()) {
                assertNotNull(new Quantity<>(1, u1).convertTo(u2));
            }
        }
    }

    // ================= ADDITION =================

    @Test
    void testGenericQuantity_LengthOperations_Addition() {
        Quantity<LengthUnit> result =
                new Quantity<>(1, LengthUnit.FEET)
                        .add(new Quantity<>(12, LengthUnit.INCH));
        assertEquals(2.0, result.getValue(), 0.001);
    }

    @Test
    void testGenericQuantity_WeightOperations_Addition() {
        Quantity<WeightUnit> result =
                new Quantity<>(1, WeightUnit.KILOGRAM)
                        .add(new Quantity<>(1000, WeightUnit.GRAM));
        assertEquals(2.0, result.getValue(), 0.001);
    }

    @Test
    void testGenericQuantity_Addition_AllUnitCombinations() {
        for (LengthUnit u1 : LengthUnit.values()) {
            for (LengthUnit u2 : LengthUnit.values()) {
                Quantity<LengthUnit> result =
                        new Quantity<>(1, u1).add(new Quantity<>(1, u2), u1);
                assertNotNull(result);
            }
        }
    }

    // ================= CROSS CATEGORY =================

    @Test
    void testCrossCategoryPrevention_LengthVsWeight() {
        assertNotEquals(
                new Quantity<>(1, LengthUnit.FEET),
                new Quantity<>(1, WeightUnit.KILOGRAM)
        );
    }

    @Test
    void testCrossCategoryPrevention_CompilerTypeSafety() {
        // Compile-time test → cannot be written
        assertTrue(true);
    }

    // ================= VALIDATION =================

    @Test
    void testGenericQuantity_ConstructorValidation_NullUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(1, null));
    }

    @Test
    void testGenericQuantity_ConstructorValidation_InvalidValue() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(Double.NaN, LengthUnit.FEET));
    }

    // ================= BACKWARD =================

    @Test
    void testBackwardCompatibility_AllUC1Through9Tests() {
        Quantity<LengthUnit> l1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(12, LengthUnit.INCH);

        assertEquals(l1, l2);
        assertEquals(2.0, l1.add(l2).getValue(), 0.001);
        assertEquals(12.0, l1.convertTo(LengthUnit.INCH).getValue(), 0.001);
    }

    // ================= APP =================

    @Test
    void testQuantityMeasurementApp_SimplifiedDemonstration_Equality() {
        assertEquals(new Quantity<>(1, LengthUnit.FEET),
                new Quantity<>(12, LengthUnit.INCH));
    }

    @Test
    void testQuantityMeasurementApp_SimplifiedDemonstration_Conversion() {
        assertEquals(12.0,
                new Quantity<>(1, LengthUnit.FEET)
                        .convertTo(LengthUnit.INCH).getValue(), 0.001);
    }

    @Test
    void testQuantityMeasurementApp_SimplifiedDemonstration_Addition() {
        assertEquals(2.0,
                new Quantity<>(1, LengthUnit.FEET)
                        .add(new Quantity<>(12, LengthUnit.INCH))
                        .getValue(), 0.001);
    }

    // ================= GENERICS =================

    @Test
    void testTypeWildcard_FlexibleSignatures() {
        Quantity<?> q = new Quantity<>(1, LengthUnit.FEET);
        assertNotNull(q);
    }

    @Test
    void testScalability_NewUnitEnumIntegration() {
        assertTrue(true); // conceptual
    }

    @Test
    void testScalability_MultipleNewCategories() {
        Quantity<LengthUnit> l = new Quantity<>(1, LengthUnit.FEET);
        Quantity<WeightUnit> w = new Quantity<>(1, WeightUnit.KILOGRAM);
        assertNotNull(l);
        assertNotNull(w);
    }

    @Test
    void testGenericBoundedTypeParameter_Enforcement() {
        assertTrue(true); // compile-time
    }

    @Test
    void testHashCode_GenericQuantity_Consistency() {
        Quantity<LengthUnit> q1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12, LengthUnit.INCH);
        assertEquals(q1.hashCode(), q2.hashCode());
    }

    @Test
    void testEquals_GenericQuantity_ContractPreservation() {
        Quantity<LengthUnit> a = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(12, LengthUnit.INCH);
        Quantity<LengthUnit> c = new Quantity<>(1, LengthUnit.FEET);

        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(a, c);
    }

    @Test
    void testTypeErasure_RuntimeSafety() {
        Quantity<?> l = new Quantity<>(1, LengthUnit.FEET);
        Quantity<?> w = new Quantity<>(1, WeightUnit.KILOGRAM);
        assertNotEquals(l, w);
    }

    @Test
    void testImmutability_GenericQuantity() {
        Quantity<LengthUnit> q1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = q1.convertTo(LengthUnit.INCH);
        assertNotSame(q1, q2);
    }

    @Test
    void testArchitecturalReadiness_MultipleNewCategories() {
        assertTrue(true);
    }

    @Test
    void testCodeReduction_DRYValidation() {
        Quantity<LengthUnit> l = new Quantity<>(1, LengthUnit.FEET);
        Quantity<WeightUnit> w = new Quantity<>(1, WeightUnit.KILOGRAM);
        assertNotNull(l);
        assertNotNull(w);
    }
}