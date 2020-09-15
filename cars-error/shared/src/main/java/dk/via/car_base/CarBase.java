package dk.via.car_base;

import java.rmi.RemoteException;
import java.util.List;

public interface CarBase {
	Car registerCar(String model, int year, Money price);
	List<Car> getAllCars() throws RemoteException;
	void removeCar(Car car) throws RemoteException;
}
