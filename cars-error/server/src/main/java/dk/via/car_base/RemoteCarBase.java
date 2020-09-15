package dk.via.car_base;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

public class RemoteCarBase extends UnicastRemoteObject implements CarBase {
	private static final long serialVersionUID = 1L;

	public RemoteCarBase() throws RemoteException {
	}

	private LinkedList<RemoteCar> cars = new LinkedList<>();

	@Override
	public Car registerCar(String model, int year, Money price) {
		RemoteCar car = new RemoteCar(model, year, price);
		cars.add(car);
		return car;
	}

	@Override
	public List<Car> getAllCars() throws RemoteException {
		LinkedList<Car> list = new LinkedList<Car>();
		list.addAll(cars);
		return list;
	}
	
	private static boolean carsEqual(Car car1, Car car2) throws RemoteException {
		return car1.getModel().equals(car2.getModel()) && car1.getYear() == car2.getYear() && car1.getPrice().equals(car2.getPrice());
	}

	@Override
	public void removeCar(Car car) throws RemoteException {
		Car found = null;
		for(Car c: cars) 
			if (carsEqual(c, car))
				found = c;
		if (found != null) cars.remove(found);
	}
}
