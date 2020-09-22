package dk.via.bank.dao;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import dk.via.bank.model.ExchangeRate;

public class ExchangeRateDAOService extends UnicastRemoteObject implements ExchangeRateDAO {
	private static final long serialVersionUID = 1L;
	private String jdbcURL;
	private String username;
	private String password;

	public ExchangeRateDAOService(String jdbcURL, String username, String password) throws RemoteException {
		this.jdbcURL = jdbcURL;
		this.username = username;
		this.password = password;
	}

	@Override
	public ExchangeRate getExchangeRate(String fromCurrency, String toCurrency) throws RemoteException {
		DatabaseHelper<BigDecimal> helper = new DatabaseHelper<>(jdbcURL, username, password);
		BigDecimal rate = helper.mapSingle((rs)->rs.getBigDecimal(1), "SELECT rate FROM Exchange_rates WHERE from_currency = ? AND to_currency = ?", fromCurrency, toCurrency);
		return new ExchangeRate(fromCurrency, toCurrency, rate);
	}
}
