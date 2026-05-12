package QuantityMeasurementApp.app;
import QuantityMeasurementApp.controller.QuantityMeasurementController;
import QuantityMeasurementApp.dto.QuantityDTO;
import QuantityMeasurementApp.repository.QuantityMeasurementCacheRepository;
import QuantityMeasurementApp.serviceImpl.QuantityMeasurementServiceImpl;
public class MeasurementApplication {

    public static void main(String[] args) {
        QuantityMeasurementCacheRepository repository =
                QuantityMeasurementCacheRepository.getInstance();
        QuantityMeasurementServiceImpl service =
                new QuantityMeasurementServiceImpl(repository);
        QuantityMeasurementController controller =
                new QuantityMeasurementController(service);
        QuantityDTO feet =
                new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO inches =
                new QuantityDTO(12.0, "INCH", "LENGTH");
        controller.performAdd(feet, inches, "FEET");
    }
}
