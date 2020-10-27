package dk.via.cars.ws;


import dk.via.cars.CarDTO;
import dk.via.cars.MoneyDTO;

import java.util.List;

public interface Cars {
	CarDTO create(String licenseNo, String model, int year, MoneyDTO price);
	List<CarDTO> readAll();
	void delete(String license_number);
}
