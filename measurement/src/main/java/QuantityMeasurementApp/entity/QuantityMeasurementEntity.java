package QuantityMeasurementApp.entity;
import java.io.Serializable;
public class QuantityMeasurementEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String operation;
    private final String result;
    private final boolean error;
    public QuantityMeasurementEntity(String operation, String result) {
        this.operation = operation;
        this.result = result;
        this.error = false;
    }
    public QuantityMeasurementEntity(String errorMessage) {
        this.operation = "ERROR";
        this.result = errorMessage;
        this.error = true;
    }
    public String getOperation() {
        return operation;
    }
    public String getResult() {
        return result;
    }
    public boolean hasError() {
        return error;
    }
    @Override
    public String toString() {
        if (error)
            return "ErrorEntity(" + result + ")";
        return "Entity(" + operation + ", " + result + ")";

    }
}