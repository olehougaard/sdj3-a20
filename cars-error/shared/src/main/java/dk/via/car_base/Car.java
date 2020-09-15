package dk.via.car_base;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface Car extends Serializable {
	String getModel() throws RemoteException;
	int getYear() throws RemoteException;
	Money getPrice() throws RemoteException;
	void setPrice(Money price) throws RemoteException;
}
