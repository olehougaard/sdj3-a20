package dk.via.cars.rs;

import dk.via.cars.CarDTO;
import dk.via.cars.MoneyDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public class CarsImpl {
	private CarDAO carDAO;

	public CarsImpl() {
		carDAO = new CarDAO();
	}

	public CarDTO create(CarDTO car)  {
		return carDAO.create(car.getLicenseNumber(), car.getModel(), car.getYear(), car.getPrice());
	}

	public CarDTO[] readAll() {
		return carDAO.readAll();
	}

	public void delete(String license_number) {
		carDAO.delete(license_number);
	}
}
