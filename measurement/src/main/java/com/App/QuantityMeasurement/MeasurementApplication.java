// MeasurementApplication.java
package com.App.QuantityMeasurement;

import com.App.QuantityMeasurement.controller.QuantityMeasurementController;
import com.App.QuantityMeasurement.dto.QuantityDTO;
import com.App.QuantityMeasurement.repository.QuantityMeasurementRepository;
import com.App.QuantityMeasurement.repositoryImpl.QuantityMeasurementDatabaseRepository;
import com.App.QuantityMeasurement.service.QuantityMeasurementService;
import com.App.QuantityMeasurement.serviceImpl.QuantityMeasurementServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeasurementApplication {

    private static final Logger logger =
            LoggerFactory.getLogger(MeasurementApplication.class);

    public static void main(String[] args) {
        logger.info("Starting Quantity Measurement Application");

        try {
            QuantityMeasurementRepository repository =
                    new QuantityMeasurementDatabaseRepository();

            QuantityMeasurementService service =
                    new QuantityMeasurementServiceImpl(repository);

            QuantityMeasurementController controller =
                    new QuantityMeasurementController(service);

            QuantityDTO result = controller.add(
                    1.0,
                    "FEET",
                    "LengthUnit",
                    12.0,
                    "INCH"
            );

            if (result.hasError()) {
                logger.error("Operation failed: {}", result.getErrorMessage());
            } else {
                logger.info("Result: {} {}",
                        result.getValue(),
                        result.getUnit());
            }

            logger.info("Database Count = {}",
                    repository.getTotalCount());

        } catch (Exception e) {
            logger.error("Application startup failed", e);
        }
    }
}