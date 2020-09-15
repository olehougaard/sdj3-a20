package dk.via.car_base;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Car extends Remote {
	String getModel() throws RemoteException;
	int getYear() throws RemoteException;
	Money getPrice() throws RemoteException;
	void setPrice(Money price) throws RemoteException;
}
