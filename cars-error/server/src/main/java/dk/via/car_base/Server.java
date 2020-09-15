package dk.via.car_base;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
	public static void main(String[] args) throws RemoteException, AlreadyBoundException {
		RemoteCarBase carBase = new RemoteCarBase();
		CarBase skeleton = (CarBase) UnicastRemoteObject.exportObject(carBase, 8080);
		Registry registry = LocateRegistry.createRegistry(1099);
		registry.rebind("CarBase", skeleton);
	}
}
