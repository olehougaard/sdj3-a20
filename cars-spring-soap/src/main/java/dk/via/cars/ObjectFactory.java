package dk.via.cars;

import dk.via.web_service.*;

import java.math.BigDecimal;
import java.util.List;

public class ObjectFactory {
    public static MoneyDTO createMoneyDTO(BigDecimal amount, String currency) {
        MoneyDTO money = new MoneyDTO();
        money.setAmount(amount);
        money.setCurrency(currency);
        return money;
    }

    public static CarDTO createCarDTO(String licenseNo, String model, int year, MoneyDTO price) {
        CarDTO car = new CarDTO();
        car.setLicenseNumber(licenseNo);
        car.setModel(model);
        car.setYear(year);
        car.setPrice(price);
        return car;
    }

    public static CreateResponse createCreateResponse(CarDTO car) {
        dk.via.web_service.ObjectFactory fac = new dk.via.web_service.ObjectFactory();
        fac.createCarDTO();
        CreateResponse response = new CreateResponse();
        response.setCar(car);
        return response;
    }

    public static ReadAllResponse createReadAllResponse(List<CarDTO> cars) {
        ReadAllResponse response = new ReadAllResponse();
        response.getCars().addAll(cars);
        return response;
    }

    public static DeleteResponse createDeleteResponse() {
        return new DeleteResponse();
    }
}
