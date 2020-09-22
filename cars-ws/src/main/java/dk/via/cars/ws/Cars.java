package dk.via.cars.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import dk.via.cars.CarDTO;
import dk.via.cars.MoneyDTO;

@WebService
@SOAPBinding(style = Style.RPC, use = Use.ENCODED)
public interface Cars {
	@WebMethod CarDTO create(String licenseNo, String model, int year, MoneyDTO price);
	@WebMethod CarDTO[] readAll();
	@WebMethod void delete(String license_number);
}

