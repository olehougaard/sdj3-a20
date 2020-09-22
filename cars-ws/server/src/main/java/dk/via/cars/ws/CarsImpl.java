package dk.via.cars.ws;

import dk.via.cars.CarDTO;
import dk.via.cars.MoneyDTO;

import javax.jws.WebService;

@WebService(endpointInterface = "dk.via.cars.ws.Cars")
public class CarsImpl implements Cars {
	private CarDAO carDAO;

	public CarsImpl() {
		carDAO = new CarDAO();
	}

	public CarDTO create(String licenseNo, String model, int year, MoneyDTO price)  {
		return carDAO.create(licenseNo, model, year, price);
	}

	public CarDTO[] readAll() {
		return carDAO.readAll();
	}
	
	public void delete(String license_number) {
		carDAO.delete(license_number);
	}
}
