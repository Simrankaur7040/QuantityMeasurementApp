package com.App.QuantityMeasurement.controller;

import com.App.QuantityMeasurement.dto.QuantityDTO;
import com.App.QuantityMeasurement.dto.QuantityRequestDTO;
import com.App.QuantityMeasurement.service.QuantityMeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/quantities")
@CrossOrigin(origins = "http://localhost:5173")
public class QuantityMeasurementController {

    @Autowired
    private QuantityMeasurementService service;

    // ADD + CONVERT
    @PostMapping("/add")
    public QuantityDTO add(
            @RequestBody QuantityRequestDTO request) {

        if (request.getTargetUnit() != null
                && !request.getTargetUnit().isEmpty()) {

            return service.addAndConvert(
                    request.getThisQuantityDTO(),
                    request.getThatQuantityDTO(),
                    request.getTargetUnit()
            );
        }

        return service.add(
                request.getThisQuantityDTO(),
                request.getThatQuantityDTO()
        );
    }

    // SUBTRACT
    @PostMapping("/subtract")
    public QuantityDTO subtract(
            @RequestBody QuantityRequestDTO request) {

        return service.subtract(
                request.getThisQuantityDTO(),
                request.getThatQuantityDTO()
        );
    }

    // DIVIDE
    @PostMapping("/divide")
    public QuantityDTO divide(
            @RequestBody QuantityRequestDTO request) {

        return service.divide(
                request.getThisQuantityDTO(),
                request.getThatQuantityDTO()
        );
    }

    // COMPARE
    @PostMapping("/compare")
    public QuantityDTO compare(
            @RequestBody QuantityRequestDTO request) {

        return service.compareEquality(
                request.getThisQuantityDTO(),
                request.getThatQuantityDTO()
        );
    }

    // CONVERT
    @PostMapping("/convert")
    public QuantityDTO convert(
            @RequestBody QuantityRequestDTO request) {

        return service.convert(
                request.getThisQuantityDTO(),
                request.getTargetUnit()
        );
    }
}