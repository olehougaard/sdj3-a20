package dk.via.cars.rs;

import dk.via.cars.CarDTO;
import dk.via.cars.MoneyDTO;

import javax.jws.WebMethod;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/cars")
public class CarsImpl {
	private CarDAO carDAO;

	public CarsImpl() {
		carDAO = new CarDAO();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CarDTO create(CarDTO car)  {
		return carDAO.create(car.getLicenseNumber(), car.getModel(), car.getYear(), car.getPrice());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public CarDTO[] readAll() {
		return carDAO.readAll();
	}

	@DELETE
	@Path("{license}")
	public void delete(@PathParam("license") String license_number) {
		carDAO.delete(license_number);
	}
}
