package dk.via.cars;

import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;

public class CarsTest {
	public static void main(String[] args) throws Exception {
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		WebTarget target = client.target("http://localhost:8080/cars-rs-server");
		GenericType<ArrayList<CarDTO>> carDTOArrayListType = new GenericType<ArrayList<CarDTO>>() {
		};
		ArrayList<CarDTO> allCars = target.path("cars").request().accept(MediaType.APPLICATION_JSON).get(carDTOArrayListType);
		for(CarDTO car: allCars) {
			System.out.println(car.getLicenseNumber());
		}

		CarDTO newCar = new CarDTO("123456", "Tesla", 2017, new MoneyDTO(new BigDecimal(1000000), "EUR"));
		target.path("cars").request(MediaType.APPLICATION_JSON).post(Entity.json(newCar));
		allCars = target.path("cars").request().accept(MediaType.APPLICATION_JSON).get(carDTOArrayListType);
		for(CarDTO car: allCars) {
			System.out.println(car.getLicenseNumber());
		}

		target.path("cars").path("123456").request().delete();
		allCars = target.path("cars").request().accept(MediaType.APPLICATION_JSON).get(carDTOArrayListType);
		for(CarDTO car: allCars) {
			System.out.println(car.getLicenseNumber());
		}
	}
}
