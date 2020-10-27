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
}
