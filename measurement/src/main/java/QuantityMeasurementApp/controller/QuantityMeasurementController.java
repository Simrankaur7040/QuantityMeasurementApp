package QuantityMeasurementApp.controller;
import QuantityMeasurementApp.dto.QuantityDTO;
import QuantityMeasurementApp.service.IQuantityMeasurementService;
public class QuantityMeasurementController {
    private final IQuantityMeasurementService service;
    public QuantityMeasurementController(
            IQuantityMeasurementService service) {
        this.service = service;
    }

    public void performCompare(QuantityDTO q1, QuantityDTO q2) {
        displayResult(service.compare(q1, q2));
    }
    public void performConvert(QuantityDTO q1, String targetUnit) {
        displayResult(service.convert(q1, targetUnit));
    }
    public void performAdd(
            QuantityDTO q1,
            QuantityDTO q2,
            String targetUnit) {
        displayResult(service.add(q1, q2, targetUnit));
    }
    public void performSubtract(
            QuantityDTO q1,
            QuantityDTO q2,
            String targetUnit) {
        displayResult(service.subtract(q1, q2, targetUnit));
    }
    public void performDivide(QuantityDTO q1, QuantityDTO q2) {
        displayResult(service.divide(q1, q2));
    }
    private void displayResult(QuantityDTO dto) {
        System.out.println(dto);
    }
}