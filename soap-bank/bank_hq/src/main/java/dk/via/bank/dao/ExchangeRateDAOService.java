package dk.via.bank.dao;

import dk.via.bank.model.ExchangeRate;

import javax.jws.WebService;
import java.math.BigDecimal;

@WebService(endpointInterface = "dk.via.bank.dao.ExchangeRateDAO", serviceName="ExchangeRateService")
public class ExchangeRateDAOService implements ExchangeRateDAO {
	private String jdbcURL;
	private String username;
	private String password;

	public ExchangeRateDAOService(String jdbcURL, String username, String password) {
		this.jdbcURL = jdbcURL;
		this.username = username;
		this.password = password;
	}

	public ExchangeRate getExchangeRate(String fromCurrency, String toCurrency) {
		DatabaseHelper<BigDecimal> helper = new DatabaseHelper<>(jdbcURL, username, password);
		BigDecimal rate = helper.mapSingle((rs)->rs.getBigDecimal(1), "SELECT rate FROM Exchange_rates WHERE from_currency = ? AND to_currency = ?", fromCurrency, toCurrency);
		return new ExchangeRate(fromCurrency, toCurrency, rate);
	}
}
