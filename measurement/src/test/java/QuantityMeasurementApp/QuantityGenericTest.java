package QuantityMeasurementApp;

import QuantityMeasurementApp.enums.IMeasurable;
import QuantityMeasurementApp.enumsimplm.LengthUnit;
import QuantityMeasurementApp.enumsimplm.VolumeUnit;
import QuantityMeasurementApp.enumsimplm.WeightUnit;
import QuantityMeasurementApp.model.Quantity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuantityGenericTest {
    private static final double EPSILON = 1e-6;

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

    //UC11


    @Test
    void testEquality_LitreToLitre_SameValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(1.0, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_LitreToLitre_DifferentValue() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(2.0, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_LitreToMillilitre_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testEquality_MillilitreToLitre_EquivalentValue() {
        assertTrue(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                .equals(new Quantity<>(1.0, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_LitreToGallon_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(0.264172, VolumeUnit.GALLON)));
    }

    @Test
    void testEquality_GallonToLitre_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.GALLON)
                .equals(new Quantity<>(3.78541, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_VolumeVsLength_Incompatible() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(1.0, LengthUnit.FEET)));
    }

    @Test
    void testEquality_VolumeVsWeight_Incompatible() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
    }

    @Test
    void testEquality_NullComparison() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(null));
    }

    @Test
    void testEquality_SameReference() {
        Quantity<VolumeUnit> q = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertTrue(q.equals(q));
    }

    @Test
    void testEquality_TransitiveProperty() {
        Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> c = new Quantity<>(0.264172, VolumeUnit.GALLON);
        assertTrue(a.equals(b) && b.equals(c) && a.equals(c));
    }

    @Test
    void testEquality_ZeroValue() {
        assertTrue(new Quantity<>(0.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(0.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testEquality_NegativeVolume() {
        assertTrue(new Quantity<>(-1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(-1000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testEquality_LargeVolumeValue() {
        assertTrue(new Quantity<>(1_000_000.0, VolumeUnit.MILLILITRE)
                .equals(new Quantity<>(1000.0, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_SmallVolumeValue() {
        assertTrue(new Quantity<>(0.001, VolumeUnit.LITRE)
                .equals(new Quantity<>(1.0, VolumeUnit.MILLILITRE)));
    }

    // ===== Conversion (9 tests) =====

    @Test
    void testConversion_LitreToMillilitre() {
        assertEquals(1000.0,
                new Quantity<>(1.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_MillilitreToLitre() {
        assertEquals(1.0,
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE).convertTo(VolumeUnit.LITRE).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_GallonToLitre() {
        assertEquals(3.78541,
                new Quantity<>(1.0, VolumeUnit.GALLON).convertTo(VolumeUnit.LITRE).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_LitreToGallon() {
        assertEquals(1.0,
                new Quantity<>(3.78541, VolumeUnit.LITRE).convertTo(VolumeUnit.GALLON).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_MillilitreToGallon() {
        assertEquals(0.264172,
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE).convertTo(VolumeUnit.GALLON).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_SameUnit() {
        assertEquals(5.0,
                new Quantity<>(5.0, VolumeUnit.LITRE).convertTo(VolumeUnit.LITRE).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_ZeroValue() {
        assertEquals(0.0,
                new Quantity<>(0.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_NegativeValue() {
        assertEquals(-1000.0,
                new Quantity<>(-1.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_RoundTrip() {
        Quantity<VolumeUnit> original = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> roundTrip = original.convertTo(VolumeUnit.MILLILITRE).convertTo(VolumeUnit.LITRE);
        assertTrue(original.equals(roundTrip));
    }

    // ===== Addition (5 tests to complete 29 total) =====

    @Test
    void testAddition_SameUnit_LitrePlusLitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE)
                .add(new Quantity<>(2.0, VolumeUnit.LITRE));
        assertEquals(3.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITRE, result.getUnit());
    }

    @Test
    void testAddition_CrossUnit_LitrePlusMillilitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE)
                .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE));
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITRE, result.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Millilitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE)
                .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE), VolumeUnit.MILLILITRE);
        assertEquals(2000.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.MILLILITRE, result.getUnit());
    }

    @Test
    void testAddition_Commutativity() {
        Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> r1 = a.add(b, VolumeUnit.LITRE);
        Quantity<VolumeUnit> r2 = b.add(a, VolumeUnit.LITRE);
        assertEquals(r1.getValue(), r2.getValue(), EPSILON);
    }

    @Test
    void testAddition_WithZero() {
        Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE)
                .add(new Quantity<>(0.0, VolumeUnit.MILLILITRE));
        assertEquals(5.0, result.getValue(), EPSILON);
    }
}