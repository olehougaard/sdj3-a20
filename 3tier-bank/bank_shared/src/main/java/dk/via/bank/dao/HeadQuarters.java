package dk.via.bank.dao;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HeadQuarters extends Remote {
	ExchangeRateDAO getExchangeDAO() throws RemoteException;
	AccountDAO getAccountDAO() throws RemoteException;
	CustomerDAO getCustomerDAO() throws RemoteException;
	TransactionDAO getTransactionDAO() throws RemoteException;
}
