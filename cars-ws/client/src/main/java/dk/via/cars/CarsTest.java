package dk.via.cars;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import dk.via.cars.ws.Cars;

public class CarsTest {
	public static void main(String[] args) throws Exception {
		URL wsdl = new URL("http://localhost:8080/cars?wsdl");
		QName name = new QName("http://ws.cars.via.dk/", "CarsImplService");
		Service service = Service.create(wsdl, name);
		Cars cars = service.getPort(Cars.class);
		CarDTO[] allCars = cars.readAll();
		for(CarDTO car: allCars) {
			System.out.println(car.getLicenseNumber());
		}
	}
}
