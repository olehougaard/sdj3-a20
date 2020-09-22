package dk.via.bank;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import dk.via.bank.dao.HeadQuarters;

public class RunBranch {
	public static void main(String[] args) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(1099);
		HeadQuarters hq = (HeadQuarters) registry.lookup("HQ");
		Registry branchRegistry = LocateRegistry.createRegistry(8099);
		RemoteBranch branch1 = new RemoteBranch(1234, hq);
		branchRegistry.rebind("Branch 1", branch1);
	}
}
