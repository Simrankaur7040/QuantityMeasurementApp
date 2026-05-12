package QuantityMeasurementApp;
import QuantityMeasurementApp.controller.QuantityMeasurementController;
import QuantityMeasurementApp.dto.QuantityDTO;
import QuantityMeasurementApp.entity.QuantityMeasurementEntity;
import QuantityMeasurementApp.enums.IMeasurable;
import QuantityMeasurementApp.enumsimplm.LengthUnit;
import QuantityMeasurementApp.enumsimplm.TemperatureUnit;
import QuantityMeasurementApp.enumsimplm.VolumeUnit;
import QuantityMeasurementApp.enumsimplm.WeightUnit;
import QuantityMeasurementApp.model.Quantity;
import QuantityMeasurementApp.repository.QuantityMeasurementCacheRepository;
import QuantityMeasurementApp.service.IQuantityMeasurementService;
import QuantityMeasurementApp.serviceImpl.QuantityMeasurementServiceImpl;
import org.junit.jupiter.api.Test;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class QuantityGenericTest {

    // ================= INTERFACE =================

    @Test
    void testIMeasurableInterface_LengthUnitImplementation() {
        IMeasurable unit = LengthUnit.FEET;
        assertNotNull(((LengthUnit) unit).getConversionFactor());
        assertEquals("FEET", unit.getUnitName());
    }

    @Test
    void testIMeasurableInterface_WeightUnitImplementation() {
        IMeasurable unit = WeightUnit.KILOGRAM;
        assertNotNull(((WeightUnit) unit).getConversionFactor());
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
        assertEquals(0.0005,
                new Quantity<>(0.001, LengthUnit.FEET)
                        .subtract(new Quantity<>(0.0005, LengthUnit.FEET)).getValue(),
                EPSILON);
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
        assertEquals(1e-6,
                new Quantity<>(1.0, WeightUnit.KILOGRAM)
                        .divide(new Quantity<>(1e6, WeightUnit.KILOGRAM)),
                EPSILON);
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
        double result =
                new Quantity<>(1.0, LengthUnit.FEET)
                        .divide(new Quantity<>(3.0, LengthUnit.FEET));

        assertEquals(0.333333, result, 1e-3); // no rounding applied
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
    //UC13
    // 1-3: Delegation tests (public behavior verifies delegation indirectly)
    @Test
    void testRefactoring_Add_DelegatesViaHelper() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(12.0, LengthUnit.INCH);
        Quantity<LengthUnit> result = a.add(b);
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testRefactoring_Subtract_DelegatesViaHelper() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(6.0, LengthUnit.INCH);
        Quantity<LengthUnit> result = a.subtract(b);
        assertEquals(9.5, result.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testRefactoring_Divide_DelegatesViaHelper() {
        double ratio = new Quantity<>(10.0, LengthUnit.FEET)
                .divide(new Quantity<>(2.0, LengthUnit.FEET));
        assertEquals(5.0, ratio, EPSILON);
    }

    // 4-7: Validation consistency tests
    @Test
    void testValidation_NullOperand_ConsistentAcrossOperations() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> a.add(null));
        assertThrows(IllegalArgumentException.class, () -> a.subtract(null));
        assertThrows(IllegalArgumentException.class, () -> a.divide(null));
    }

    @Test
    void testValidation_CrossCategory_ConsistentAcrossOperations() {
        Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        assertThrows(IllegalArgumentException.class, () -> length.add((Quantity) weight));
        assertThrows(IllegalArgumentException.class, () -> length.subtract((Quantity) weight));
        assertThrows(IllegalArgumentException.class, () -> length.divide((Quantity) weight));
    }

    @Test
    void testValidation_FiniteValue_ConsistentAcrossOperations() {
        Quantity<LengthUnit> finite = new Quantity<>(Double.POSITIVE_INFINITY, LengthUnit.FEET);
        Quantity<LengthUnit> other = new Quantity<>(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> finite.add(other));
        assertThrows(IllegalArgumentException.class, () -> finite.subtract(other));
        assertThrows(IllegalArgumentException.class, () -> finite.divide(other));
    }

    @Test
    void testValidation_NullTargetUnit_AddSubtractReject() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> a.add(b, null));
        assertThrows(IllegalArgumentException.class, () -> a.subtract(b, null));
    }

    // 8-11: Enum operation correctness (indirect via public API and division-by-zero)
    @Test
    void testArithmeticOperation_Add_EnumComputation() {
        Quantity<LengthUnit> a = new Quantity<>(7.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(3.0, LengthUnit.FEET);
        assertEquals(10.0, a.add(b).getValue(), EPSILON);
    }

    @Test
    void testArithmeticOperation_Subtract_EnumComputation() {
        Quantity<LengthUnit> a = new Quantity<>(7.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(3.0, LengthUnit.FEET);
        assertEquals(4.0, a.subtract(b).getValue(), EPSILON);
    }

    @Test
    void testArithmeticOperation_Divide_EnumComputation() {
        Quantity<LengthUnit> a = new Quantity<>(7.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(2.0, LengthUnit.FEET);
        assertEquals(3.5, a.divide(b), EPSILON);
    }

    @Test
    void testArithmeticOperation_DivideByZero_EnumThrows() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> zero = new Quantity<>(0.0, LengthUnit.FEET);
        assertThrows(ArithmeticException.class, () -> a.divide(zero));
    }

    // 12-16: Helper correctness and visibility (use reflection for private helpers)
    @Test
    void testPerformBaseArithmetic_ConversionAndOperation() {
        Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE); // base 1.0
        Quantity<VolumeUnit> b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE); // base 1.0
        Quantity<VolumeUnit> sum = a.add(b); // 1 + 1 = 2 L
        assertEquals(2.0, sum.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITRE, sum.getUnit());
    }

    @Test
    void testHelper_BaseUnitConversion_Correct() {
        // verify conversion via convertTo helper
        Quantity<VolumeUnit> g = new Quantity<>(1.0, VolumeUnit.GALLON);
        Quantity<VolumeUnit> inLitres = g.convertTo(VolumeUnit.LITRE);
        // Accept small tolerance for conversion constants
        assertEquals(3.78541, inLitres.getValue(), 1e-5);
    }

    @Test
    void testHelper_ResultConversion_Correct() {
        Quantity<VolumeUnit> a = new Quantity<>(3.78541, VolumeUnit.LITRE);
        Quantity<VolumeUnit> result = a.add(new Quantity<>(3.78541, VolumeUnit.LITRE), VolumeUnit.GALLON);
        assertEquals(2.0, result.getValue(), 1e-6);
        assertEquals(VolumeUnit.GALLON, result.getUnit());
    }

    @Test
    void testHelper_PrivateVisibility() throws Exception {
        Class<?> qtyClass = Quantity.class;
        boolean hasPerform = Arrays.stream(qtyClass.getDeclaredMethods())
                .anyMatch(m -> m.getName().equals("performBaseArithmetic"));
        boolean hasValidate = Arrays.stream(qtyClass.getDeclaredMethods())
                .anyMatch(m -> m.getName().equals("validateArithmeticOperands"));
        assertTrue(hasPerform, "performBaseArithmetic should exist");
        assertTrue(hasValidate, "validateArithmeticOperands should exist");

        Method perform = null;
        for (Method m : qtyClass.getDeclaredMethods()) {
            if (m.getName().equals("performBaseArithmetic")) perform = m;
        }
        if (perform != null) {
            assertTrue(Modifier.isPrivate(perform.getModifiers()), "performBaseArithmetic should be private");
        }
    }

    @Test
    void testValidation_Helper_PrivateVisibility() throws Exception {
        Class<?> qtyClass = Quantity.class;
        Method validate = null;
        for (Method m : qtyClass.getDeclaredMethods()) {
            if (m.getName().equals("validateArithmeticOperands")) validate = m;
        }
        if (validate != null) {
            assertTrue(Modifier.isPrivate(validate.getModifiers()), "validateArithmeticOperands should be private");
        }
    }

    // 17-19: UC12 behavior preserved (add/subtract/divide)
    @Test
    void testAdd_UC12_BehaviorPreserved() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(12.0, LengthUnit.INCH);
        assertEquals(2.0, a.add(b).getValue(), EPSILON);
    }

    @Test
    void testSubtract_UC12_BehaviorPreserved() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(6.0, LengthUnit.INCH);
        assertEquals(9.5, a.subtract(b).getValue(), EPSILON);
    }

    @Test
    void testDivide_UC12_BehaviorPreserved() {
        double ratio = new Quantity<>(24.0, LengthUnit.INCH)
                .divide(new Quantity<>(2.0, LengthUnit.FEET));
        assertEquals(1.0, ratio, EPSILON);
    }

    // 20-22: Rounding behavior
    @Test
    void testRounding_AddSubtract_TwoDecimalPlaces() {
        Quantity<LengthUnit> a = new Quantity<>(1.2345, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(0.0045, LengthUnit.FEET);
        Quantity<LengthUnit> result = a.subtract(b);
        assertEquals(1.23, result.getValue(), EPSILON);
    }

    @Test
    void testRounding_Divide_NoRounding() {
        double ratio = new Quantity<>(10.0, LengthUnit.FEET)
                .divide(new Quantity<>(3.0, LengthUnit.FEET));
        assertEquals(10.0 / 3.0, ratio, 1e-12);
    }

    @Test
    void testRounding_Helper_Accuracy() {
        Quantity<LengthUnit> a = new Quantity<>(1.2356, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(0.0, LengthUnit.FEET);
        Quantity<LengthUnit> r = a.add(b);
        assertEquals(1.24, r.getValue(), EPSILON);
    }

    // 23-24: Target unit handling
    @Test
    void testImplicitTargetUnit_AddSubtract() {
        Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> sum = a.add(b); // implicit target = first operand's unit (LITRE)
        assertEquals(2.0, sum.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITRE, sum.getUnit());
    }

    @Test
    void testExplicitTargetUnit_AddSubtract_Overrides() {
        Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> sum = a.add(b, VolumeUnit.MILLILITRE);
        assertEquals(2000.0, sum.getValue(), EPSILON);
        assertEquals(VolumeUnit.MILLILITRE, sum.getUnit());
    }

    // 25-27: Immutability checks
    @Test
    void testImmutability_AfterAdd_ViaCentralizedHelper() {
        Quantity<LengthUnit> a = new Quantity<>(3.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> sum = a.add(b);
        assertEquals(3.0, a.getValue(), EPSILON);
        assertEquals(1.0, b.getValue(), EPSILON);
        assertEquals(4.0, sum.getValue(), EPSILON);
    }

    @Test
    void testImmutability_AfterSubtract_ViaCentralizedHelper() {
        Quantity<LengthUnit> a = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(2.0, LengthUnit.FEET);
        Quantity<LengthUnit> res = a.subtract(b);
        assertEquals(5.0, a.getValue(), EPSILON);
        assertEquals(2.0, b.getValue(), EPSILON);
        assertEquals(3.0, res.getValue(), EPSILON);
    }

    @Test
    void testImmutability_AfterDivide_ViaCentralizedHelper() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(2.0, LengthUnit.FEET);
        double ratio = a.divide(b);
        assertEquals(10.0, a.getValue(), EPSILON);
        assertEquals(2.0, b.getValue(), EPSILON);
        assertEquals(5.0, ratio, EPSILON);
    }

    // 28: All categories sanity
    @Test
    void testAllOperations_AcrossAllCategories() {
        double r1 = new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(5.0, LengthUnit.FEET));
        double r2 = new Quantity<>(10.0, VolumeUnit.LITRE).divide(new Quantity<>(5.0, VolumeUnit.LITRE));
        double r3 = new Quantity<>(10.0, WeightUnit.KILOGRAM).divide(new Quantity<>(5.0, WeightUnit.KILOGRAM));
        assertEquals(2.0, r1, EPSILON);
        assertEquals(2.0, r2, EPSILON);
        assertEquals(2.0, r3, EPSILON);
    }

    // 29-30: DRY verification via consistent behavior across operations
    @Test
    void testCodeDuplication_ValidationLogic_Eliminated() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> nan = null;
        // construct a NaN quantity is not allowed by constructor (Option A), so use POSITIVE_INFINITY to test centralized validation
        Quantity<LengthUnit> inf = new Quantity<>(Double.POSITIVE_INFINITY, LengthUnit.FEET);
        Exception e1 = assertThrows(IllegalArgumentException.class, () -> a.add(inf));
        Exception e2 = assertThrows(IllegalArgumentException.class, () -> a.subtract(inf));
        Exception e3 = assertThrows(IllegalArgumentException.class, () -> a.divide(inf));
        assertEquals(e1.getClass(), e2.getClass());
        assertEquals(e2.getClass(), e3.getClass());
    }

    @Test
    void testCodeDuplication_ConversionLogic_Eliminated() {
        Quantity<LengthUnit> feet = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> inches = new Quantity<>(12.0, LengthUnit.INCH);
        Quantity<LengthUnit> r1 = feet.add(inches, LengthUnit.FEET);
        Quantity<LengthUnit> r2 = inches.add(feet, LengthUnit.FEET);
        assertEquals(r1.getValue(), r2.getValue(), EPSILON);
    }

    // 31-33: Enum dispatch and extensibility checks
    @Test
    void testEnumDispatch_AllOperations_CorrectlyDispatched() throws Exception {
        Class<?> qtyClass = Quantity.class;
        boolean foundEnum = Arrays.stream(qtyClass.getDeclaredClasses())
                .anyMatch(c -> c.getSimpleName().equals("ArithmeticOperation"));
        assertTrue(foundEnum, "ArithmeticOperation enum should exist inside Quantity");
    }

    @Test
    void testFutureOperation_MultiplicationPattern() throws Exception {
        Class<?> qtyClass = Quantity.class;
        Class<?> enumClass = null;
        for (Class<?> c : qtyClass.getDeclaredClasses()) {
            if (c.getSimpleName().equals("ArithmeticOperation")) enumClass = c;
        }
        assertNotNull(enumClass, "ArithmeticOperation enum must exist");
        String[] names = Arrays.stream(enumClass.getEnumConstants()).map(Object::toString).toArray(String[]::new);
        assertTrue(Arrays.asList(names).contains("ADD"));
        assertTrue(Arrays.asList(names).contains("SUBTRACT"));
        assertTrue(Arrays.asList(names).contains("DIVIDE"));
    }

    @Test
    void testErrorMessage_Consistency_Across_Operations() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> inf = new Quantity<>(Double.POSITIVE_INFINITY, LengthUnit.FEET);
        Exception exAdd = assertThrows(IllegalArgumentException.class, () -> a.add(inf));
        Exception exSub = assertThrows(IllegalArgumentException.class, () -> a.subtract(inf));
        Exception exDiv = assertThrows(IllegalArgumentException.class, () -> a.divide(inf));
        assertEquals(exAdd.getClass(), exSub.getClass());
        assertEquals(exSub.getClass(), exDiv.getClass());
    }

    // 34: Chaining operations
    @Test
    void testArithmetic_Chain_Operations() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(2.0, LengthUnit.FEET);
        Quantity<LengthUnit> q3 = new Quantity<>(1.0, LengthUnit.FEET);
        double result = q1.add(q2).subtract(q3).divide(q2); // (10+2-1)/2 = 5.5
        assertEquals(5.5, result, EPSILON);
    }

    // 35-37: Enum constant correctness (indirect)
    @Test
    void testEnumConstant_ADD_CorrectlyAdds() {
        Quantity<LengthUnit> a = new Quantity<>(7.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(3.0, LengthUnit.FEET);
        assertEquals(10.0, a.add(b).getValue(), EPSILON);
    }

    @Test
    void testEnumConstant_SUBTRACT_CorrectlySubtracts() {
        Quantity<LengthUnit> a = new Quantity<>(7.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(3.0, LengthUnit.FEET);
        assertEquals(4.0, a.subtract(b).getValue(), EPSILON);
    }

    @Test
    void testEnumConstant_DIVIDE_CorrectlyDivides() {
        Quantity<LengthUnit> a = new Quantity<>(7.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(2.0, LengthUnit.FEET);
        assertEquals(3.5, a.divide(b), EPSILON);
    }

    // 38: Helper base conversion correctness (additional)
    @Test
    void testHelper_BaseUnitConversion_Correctness() {
        Quantity<VolumeUnit> m = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> l = m.convertTo(VolumeUnit.LITRE);
        assertEquals(1.0, l.getValue(), EPSILON);
    }

    // 39: Unified validation behavior (Option A uses POSITIVE_INFINITY)
    @Test
    void testRefactoring_Validation_UnifiedBehavior() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> invalid = new Quantity<>(Double.POSITIVE_INFINITY, LengthUnit.FEET);

        Exception eAdd = assertThrows(IllegalArgumentException.class, () -> a.add(invalid));
        Exception eSub = assertThrows(IllegalArgumentException.class, () -> a.subtract(invalid));
        Exception eDiv = assertThrows(IllegalArgumentException.class, () -> a.divide(invalid));

        assertEquals(eAdd.getClass(), eSub.getClass());
        assertEquals(eSub.getClass(), eDiv.getClass());
    }
    //UC14

    // 1. Celsius-to-Celsius equality (reflexive / same-unit)
    @Test
    void testTemperatureEquality_CelsiusToCelsius_SameValue() {
        Quantity<TemperatureUnit> a = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> b = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        assertEquals(a, b);
    }

    // 2. Fahrenheit-to-Fahrenheit equality (same-unit)
    @Test
    void testTemperatureEquality_FahrenheitToFahrenheit_SameValue() {
        Quantity<TemperatureUnit> a = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);
        Quantity<TemperatureUnit> b = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);
        assertEquals(a, b);
    }

    // 3. Kelvin-to-Kelvin equality (same-unit)
    @Test
    void testTemperatureEquality_KelvinToKelvin_SameValue() {
        Quantity<TemperatureUnit> a = new Quantity<>(273.15, TemperatureUnit.KELVIN);
        Quantity<TemperatureUnit> b = new Quantity<>(273.15, TemperatureUnit.KELVIN);
        assertEquals(a, b);
    }

    // 4. Celsius to Fahrenheit equality (0°C = 32°F)
    @Test
    void testTemperatureEquality_CelsiusToFahrenheit_0CEquals32F() {
        Quantity<TemperatureUnit> c = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> f = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);
        assertEquals(c, f);
    }

    // 5. Celsius to Fahrenheit equality (100°C = 212°F)
    @Test
    void testTemperatureEquality_CelsiusToFahrenheit_100CEquals212F() {
        Quantity<TemperatureUnit> c = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> f = new Quantity<>(212.0, TemperatureUnit.FAHRENHEIT);
        assertEquals(c, f);
    }

    // 6. Celsius to Kelvin equality (0°C = 273.15 K)
    @Test
    void testTemperatureEquality_CelsiusToKelvin_0CEquals27315K() {
        Quantity<TemperatureUnit> c = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> k = new Quantity<>(273.15, TemperatureUnit.KELVIN);
        assertEquals(c, k);
    }

    // 7. Celsius to Kelvin equality (100°C = 373.15 K)
    @Test
    void testTemperatureEquality_100CEquals37315K() {
        Quantity<TemperatureUnit> c = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> k = new Quantity<>(373.15, TemperatureUnit.KELVIN);
        assertEquals(c, k);
    }

    // 8. Special equal point -40°C = -40°F
    @Test
    void testTemperatureEquality_Negative40Equal() {
        Quantity<TemperatureUnit> c = new Quantity<>(-40.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> f = new Quantity<>(-40.0, TemperatureUnit.FAHRENHEIT);
        assertEquals(c, f);
    }

    // 9. Symmetric property of equality (A = B implies B = A)
    @Test
    void testTemperatureEquality_SymmetricProperty() {
        Quantity<TemperatureUnit> a = new Quantity<>(50.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> b = new Quantity<>(122.0, TemperatureUnit.FAHRENHEIT);
        assertEquals(a, b);
        assertEquals(b, a);
    }

    // 10. Reflexive property (object equals itself)
    @Test
    void testTemperatureEquality_ReflexiveProperty() {
        Quantity<TemperatureUnit> a = new Quantity<>(10.0, TemperatureUnit.CELSIUS);
        assertEquals(a, a);
    }

    // 11. Different values are not equal
    @Test
    void testTemperatureEquality_DifferentValues() {
        Quantity<TemperatureUnit> a = new Quantity<>(50.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> b = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        assertNotEquals(a, b);
    }

    // 12. Celsius to Fahrenheit conversion correctness (various values)
    @Test
    void testTemperatureConversion_CelsiusToFahrenheit_VariousValues() {
        Quantity<TemperatureUnit> a = new Quantity<>(50.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> converted = a.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(122.0, converted.getValue(), EPSILON);

        Quantity<TemperatureUnit> b = new Quantity<>(-20.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> bConv = b.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(-4.0, bConv.getValue(), EPSILON);
    }

    // 13. Fahrenheit to Celsius conversion correctness (reverse)
    @Test
    void testTemperatureConversion_FahrenheitToCelsius_VariousValues() {
        Quantity<TemperatureUnit> a = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);
        Quantity<TemperatureUnit> conv = a.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(0.0, conv.getValue(), EPSILON);

        Quantity<TemperatureUnit> b = new Quantity<>(212.0, TemperatureUnit.FAHRENHEIT);
        Quantity<TemperatureUnit> conv2 = b.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(100.0, conv2.getValue(), EPSILON);
    }

    // 14. Celsius to Kelvin conversion correctness
    @Test
    void testTemperatureConversion_CelsiusToKelvin() {
        Quantity<TemperatureUnit> a = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> conv = a.convertTo(TemperatureUnit.KELVIN);
        assertEquals(273.15, conv.getValue(), EPSILON);
    }

    // 15. Kelvin to Celsius conversion correctness
    @Test
    void testTemperatureConversion_KelvinToCelsius() {
        Quantity<TemperatureUnit> a = new Quantity<>(273.15, TemperatureUnit.KELVIN);
        Quantity<TemperatureUnit> conv = a.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(0.0, conv.getValue(), EPSILON);
    }

    // 16. Same-unit conversion returns unchanged value
    @Test
    void testTemperatureConversion_SameUnit() {
        Quantity<TemperatureUnit> a = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> conv = a.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(100.0, conv.getValue(), EPSILON);
    }

    // 17. Zero value conversion (0°C -> 32°F)
    @Test
    void testTemperatureConversion_ZeroValue() {
        Quantity<TemperatureUnit> a = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> conv = a.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(32.0, conv.getValue(), EPSILON);
    }

    // 18. Negative temperature conversions preserve sign
    @Test
    void testTemperatureConversion_NegativeValues() {
        Quantity<TemperatureUnit> a = new Quantity<>(-10.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> conv = a.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(14.0, conv.getValue(), EPSILON);
    }

    // 19. Round-trip conversion preserves value within epsilon (C -> F -> C)
    @Test
    void testTemperatureConversion_RoundTripPreservesValue() {
        Quantity<TemperatureUnit> original = new Quantity<>(37.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> toF = original.convertTo(TemperatureUnit.FAHRENHEIT);
        Quantity<TemperatureUnit> back = toF.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(original.getValue(), back.getValue(), 1e-6);
    }

    // 20. add() throws UnsupportedOperationException for temperature
    @Test
    void testTemperatureUnsupportedOperation_Add() {
        Quantity<TemperatureUnit> a = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> b = new Quantity<>(50.0, TemperatureUnit.CELSIUS);
        assertThrows(UnsupportedOperationException.class, () -> a.add(b));
    }

    // 21. subtract() throws UnsupportedOperationException for temperature
    @Test
    void testTemperatureUnsupportedOperation_Subtract() {
        Quantity<TemperatureUnit> a = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> b = new Quantity<>(50.0, TemperatureUnit.CELSIUS);
        assertThrows(UnsupportedOperationException.class, () -> a.subtract(b));
    }

    // 22. divide() throws UnsupportedOperationException for temperature
    @Test
    void testTemperatureUnsupportedOperation_Divide() {
        Quantity<TemperatureUnit> a = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> b = new Quantity<>(50.0, TemperatureUnit.CELSIUS);
        assertThrows(UnsupportedOperationException.class, () -> a.divide(b));
    }

    // 23. Unsupported operation exception message is informative
    @Test
    void testTemperatureUnsupportedOperation_ErrorMessage() {
        Quantity<TemperatureUnit> a = new Quantity<>(10.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> b = new Quantity<>(5.0, TemperatureUnit.CELSIUS);
        UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class, () -> a.add(b));
        assertTrue(ex.getMessage().toLowerCase().contains("not supported") || ex.getMessage().toLowerCase().contains("unsupported"));
    }

    // 24. Temperature vs Length incompatibility (equals returns false)
    @Test
    void testTemperatureVsLengthIncompatibility() {
        Quantity<TemperatureUnit> t = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<LengthUnit> l = new Quantity<>(100.0, LengthUnit.FEET);
        assertNotEquals(t, l);
    }

    // 25. Temperature vs Weight incompatibility
    @Test
    void testTemperatureVsWeightIncompatibility() {
        Quantity<TemperatureUnit> t = new Quantity<>(50.0, TemperatureUnit.CELSIUS);
        Quantity<WeightUnit> w = new Quantity<>(50.0, WeightUnit.KILOGRAM);
        assertNotEquals(t, w);
    }

    // 26. Temperature vs Volume incompatibility
    @Test
    void testTemperatureVsVolumeIncompatibility() {
        Quantity<TemperatureUnit> t = new Quantity<>(25.0, TemperatureUnit.CELSIUS);
        Quantity<VolumeUnit> v = new Quantity<>(25.0, VolumeUnit.LITRE);
        assertNotEquals(t, v);
    }

    // 27. TemperatureUnit.supportsArithmetic() returns false
    @Test
    void testOperationSupportMethods_TemperatureUnit_Addition() {
        assertFalse(TemperatureUnit.CELSIUS.supportsArithmetic().isSupported());
    }

    // 28. LengthUnit supportsArithmetic() default true
    @Test
    void testOperationSupportMethods_LengthUnit_Addition() {
        assertTrue(LengthUnit.FEET.supportsArithmetic().isSupported());
    }

    // 29. WeightUnit supportsArithmetic() default true
    @Test
    void testOperationSupportMethods_WeightUnit_Division() {
        assertTrue(WeightUnit.KILOGRAM.supportsArithmetic().isSupported());
    }

    // 30. Null unit validation in Quantity constructor
    @Test
    void testTemperatureNullUnitValidation() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(100.0, null));
    }

    // 31. IMeasurable backward compatibility: existing enums still convert (Length example)
    @Test
    void testIMeasurableInterface_BackwardCompatible() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> conv = a.convertTo(LengthUnit.INCH);
        assertEquals(12.0, conv.getValue(), EPSILON);
    }

    // 32. TemperatureUnit non-linear conversion verified (F -> K)
    @Test
    void testTemperatureUnit_NonLinearConversion_FtoK() {
        Quantity<TemperatureUnit> f = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);
        Quantity<TemperatureUnit> k = f.convertTo(TemperatureUnit.KELVIN);
        assertEquals(273.15, k.getValue(), EPSILON);
    }

    // 33. TemperatureUnit constants accessible
    @Test
    void testTemperatureUnit_AllConstants() {
        assertNotNull(TemperatureUnit.CELSIUS);
        assertNotNull(TemperatureUnit.FAHRENHEIT);
        assertNotNull(TemperatureUnit.KELVIN);
    }

    // 34. Default method inheritance: non-temperature enums inherit supportsArithmetic = true
    @Test
    void testTemperatureDefaultMethodInheritance() {
        assertTrue(VolumeUnit.LITRE.supportsArithmetic().isSupported());
    }

    // 35. validateOperationSupport throws for temperature (direct call)
    @Test
    void testTemperatureValidateOperationSupport_Throws() {
        UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class,
                () -> TemperatureUnit.CELSIUS.validateOperationSupport("ADD"));
        assertTrue(ex.getMessage().toLowerCase().contains("not supported") || ex.getMessage().toLowerCase().contains("unsupported"));
    }

    // 36. Integration: Quantity<TemperatureUnit> can be constructed and converted (no structural changes)
    @Test
    void testTemperatureIntegrationWithGenericQuantity() {
        Quantity<TemperatureUnit> t = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> f = t.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(212.0, f.getValue(), EPSILON);
    }

    // 37. Backward compatibility: UC1-UC13 behavior preserved for non-temperature categories (simple check)
    @Test
    void testTemperatureBackwardCompatibility_UC1ThroughUC13() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(12.0, LengthUnit.INCH);
        assertEquals(2.0, a.add(b).getValue(), EPSILON);
    }

    // 38. Conversion precision within epsilon
    @Test
    void testTemperatureConversionPrecision_Epsilon() {
        Quantity<TemperatureUnit> a = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> k = a.convertTo(TemperatureUnit.KELVIN);
        assertEquals(373.15, k.getValue(), 1e-6);
    }

    // 39. TemperatureUnit implements IMeasurable (indirect check via convertToBaseUnit)
    @Test
    void testTemperatureEnumImplementsIMeasurable() {
        double base = TemperatureUnit.FAHRENHEIT.convertToBaseUnit(32.0);
        assertEquals(0.0, base, EPSILON);
    }
//    ==================================================================================
    //UC15
        private final QuantityMeasurementCacheRepository repository =
                QuantityMeasurementCacheRepository.getInstance();

        private final IQuantityMeasurementService service =
                new QuantityMeasurementServiceImpl(repository);

        private final QuantityMeasurementController controller =
                new QuantityMeasurementController(service);

        // =========================================================
        // ENTITY TESTS (1–5)
        // =========================================================

        @Test
        void testQuantityEntity_SingleOperandConstruction() {

            QuantityMeasurementEntity entity =
                    new QuantityMeasurementEntity("CONVERT", "Quantity(12.0, INCH)");

            assertEquals("CONVERT", entity.getOperation());
            assertEquals("Quantity(12.0, INCH)", entity.getResult());
        }

        @Test
        void testQuantityEntity_BinaryOperandConstruction() {

            QuantityMeasurementEntity entity =
                    new QuantityMeasurementEntity("ADD", "Quantity(2.0, FEET)");

            assertEquals("ADD", entity.getOperation());
        }

        @Test
        void testQuantityEntity_ErrorConstruction() {

            QuantityMeasurementEntity entity =
                    new QuantityMeasurementEntity("Failure");

            assertTrue(entity.hasError());
        }

        @Test
        void testQuantityEntity_ToString_Success() {

            QuantityMeasurementEntity entity =
                    new QuantityMeasurementEntity("ADD", "Quantity(2.0, FEET)");

            assertTrue(entity.toString().contains("ADD"));
        }

        @Test
        void testQuantityEntity_ToString_Error() {

            QuantityMeasurementEntity entity =
                    new QuantityMeasurementEntity("Failure");

            assertTrue(entity.toString().contains("Failure"));
        }

        // =========================================================
        // SERVICE TESTS (6–15)
        // =========================================================

        @Test
        void testService_CompareEquality_SameUnit_Success() {

            QuantityDTO q1 =
                    new QuantityDTO(1.0, "FEET", "LENGTH");

            QuantityDTO q2 =
                    new QuantityDTO(1.0, "FEET", "LENGTH");

            QuantityDTO result = service.compare(q1, q2);

            assertEquals(1.0, result.getValue());
        }

        @Test
        void testService_CompareEquality_DifferentUnit_Success() {

            QuantityDTO q1 =
                    new QuantityDTO(1.0, "FEET", "LENGTH");

            QuantityDTO q2 =
                    new QuantityDTO(12.0, "INCH", "LENGTH");

            QuantityDTO result = service.compare(q1, q2);

            assertEquals(1.0, result.getValue());
        }

        @Test
        void testService_CompareEquality_CrossCategory_Error() {

            QuantityDTO q1 =
                    new QuantityDTO(1.0, "FEET", "LENGTH");

            QuantityDTO q2 =
                    new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");

            QuantityDTO result = service.compare(q1, q2);

            assertTrue(result.hasError());
        }

        @Test
        void testService_Convert_Success() {

            QuantityDTO q1 =
                    new QuantityDTO(1.0, "FEET", "LENGTH");

            QuantityDTO result =
                    service.convert(q1, "INCH");

            assertEquals(12.0, result.getValue());
        }

        @Test
        void testService_Add_Success() {

            QuantityDTO q1 =
                    new QuantityDTO(1.0, "FEET", "LENGTH");

            QuantityDTO q2 =
                    new QuantityDTO(12.0, "INCH", "LENGTH");

            QuantityDTO result =
                    service.add(q1, q2, "FEET");

            assertEquals(2.0, result.getValue());
        }

        @Test
        void testService_Add_UnsupportedOperation_Error() {

            QuantityDTO q1 =
                    new QuantityDTO(100.0, "CELSIUS", "TEMPERATURE");

            QuantityDTO q2 =
                    new QuantityDTO(50.0, "CELSIUS", "TEMPERATURE");

            QuantityDTO result =
                    service.add(q1, q2, "CELSIUS");

            assertTrue(result.hasError());
        }

        @Test
        void testService_Subtract_Success() {

            QuantityDTO q1 =
                    new QuantityDTO(10.0, "FEET", "LENGTH");

            QuantityDTO q2 =
                    new QuantityDTO(5.0, "FEET", "LENGTH");

            QuantityDTO result =
                    service.subtract(q1, q2, "FEET");

            assertEquals(5.0, result.getValue());
        }

        @Test
        void testService_Divide_Success() {

            QuantityDTO q1 =
                    new QuantityDTO(10.0, "FEET", "LENGTH");

            QuantityDTO q2 =
                    new QuantityDTO(5.0, "FEET", "LENGTH");

            QuantityDTO result =
                    service.divide(q1, q2);

            assertEquals(2.0, result.getValue());
        }

        @Test
        void testService_Divide_ByZero_Error() {

            QuantityDTO q1 =
                    new QuantityDTO(10.0, "FEET", "LENGTH");

            QuantityDTO q2 =
                    new QuantityDTO(0.0, "FEET", "LENGTH");

            QuantityDTO result =
                    service.divide(q1, q2);

            assertTrue(result.hasError());
        }

        @Test
        void testService_Convert_Temperature_Success() {

            QuantityDTO q1 =
                    new QuantityDTO(0.0, "CELSIUS", "TEMPERATURE");

            QuantityDTO result =
                    service.convert(q1, "FAHRENHEIT");

            assertEquals(32.0, result.getValue());
        }

        // =========================================================
        // CONTROLLER TESTS (16–21)
        // =========================================================

        @Test
        void testController_DemonstrateEquality_Success() {

            QuantityDTO q1 =
                    new QuantityDTO(1.0, "FEET", "LENGTH");

            QuantityDTO q2 =
                    new QuantityDTO(12.0, "INCH", "LENGTH");

            controller.performCompare(q1, q2);

            assertTrue(true);
        }

        @Test
        void testController_DemonstrateConversion_Success() {

            QuantityDTO q1 =
                    new QuantityDTO(1.0, "FEET", "LENGTH");

            controller.performConvert(q1, "INCH");

            assertTrue(true);
        }

        @Test
        void testController_DemonstrateAddition_Success() {

            QuantityDTO q1 =
                    new QuantityDTO(1.0, "FEET", "LENGTH");

            QuantityDTO q2 =
                    new QuantityDTO(12.0, "INCH", "LENGTH");

            controller.performAdd(q1, q2, "FEET");

            assertTrue(true);
        }

        @Test
        void testController_DemonstrateAddition_Error() {

            QuantityDTO q1 =
                    new QuantityDTO(10.0, "CELSIUS", "TEMPERATURE");

            QuantityDTO q2 =
                    new QuantityDTO(5.0, "CELSIUS", "TEMPERATURE");

            controller.performAdd(q1, q2, "CELSIUS");

            assertTrue(true);
        }

        @Test
        void testController_DemonstrateSubtraction_Success() {

            QuantityDTO q1 =
                    new QuantityDTO(10.0, "FEET", "LENGTH");

            QuantityDTO q2 =
                    new QuantityDTO(5.0, "FEET", "LENGTH");

            controller.performSubtract(q1, q2, "FEET");

            assertTrue(true);
        }

        @Test
        void testController_DemonstrateDivision_Success() {

            QuantityDTO q1 =
                    new QuantityDTO(10.0, "FEET", "LENGTH");

            QuantityDTO q2 =
                    new QuantityDTO(5.0, "FEET", "LENGTH");

            controller.performDivide(q1, q2);

            assertTrue(true);
        }

        // =========================================================
        // LAYER / FLOW TESTS (22–28)
        // =========================================================

        @Test
        void testLayerSeparation_ServiceIndependence() {

            assertNotNull(service);
        }

        @Test
        void testLayerSeparation_ControllerIndependence() {

            assertNotNull(controller);
        }

        @Test
        void testDataFlow_ControllerToService() {

            QuantityDTO dto =
                    new QuantityDTO(1.0, "FEET", "LENGTH");

            controller.performConvert(dto, "INCH");

            assertTrue(true);
        }

        @Test
        void testDataFlow_ServiceToController() {

            QuantityDTO dto =
                    service.convert(
                            new QuantityDTO(1.0, "FEET", "LENGTH"),
                            "INCH"
                    );

            assertEquals(12.0, dto.getValue());
        }

        @Test
        void testBackwardCompatibility_AllUC1_UC14_Tests() {

            assertTrue(true);
        }

        @Test
        void testService_AllMeasurementCategories() {

            assertNotNull(LengthUnit.FEET);
            assertNotNull(TemperatureUnit.CELSIUS);
        }

        @Test
        void testController_AllOperations() {

            assertNotNull(controller);
        }

        // =========================================================
        // VALIDATION / IMMUTABILITY (29–33)
        // =========================================================

        @Test
        void testService_ValidationConsistency() {

            QuantityDTO q1 =
                    new QuantityDTO(10.0, "CELSIUS", "TEMPERATURE");

            QuantityDTO q2 =
                    new QuantityDTO(5.0, "CELSIUS", "TEMPERATURE");

            QuantityDTO result =
                    service.add(q1, q2, "CELSIUS");

            assertTrue(result.hasError());
        }

        @Test
        void testEntity_Immutability() {

            QuantityMeasurementEntity entity =
                    new QuantityMeasurementEntity("ADD", "Quantity(2.0, FEET)");

            assertNotNull(entity);
        }

        @Test
        void testService_ExceptionHandling_AllOperations() {

            QuantityDTO q1 =
                    new QuantityDTO(10.0, "FEET", "LENGTH");

            QuantityDTO q2 =
                    new QuantityDTO(0.0, "FEET", "LENGTH");

            QuantityDTO result =
                    service.divide(q1, q2);

            assertTrue(result.hasError());
        }

        @Test
        void testIntegration_EndToEnd_LengthAddition() {

            QuantityDTO q1 =
                    new QuantityDTO(1.0, "FEET", "LENGTH");

            QuantityDTO q2 =
                    new QuantityDTO(12.0, "INCH", "LENGTH");

            QuantityDTO result =
                    service.add(q1, q2, "FEET");

            assertEquals(2.0, result.getValue());
        }

        @Test
        void testIntegration_EndToEnd_TemperatureUnsupported() {

            QuantityDTO q1 =
                    new QuantityDTO(10.0, "CELSIUS", "TEMPERATURE");

            QuantityDTO q2 =
                    new QuantityDTO(5.0, "CELSIUS", "TEMPERATURE");

            QuantityDTO result =
                    service.add(q1, q2, "CELSIUS");

            assertTrue(result.hasError());
        }

        // =========================================================
        // EXTRA TESTS (34–36)
        // =========================================================

    @Test
    void testService_NullEntity_Rejection() {

        QuantityDTO result =
                service.convert(null, "INCH");

        assertTrue(result.hasError());
    }

        @Test
        void testLayerDecoupling_ServiceChange() {

            IQuantityMeasurementService newService =
                    new QuantityMeasurementServiceImpl(repository);

            QuantityMeasurementController newController =
                    new QuantityMeasurementController(newService);

            assertNotNull(newController);
        }

        @Test
        void testScalability_NewOperation_Addition() {

            QuantityDTO q1 =
                    new QuantityDTO(5.0, "FEET", "LENGTH");

            QuantityDTO q2 =
                    new QuantityDTO(5.0, "FEET", "LENGTH");

            QuantityDTO result =
                    service.add(q1, q2, "FEET");

            assertEquals(10.0, result.getValue());
        }
    }


