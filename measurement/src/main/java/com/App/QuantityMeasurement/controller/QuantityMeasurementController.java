package com.App.QuantityMeasurement.controller;
import com.App.QuantityMeasurement.dto.QuantityDTO;
import com.App.QuantityMeasurement.dto.QuantityInputDTO;
import com.App.QuantityMeasurement.service.QuantityMeasurementService;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/quantities")
public class QuantityMeasurementController {
    private final QuantityMeasurementService service;
    public QuantityMeasurementController(QuantityMeasurementService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public QuantityDTO add(@RequestBody QuantityInputDTO input) {
        return service.add(
                input.getThisQuantityDTO(),
                input.getThatQuantityDTO()
        );
    }
    @PostMapping("/subtract")
    public QuantityDTO subtract(@RequestBody QuantityInputDTO input) {
        return service.subtract(
                input.getThisQuantityDTO(),
                input.getThatQuantityDTO()
        );
    }
    @PostMapping("/divide")
    public QuantityDTO divide(@RequestBody QuantityInputDTO input) {
        return service.divide(
                input.getThisQuantityDTO(),
                input.getThatQuantityDTO()
        );
    }
    @PostMapping("/compare")
    public QuantityDTO compare(@RequestBody QuantityInputDTO input) {
        return service.compareEquality(
                input.getThisQuantityDTO(),
                input.getThatQuantityDTO()
        );
    }
    @PostMapping("/convert")
    public QuantityDTO convert(@RequestBody QuantityInputDTO input) {
        return service.convert(
                input.getThisQuantityDTO(),
                input.getThatQuantityDTO().getUnit()
        );
    }
}
