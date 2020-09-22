package dk.via.bank.dao;

import java.rmi.Remote;
import java.rmi.RemoteException;

import dk.via.bank.model.ExchangeRate;

public interface ExchangeRateDAO extends Remote {
	ExchangeRate getExchangeRate(String fromCurrency, String toCurrency) throws RemoteException;
}
