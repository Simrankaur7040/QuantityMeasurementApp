// Replace QuantityMeasurementEntity.java with this exact code
// File: src/main/java/com/App/QuantityMeasurement/model/QuantityMeasurementEntity.java

package com.App.QuantityMeasurement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "quantity_measurements")
public class QuantityMeasurementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operation;

    @Column(length = 255)
    private String input;

    @Column(length = 255)
    private String result;

    private boolean error;

    public QuantityMeasurementEntity() {
    }

    public QuantityMeasurementEntity(
            Long id,
            String operation,
            String input,
            String result,
            boolean error) {
        this.id = id;
        this.operation = operation;
        this.input = input;
        this.result = result;
        this.error = error;
    }

    public QuantityMeasurementEntity(
            String operation,
            String input,
            String result,
            boolean error) {
        this.operation = operation;
        this.input = input;
        this.result = result;
        this.error = error;
    }

    // Constructor for error-only records
    public QuantityMeasurementEntity(
            String operation,
            String errorMessage) {
        this.operation = operation;
        this.input = errorMessage;
        this.result = null;
        this.error = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "QuantityMeasurementEntity{" +
                "id=" + id +
                ", operation='" + operation + '\'' +
                ", input='" + input + '\'' +
                ", result='" + result + '\'' +
                ", error=" + error +
                '}';
    }
}