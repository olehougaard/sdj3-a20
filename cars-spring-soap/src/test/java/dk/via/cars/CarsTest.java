package dk.via.cars;

import dk.via.web_service.CarDTO;
import dk.via.web_service.CarsImpl;
import dk.via.web_service.CreateRequest;
import dk.via.web_service.CreateResponse;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class CarsTest {
    private CarsImpl cars;

    @Before
    public void setUp() {
        cars = new CarsImpl(new CarsStub());
    }

    @Test
    public void createReturnsCar() {
        CreateRequest request = ObjectFactory.createCreateRequest("ABC", "Ford", 2020, ObjectFactory.createMoneyDTO(new BigDecimal(20000), "DKK"));
        CreateResponse response = cars.create(request);
        CarDTO car = response.getCar();
        assertEquals("ABC", car.getLicenseNumber());
    }
}
