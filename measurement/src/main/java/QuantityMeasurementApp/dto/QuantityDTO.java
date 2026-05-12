package QuantityMeasurementApp.dto;
public class QuantityDTO {
    private double value;
    private String unit;
    private String measurementType;
    private boolean error;
    private String errorMessage;
    public QuantityDTO(double value, String unit, String measurementType) {
        this.value = value;
        this.unit = unit;
        this.measurementType = measurementType;
    }
    public static QuantityDTO error(String message) {
        QuantityDTO dto = new QuantityDTO(0, "ERROR", "ERROR");
        dto.error = true;
        dto.errorMessage = message;
        return dto;
    }
    public double getValue() {
        return value;
    }
    public String getUnit() {
        return unit;
    }
    public String getMeasurementType() {
        return measurementType;
    }
    public boolean hasError() {

        return error;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    @Override
    public String toString() {
        if (error)
            return "Error: " + errorMessage;
        return "Quantity(" + value + ", " + unit + ")";
    }
}
