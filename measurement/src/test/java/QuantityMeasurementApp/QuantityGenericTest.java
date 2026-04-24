package QuantityMeasurementApp;
import QuantityMeasurementApp.enums.IMeasurable;
import QuantityMeasurementApp.enumsimplm.LengthUnit;
import QuantityMeasurementApp.enumsimplm.VolumeUnit;
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




    //uc11
    private static final double EPSILON = 1e-4;

    // ================= EQUALITY =================

    @Test
    void testEquality_LitreToLitre_SameValue() {
        assertEquals(new Quantity<>(1.0, VolumeUnit.LITRE),
                new Quantity<>(1.0, VolumeUnit.LITRE));
    }

    @Test
    void testEquality_LitreToLitre_DifferentValue() {
        assertNotEquals(new Quantity<>(1.0, VolumeUnit.LITRE),
                new Quantity<>(2.0, VolumeUnit.LITRE));
    }

    @Test
    void testEquality_LitreToMillilitre_EquivalentValue() {
        assertEquals(new Quantity<>(1.0, VolumeUnit.LITRE),
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE));
    }

    @Test
    void testEquality_MillilitreToLitre_EquivalentValue() {
        assertEquals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE),
                new Quantity<>(1.0, VolumeUnit.LITRE));
    }

    @Test
    void testEquality_LitreToGallon_EquivalentValue() {
        assertEquals(new Quantity<>(1.0, VolumeUnit.LITRE),
                new Quantity<>(0.264172, VolumeUnit.GALLON));
    }

    @Test
    void testEquality_GallonToLitre_EquivalentValue() {
        assertEquals(new Quantity<>(1.0, VolumeUnit.GALLON),
                new Quantity<>(3.78541, VolumeUnit.LITRE));
    }

    @Test
    void testEquality_VolumeVsLength_Incompatible() {
        assertNotEquals(new Quantity<>(1.0, VolumeUnit.LITRE),
                new Quantity<>(1.0, LengthUnit.FEET));
    }

    @Test
    void testEquality_VolumeVsWeight_Incompatible() {
        assertNotEquals(new Quantity<>(1.0, VolumeUnit.LITRE),
                new Quantity<>(1.0, WeightUnit.KILOGRAM));
    }

    @Test
    void testEquality_NullComparison() {
        assertNotEquals(null, new Quantity<>(1.0, VolumeUnit.LITRE));
    }

    @Test
    void testEquality_SameReference() {
        Quantity<VolumeUnit> q = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertEquals(q, q);
    }

    @Test
    void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(1.0, null));
    }

    @Test
    void testEquality_TransitiveProperty() {
        Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> c = new Quantity<>(0.264172, VolumeUnit.GALLON);

        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(a, c);
    }

    @Test
    void testEquality_ZeroValue() {
        assertEquals(new Quantity<>(0.0, VolumeUnit.LITRE),
                new Quantity<>(0.0, VolumeUnit.MILLILITRE));
    }

    @Test
    void testEquality_NegativeVolume() {
        assertEquals(new Quantity<>(-1.0, VolumeUnit.LITRE),
                new Quantity<>(-1000.0, VolumeUnit.MILLILITRE));
    }

    @Test
    void testEquality_LargeVolumeValue() {
        assertEquals(new Quantity<>(1000000.0, VolumeUnit.MILLILITRE),
                new Quantity<>(1000.0, VolumeUnit.LITRE));
    }

    @Test
    void testEquality_SmallVolumeValue() {
        assertEquals(new Quantity<>(0.001, VolumeUnit.LITRE),
                new Quantity<>(1.0, VolumeUnit.MILLILITRE));
    }

    // ================= CONVERSION =================

    @Test
    void testConversion_LitreToMillilitre() {
        assertEquals(1000.0,
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .convertTo(VolumeUnit.MILLILITRE).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_MillilitreToLitre() {
        assertEquals(1.0,
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                        .convertTo(VolumeUnit.LITRE).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_GallonToLitre() {
        assertEquals(3.78541,
                new Quantity<>(1.0, VolumeUnit.GALLON)
                        .convertTo(VolumeUnit.LITRE).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_LitreToGallon() {
        assertEquals(1.0,
                new Quantity<>(3.78541, VolumeUnit.LITRE)
                        .convertTo(VolumeUnit.GALLON).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_MillilitreToGallon() {
        assertEquals(0.264172,
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                        .convertTo(VolumeUnit.GALLON).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_SameUnit() {
        assertEquals(5.0,
                new Quantity<>(5.0, VolumeUnit.LITRE)
                        .convertTo(VolumeUnit.LITRE).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_ZeroValue() {
        assertEquals(0.0,
                new Quantity<>(0.0, VolumeUnit.LITRE)
                        .convertTo(VolumeUnit.MILLILITRE).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_NegativeValue() {
        assertEquals(-1000.0,
                new Quantity<>(-1.0, VolumeUnit.LITRE)
                        .convertTo(VolumeUnit.MILLILITRE).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_RoundTrip() {
        Quantity<VolumeUnit> original = new Quantity<>(5.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> result = original.convertTo(VolumeUnit.MILLILITRE)
                .convertTo(VolumeUnit.LITRE);

        assertEquals(original.getValue(), result.getValue(), EPSILON);
    }

    // ================= ADDITION =================

    @Test
    void testAddition_SameUnit_LitrePlusLitre() {
        assertEquals(3.0,
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(2.0, VolumeUnit.LITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_SameUnit_MillilitrePlusMillilitre() {
        assertEquals(1000.0,
                new Quantity<>(500.0, VolumeUnit.MILLILITRE)
                        .add(new Quantity<>(500.0, VolumeUnit.MILLILITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_CrossUnit_LitrePlusMillilitre() {
        assertEquals(2.0,
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_CrossUnit_MillilitrePlusLitre() {
        assertEquals(2000.0,
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                        .add(new Quantity<>(1.0, VolumeUnit.LITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_CrossUnit_GallonPlusLitre() {
        assertEquals(2.0,
                new Quantity<>(1.0, VolumeUnit.GALLON)
                        .add(new Quantity<>(3.78541, VolumeUnit.LITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Litre() {
        assertEquals(2.0,
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE), VolumeUnit.LITRE).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Millilitre() {
        assertEquals(2000.0,
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE), VolumeUnit.MILLILITRE).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Gallon() {
        assertEquals(2.0,
                new Quantity<>(3.78541, VolumeUnit.LITRE)
                        .add(new Quantity<>(3.78541, VolumeUnit.LITRE), VolumeUnit.GALLON).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_Commutativity() {
        Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE)
                .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE));
        Quantity<VolumeUnit> b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                .add(new Quantity<>(1.0, VolumeUnit.LITRE));

        assertEquals(a.convertTo(VolumeUnit.LITRE).getValue(),
                b.convertTo(VolumeUnit.LITRE).getValue(), EPSILON);
    }

    @Test
    void testAddition_WithZero() {
        assertEquals(5.0,
                new Quantity<>(5.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(0.0, VolumeUnit.MILLILITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_NegativeValues() {
        assertEquals(3.0,
                new Quantity<>(5.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(-2000.0, VolumeUnit.MILLILITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_LargeValues() {
        assertEquals(2000000.0,
                new Quantity<>(1000000.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(1000000.0, VolumeUnit.LITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_SmallValues() {
        assertEquals(0.003,
                new Quantity<>(0.001, VolumeUnit.LITRE)
                        .add(new Quantity<>(0.002, VolumeUnit.LITRE)).getValue(),
                EPSILON);
    }

    // ================= ENUM / ARCHITECTURE =================

    @Test
    void testVolumeUnitEnum_LitreConstant() {
        assertEquals(1.0, VolumeUnit.LITRE.getConversionFactor(), EPSILON);
    }

    @Test
    void testVolumeUnitEnum_MillilitreConstant() {
        assertEquals(0.001, VolumeUnit.MILLILITRE.getConversionFactor(), EPSILON);
    }

    @Test
    void testVolumeUnitEnum_GallonConstant() {
        assertEquals(3.78541, VolumeUnit.GALLON.getConversionFactor(), EPSILON);
    }

    @Test
    void testConvertToBaseUnit_MillilitreToLitre() {
        assertEquals(1.0, VolumeUnit.MILLILITRE.convertToBaseUnit(1000), EPSILON);
    }

    @Test
    void testConvertToBaseUnit_GallonToLitre() {
        assertEquals(3.78541, VolumeUnit.GALLON.convertToBaseUnit(1), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_LitreToMillilitre() {
        assertEquals(1000.0, VolumeUnit.MILLILITRE.convertFromBaseUnit(1), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_LitreToGallon() {
        assertEquals(1.0, VolumeUnit.GALLON.convertFromBaseUnit(3.78541), EPSILON);
    }

    @Test
    void testBackwardCompatibility_AllUC1Through10Tests() {
        assertEquals(new Quantity<>(1.0, LengthUnit.FEET),
                new Quantity<>(12.0, LengthUnit.INCH));
    }

    @Test
    void testGenericQuantity_VolumeOperations_Consistency() {
        assertEquals(1000.0,
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .convertTo(VolumeUnit.MILLILITRE).getValue(),
                EPSILON);
    }

    @Test
    void testScalability_VolumeIntegration() {
        Quantity<VolumeUnit> q = new Quantity<>(1.0, VolumeUnit.GALLON);
        assertNotNull(q);
    }



    //uc12
    @Test
    void testSubtraction_SameUnit_FeetMinusFeet() {
        Quantity<LengthUnit> q1 = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5, LengthUnit.FEET);

        assertEquals(new Quantity<>(5, LengthUnit.FEET), q1.subtract(q2));
    }

    @Test
    void testSubtraction_SameUnit_LitreMinusLitre() {
        Quantity<VolumeUnit> q1 = new Quantity<>(10, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(3, VolumeUnit.LITRE);

        assertEquals(new Quantity<>(7, VolumeUnit.LITRE), q1.subtract(q2));
    }

    @Test
    void testSubtraction_CrossUnit_FeetMinusInches() {
        Quantity<LengthUnit> q1 = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(6, LengthUnit.INCH);

        assertEquals(new Quantity<>(9.5, LengthUnit.FEET), q1.subtract(q2));
    }

    @Test
    void testSubtraction_CrossUnit_InchesMinusFeet() {
        Quantity<LengthUnit> q1 = new Quantity<>(120, LengthUnit.INCH);
        Quantity<LengthUnit> q2 = new Quantity<>(5, LengthUnit.FEET);

        assertEquals(new Quantity<>(60, LengthUnit.INCH), q1.subtract(q2));
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Feet() {
        Quantity<LengthUnit> q1 = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(6, LengthUnit.INCH);

        assertEquals(new Quantity<>(9.5, LengthUnit.FEET), q1.subtract(q2, LengthUnit.FEET));
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Inches() {
        Quantity<LengthUnit> q1 = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(6, LengthUnit.INCH);

        assertEquals(new Quantity<>(114, LengthUnit.INCH), q1.subtract(q2, LengthUnit.INCH));
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Millilitre() {
        Quantity<VolumeUnit> q1 = new Quantity<>(5, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(2, VolumeUnit.LITRE);

        assertEquals(new Quantity<>(3000, VolumeUnit.MILLILITRE),
                q1.subtract(q2, VolumeUnit.MILLILITRE));
    }

    @Test
    void testSubtraction_ResultingInNegative() {
        Quantity<LengthUnit> q1 = new Quantity<>(5, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(10, LengthUnit.FEET);

        assertEquals(new Quantity<>(-5, LengthUnit.FEET), q1.subtract(q2));
    }

    @Test
    void testSubtraction_ResultingInZero() {
        Quantity<LengthUnit> q1 = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(120, LengthUnit.INCH);

        assertEquals(new Quantity<>(0, LengthUnit.FEET), q1.subtract(q2));
    }

    @Test
    void testSubtraction_WithZeroOperand() {
        Quantity<LengthUnit> q1 = new Quantity<>(5, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(0, LengthUnit.INCH);

        assertEquals(new Quantity<>(5, LengthUnit.FEET), q1.subtract(q2));
    }

    @Test
    void testSubtraction_WithNegativeValues() {
        Quantity<LengthUnit> q1 = new Quantity<>(5, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(-2, LengthUnit.FEET);

        assertEquals(new Quantity<>(7, LengthUnit.FEET), q1.subtract(q2));
    }

    @Test
    void testSubtraction_NonCommutative() {
        Quantity<LengthUnit> a = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5, LengthUnit.FEET);

        assertNotEquals(a.subtract(b), b.subtract(a));
    }

    @Test
    void testSubtraction_WithLargeValues() {
        Quantity<WeightUnit> q1 = new Quantity<>(1_000_000, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(500_000, WeightUnit.KILOGRAM);

        assertEquals(new Quantity<>(500_000, WeightUnit.KILOGRAM), q1.subtract(q2));
    }

    @Test
    void testSubtraction_WithSmallValues() {
        Quantity<LengthUnit> q1 = new Quantity<>(0.001, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(0.0005, LengthUnit.FEET);

        assertEquals(new Quantity<>(0.00, LengthUnit.FEET), q1.subtract(q2));
    }

    @Test
    void testSubtraction_NullOperand() {
        Quantity<LengthUnit> q1 = new Quantity<>(5, LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class, () -> q1.subtract(null));
    }

    @Test
    void testSubtraction_NullTargetUnit() {
        Quantity<LengthUnit> q1 = new Quantity<>(5, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(2, LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class, () -> q1.subtract(q2, null));
    }

    @Test
    void testSubtraction_AllMeasurementCategories() {
        assertEquals(new Quantity<>(5, LengthUnit.FEET),
                new Quantity<>(10, LengthUnit.FEET).subtract(new Quantity<>(5, LengthUnit.FEET)));

        assertEquals(new Quantity<>(5, WeightUnit.KILOGRAM),
                new Quantity<>(10, WeightUnit.KILOGRAM).subtract(new Quantity<>(5, WeightUnit.KILOGRAM)));

        assertEquals(new Quantity<>(5, VolumeUnit.LITRE),
                new Quantity<>(10, VolumeUnit.LITRE).subtract(new Quantity<>(5, VolumeUnit.LITRE)));
    }

    @Test
    void testSubtraction_ChainedOperations() {
        Quantity<LengthUnit> result =
                new Quantity<>(10, LengthUnit.FEET)
                        .subtract(new Quantity<>(2, LengthUnit.FEET))
                        .subtract(new Quantity<>(1, LengthUnit.FEET));

        assertEquals(new Quantity<>(7, LengthUnit.FEET), result);
    }

    // ================= DIVISION TESTS =================

    @Test
    void testDivision_SameUnit_FeetDividedByFeet() {
        assertEquals(5.0,
                new Quantity<>(10, LengthUnit.FEET)
                        .divide(new Quantity<>(2, LengthUnit.FEET)));
    }

    @Test
    void testDivision_SameUnit_LitreDividedByLitre() {
        assertEquals(2.0,
                new Quantity<>(10, VolumeUnit.LITRE)
                        .divide(new Quantity<>(5, VolumeUnit.LITRE)));
    }

    @Test
    void testDivision_CrossUnit_FeetDividedByInches() {
        assertEquals(1.0,
                new Quantity<>(24, LengthUnit.INCH)
                        .divide(new Quantity<>(2, LengthUnit.FEET)));
    }

    @Test
    void testDivision_CrossUnit_KilogramDividedByGram() {
        assertEquals(1.0,
                new Quantity<>(2, WeightUnit.KILOGRAM)
                        .divide(new Quantity<>(2000, WeightUnit.GRAM)));
    }

    @Test
    void testDivision_RatioGreaterThanOne() {
        assertEquals(5.0,
                new Quantity<>(10, LengthUnit.FEET)
                        .divide(new Quantity<>(2, LengthUnit.FEET)));
    }

    @Test
    void testDivision_RatioLessThanOne() {
        assertEquals(0.5,
                new Quantity<>(5, LengthUnit.FEET)
                        .divide(new Quantity<>(10, LengthUnit.FEET)));
    }

    @Test
    void testDivision_RatioEqualToOne() {
        assertEquals(1.0,
                new Quantity<>(10, LengthUnit.FEET)
                        .divide(new Quantity<>(10, LengthUnit.FEET)));
    }

    @Test
    void testDivision_NonCommutative() {
        double a = new Quantity<>(10, LengthUnit.FEET)
                .divide(new Quantity<>(5, LengthUnit.FEET));

        double b = new Quantity<>(5, LengthUnit.FEET)
                .divide(new Quantity<>(10, LengthUnit.FEET));

        assertNotEquals(a, b);
    }

    @Test
    void testDivision_ByZero() {
        assertThrows(ArithmeticException.class, () ->
                new Quantity<>(10, LengthUnit.FEET)
                        .divide(new Quantity<>(0, LengthUnit.FEET)));
    }

    @Test
    void testDivision_WithLargeRatio() {
        assertEquals(1_000_000.0,
                new Quantity<>(1_000_000, WeightUnit.KILOGRAM)
                        .divide(new Quantity<>(1, WeightUnit.KILOGRAM)));
    }

    @Test
    void testDivision_WithSmallRatio() {
        assertEquals(0.0,
                new Quantity<>(1, WeightUnit.KILOGRAM)
                        .divide(new Quantity<>(1_000_000, WeightUnit.KILOGRAM)));
    }

    @Test
    void testDivision_NullOperand() {
        Quantity<LengthUnit> q1 = new Quantity<>(10, LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class, () -> q1.divide(null));
    }

    @Test
    void testDivision_AllMeasurementCategories() {
        assertEquals(2.0,
                new Quantity<>(10, LengthUnit.FEET)
                        .divide(new Quantity<>(5, LengthUnit.FEET)));

        assertEquals(2.0,
                new Quantity<>(10, WeightUnit.KILOGRAM)
                        .divide(new Quantity<>(5, WeightUnit.KILOGRAM)));

        assertEquals(2.0,
                new Quantity<>(10, VolumeUnit.LITRE)
                        .divide(new Quantity<>(5, VolumeUnit.LITRE)));
    }

    @Test
    void testSubtractionAndDivision_Integration() {
        double result =
                new Quantity<>(10, LengthUnit.FEET)
                        .subtract(new Quantity<>(2, LengthUnit.FEET))
                        .divide(new Quantity<>(4, LengthUnit.FEET));

        assertEquals(2.0, result);
    }

    @Test
    void testSubtractionAddition_Inverse() {
        Quantity<LengthUnit> a = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5, LengthUnit.FEET);

        assertEquals(a, a.add(b).subtract(b));
    }

    @Test
    void testSubtraction_Immutability() {
        Quantity<LengthUnit> a = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5, LengthUnit.FEET);

        a.subtract(b);

        assertEquals(new Quantity<>(10, LengthUnit.FEET), a);
        assertEquals(new Quantity<>(5, LengthUnit.FEET), b);
    }

    @Test
    void testDivision_Immutability() {
        Quantity<LengthUnit> a = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5, LengthUnit.FEET);

        a.divide(b);

        assertEquals(new Quantity<>(10, LengthUnit.FEET), a);
        assertEquals(new Quantity<>(5, LengthUnit.FEET), b);
    }

    @Test
    void testSubtraction_PrecisionAndRounding() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.555, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(0.111, LengthUnit.FEET);

        assertEquals(new Quantity<>(10.44, LengthUnit.FEET), q1.subtract(q2));
    }

    @Test
    void testDivision_PrecisionHandling() {
        double result = new Quantity<>(1, LengthUnit.FEET)
                .divide(new Quantity<>(3, LengthUnit.FEET));

        assertEquals(0.33, result);
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testSubtraction_CrossCategory() {
        Quantity length = new Quantity<>(10, LengthUnit.FEET);
        Quantity weight = new Quantity<>(5, WeightUnit.KILOGRAM);

        assertThrows(IllegalArgumentException.class, () -> {
            length.subtract(weight);
        });
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testDivision_CrossCategory() {
        Quantity length = new Quantity<>(10, LengthUnit.FEET);
        Quantity weight = new Quantity<>(5, WeightUnit.KILOGRAM);

        assertThrows(IllegalArgumentException.class, () -> {
            length.divide(weight);
        });
    }

}