package com.App.QuantityMeasurement.dto;
import com.App.QuantityMeasurement.model.QuantityMeasurementEntity;
public class QuantityMeasurementDTO {

    private String operation;
    private String input;
    private String result;
    private boolean error;
    public QuantityMeasurementDTO() {
    }
    public QuantityMeasurementDTO(String operation,
                                  String input,
                                  String result,
                                  boolean error) {
        this.operation = operation;
        this.input = input;
        this.result = result;
        this.error = error;
    }
    public static QuantityMeasurementDTO fromEntity(QuantityMeasurementEntity
                                                            entity) {
        return new QuantityMeasurementDTO(
                entity.getOperation(),
                entity.getInput(),
                entity.getResult(),
                entity.isError()
        );
    }
    public String getOperation() {
        return operation;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }
    public String getInput() {
        return input;
    }
    public void setInput(String input) {
        this.input = input;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public boolean isError() {
        return error;
    }
    public void setError(boolean error) {
        this.error = error;
    }
}