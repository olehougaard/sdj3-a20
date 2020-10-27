package dk.via.cars;

import dk.via.cars.ws.Cars;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CarsStub implements Cars {
    private List<CarDTO> cars = new ArrayList<>();

    @Override
    public CarDTO create(String licenseNo, String model, int year, MoneyDTO price) {
        CarDTO car = new CarDTO();
        car.setLicenseNumber(licenseNo);
        car.setModel(model);
        car.setYear(year);
        car.setPrice(price);
        cars.add(car);
        return car;
    }

    @Override
    public List<CarDTO> readAll() {
        return new ArrayList<>(cars);
    }

    @Override
    public void delete(String license_number) {
        cars = cars.stream().filter(c -> !c.getLicenseNumber().equals(license_number)).collect(toList());
    }
}
