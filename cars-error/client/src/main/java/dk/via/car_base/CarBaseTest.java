package dk.via.car_base;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CarBaseTest {
	private CarBase carBase;
	
	@Before
	public void setUp() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(1099);
		carBase = (RemoteCarBase) registry.lookup("CarBase");
	}

	@After
	public void tearDown() throws RemoteException {
		for(Car car: carBase.getAllCars()) {
			carBase.removeCar(car);
		}
	}
	
	@Test
	public void testRegisterCar() throws RemoteException {
		Money dkk = new Money(new BigDecimal(100000), "DKK");
		carBase.registerCar("Ford", 2014, dkk);
		List<Car> allCars = carBase.getAllCars();
		assertEquals(1, allCars.size());
		assertEquals(dkk, allCars.get(0).getPrice());
		assertEquals("Ford", allCars.get(0).getModel());
		assertEquals(2014, allCars.get(0).getYear());
	}

	@Test
	public void testSetPrice() throws RemoteException {
		Money dkk = new Money(new BigDecimal(100000), "DKK");
		Money eur = new Money(new BigDecimal("4999.99"), "EUR");
		Car car = carBase.registerCar("Ford", 2014, dkk);
		car.setPrice(eur);
		List<Car> allCars = carBase.getAllCars();
		assertEquals(1, allCars.size());
		assertEquals(eur, allCars.get(0).getPrice());
		assertEquals("Ford", allCars.get(0).getModel());
		assertEquals(2014, allCars.get(0).getYear());
	}
}
