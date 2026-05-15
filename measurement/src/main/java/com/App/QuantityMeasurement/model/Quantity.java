package com.App.QuantityMeasurement.model;

import com.App.QuantityMeasurement.enums.IMeasurable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

public class Quantity<U extends IMeasurable> {

    private static final Logger logger =
            LoggerFactory.getLogger(Quantity.class);

    private static final double EPSILON = 1e-6;
    private static final double SMALL_ROUND_THRESHOLD = 0.01;
    private static final int SMALL_ROUND_DECIMALS = 9;
    private static final int LARGE_ROUND_DECIMALS = 2;

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) {
            logger.error("Unit cannot be null");
            throw new IllegalArgumentException("Unit cannot be null");
        }

        if (Double.isNaN(value)) {
            logger.error("Value cannot be NaN");
            throw new IllegalArgumentException("Value cannot be NaN");
        }

        logger.debug("Created quantity: {} {}", value, unit);

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null) {
            logger.error("Target unit cannot be null");
            throw new IllegalArgumentException(
                    "Target unit cannot be null"
            );
        }

        if (!unit.getClass().equals(targetUnit.getClass())) {
            logger.error("Cross-category conversion not allowed");
            throw new IllegalArgumentException(
                    "Cross-category conversion not allowed"
            );
        }

        double base = unit.convertToBaseUnit(value);
        double converted =
                targetUnit.convertFromBaseUnit(base);

        logger.info("Converted {} {} to {} {}",
                value,
                unit,
                converted,
                targetUnit);

        return new Quantity<>(
                converted,
                targetUnit
        );
    }

    private enum ArithmeticOperation {
        ADD((a, b) -> a + b),

        SUBTRACT((a, b) -> a - b),

        DIVIDE((a, b) -> {
            if (Math.abs(b) < EPSILON) {
                throw new ArithmeticException(
                        "Division by zero"
                );
            }
            return a / b;
        });

        private final DoubleBinaryOperator op;

        ArithmeticOperation(
                DoubleBinaryOperator op) {
            this.op = op;
        }

        double compute(double a, double b) {
            return op.applyAsDouble(a, b);
        }
    }

    private void validateArithmeticOperands(
            Quantity<U> other,
            U targetUnit,
            boolean targetRequired,
            String operation) {

        if (other == null) {
            logger.error("Operand cannot be null");
            throw new IllegalArgumentException(
                    "Operand cannot be null"
            );
        }

        if (other.unit == null) {
            logger.error("Operand unit cannot be null");
            throw new IllegalArgumentException(
                    "Operand unit cannot be null"
            );
        }

        if (!unit.getClass()
                .equals(other.unit.getClass())) {
            logger.error(
                    "Cross-category operation not allowed"
            );
            throw new IllegalArgumentException(
                    "Cross-category operation not allowed"
            );
        }

        if (!Double.isFinite(this.value)
                || !Double.isFinite(other.value)) {
            logger.error("Values must be finite");
            throw new IllegalArgumentException(
                    "Values must be finite"
            );
        }

        if (targetRequired && targetUnit == null) {
            logger.error("Target unit required");
            throw new IllegalArgumentException(
                    "Target unit required"
            );
        }

        if (targetUnit != null
                && !unit.getClass()
                .equals(targetUnit.getClass())) {
            logger.error(
                    "Target unit must be same category"
            );
            throw new IllegalArgumentException(
                    "Target unit must be same category"
            );
        }

        unit.validateOperationSupport(operation);
        other.unit.validateOperationSupport(operation);

        if (targetUnit != null) {
            targetUnit.validateOperationSupport(
                    operation
            );
        }

        logger.debug("Validated operation: {}",
                operation);
    }

    private double performBaseArithmetic(
            Quantity<U> other,
            ArithmeticOperation op) {

        double base1 =
                unit.convertToBaseUnit(value);

        double base2 =
                other.unit.convertToBaseUnit(
                        other.value
                );

        return op.compute(base1, base2);
    }

    private double roundToNDecimals(
            double v,
            int n) {

        double factor = Math.pow(10.0, n);
        return Math.round(v * factor) / factor;
    }

    private double applyRoundingPolicy(
            double converted) {

        double abs = Math.abs(converted);

        if (abs >= SMALL_ROUND_THRESHOLD) {
            return roundToNDecimals(
                    converted,
                    LARGE_ROUND_DECIMALS
            );
        } else {
            return roundToNDecimals(
                    converted,
                    SMALL_ROUND_DECIMALS
            );
        }
    }

    public Quantity<U> add(Quantity<U> other) {
        validateArithmeticOperands(
                other,
                null,
                false,
                "ADD"
        );

        double baseResult =
                performBaseArithmetic(
                        other,
                        ArithmeticOperation.ADD
                );

        double converted =
                unit.convertFromBaseUnit(
                        baseResult
                );

        double rounded =
                applyRoundingPolicy(converted);

        logger.info("Added {} {} and {} {} = {} {}",
                this.value,
                this.unit,
                other.value,
                other.unit,
                rounded,
                this.unit);

        return new Quantity<>(
                rounded,
                unit
        );
    }

    public Quantity<U> add(
            Quantity<U> other,
            U targetUnit) {

        validateArithmeticOperands(
                other,
                targetUnit,
                true,
                "ADD"
        );

        double baseResult =
                performBaseArithmetic(
                        other,
                        ArithmeticOperation.ADD
                );

        double converted =
                targetUnit.convertFromBaseUnit(
                        baseResult
                );

        logger.info("Added {} {} and {} {} = {} {}",
                this.value,
                this.unit,
                other.value,
                other.unit,
                converted,
                targetUnit);

        return new Quantity<>(
                converted,
                targetUnit
        );
    }

    public Quantity<U> subtract(
            Quantity<U> other) {

        validateArithmeticOperands(
                other,
                null,
                false,
                "SUBTRACT"
        );

        double baseResult =
                performBaseArithmetic(
                        other,
                        ArithmeticOperation.SUBTRACT
                );

        double converted =
                unit.convertFromBaseUnit(
                        baseResult
                );

        double rounded =
                applyRoundingPolicy(converted);

        logger.info(
                "Subtracted {} {} from {} {} = {} {}",
                other.value,
                other.unit,
                this.value,
                this.unit,
                rounded,
                this.unit
        );

        return new Quantity<>(
                rounded,
                unit
        );
    }

    public Quantity<U> subtract(
            Quantity<U> other,
            U targetUnit) {

        validateArithmeticOperands(
                other,
                targetUnit,
                true,
                "SUBTRACT"
        );

        double baseResult =
                performBaseArithmetic(
                        other,
                        ArithmeticOperation.SUBTRACT
                );

        double converted =
                targetUnit.convertFromBaseUnit(
                        baseResult
                );

        logger.info(
                "Subtracted {} {} from {} {} = {} {}",
                other.value,
                other.unit,
                this.value,
                this.unit,
                converted,
                targetUnit
        );

        return new Quantity<>(
                converted,
                targetUnit
        );
    }

    public double divide(Quantity<U> other) {
        validateArithmeticOperands(
                other,
                null,
                false,
                "DIVIDE"
        );

        double result =
                performBaseArithmetic(
                        other,
                        ArithmeticOperation.DIVIDE
                );

        logger.info("Divided {} {} by {} {} = {}",
                this.value,
                this.unit,
                other.value,
                other.unit,
                result);

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            logger.debug(
                    "Comparing same Quantity instance"
            );
            return true;
        }

        if (!(obj instanceof Quantity<?>)) {
            return false;
        }

        Quantity<?> other = (Quantity<?>) obj;

        if (!unit.getClass()
                .equals(other.unit.getClass())) {
            return false;
        }

        double base1 =
                unit.convertToBaseUnit(value);

        double base2 =
                ((IMeasurable) other.unit)
                        .convertToBaseUnit(
                                other.value
                        );

        boolean result =
                Math.abs(base1 - base2) < EPSILON;

        logger.debug(
                "Equality check result: {}",
                result
        );

        return result;
    }

    @Override
    public int hashCode() {
        double base =
                unit.convertToBaseUnit(value);

        long bits =
                Double.doubleToLongBits(
                        roundToNDecimals(
                                base,
                                LARGE_ROUND_DECIMALS
                        )
                );

        return Objects.hash(
                unit.getClass(),
                bits
        );
    }

    @Override
    public String toString() {
        return "Quantity{" +
                "value=" + value +
                ", unit=" + unit +
                '}';
    }
}