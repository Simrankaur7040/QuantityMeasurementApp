package com.App.QuantityMeasurement.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(QuantityMeasurementException.class)
    public ResponseEntity<Map<String, Object>>
    handle(QuantityMeasurementException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", 400,
                        "error", "Quantity Measurement Error",
                        "message", ex.getMessage()
                ));
    }
}
