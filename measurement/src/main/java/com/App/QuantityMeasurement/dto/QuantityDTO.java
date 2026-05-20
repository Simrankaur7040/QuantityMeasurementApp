package com.App.QuantityMeasurement.dto;

public class QuantityDTO {

    private double value;
    private String unit;
    private String measurementType;
    private boolean error;
    private String errorMessage;

    // Required by Jackson
    public QuantityDTO() {
    }

    // Normal constructor
    public QuantityDTO(double value, String unit, String measurementType) {
        this.value = value;
        this.unit = unit;
        this.measurementType = measurementType;
    }

    // Error constructor
    public QuantityDTO(boolean error, String errorMessage) {
        this.error = error;
        this.errorMessage = errorMessage;
    }

    // Getters and Setters
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}