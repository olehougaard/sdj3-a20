package dk.via.bank.dao;

import dk.via.bank.model.ExchangeRate;

import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Path("/exchange_rate")
public class ExchangeRateDAOService {
	private String jdbcURL;
	private String username;
	private String password;
	private ExchangeRateMapper mapper;
	private DatabaseHelper<ExchangeRate> helper;

	@SuppressWarnings("unused")
	public ExchangeRateDAOService() {
		this(DatabaseHelper.JDBC_URL, DatabaseHelper.USERNAME, DatabaseHelper.PASSWORD);
	}

	public ExchangeRateDAOService(String jdbcURL, String username, String password) {
		this.jdbcURL = jdbcURL;
		this.username = username;
		this.password = password;
		mapper = new ExchangeRateMapper();
		helper = new DatabaseHelper<>(jdbcURL, username, password);
	}

	private static class ExchangeRateMapper implements DataMapper<ExchangeRate> {
		@Override
		public ExchangeRate create(ResultSet rs) throws SQLException {
			return new ExchangeRate(rs.getString("from_currency"), rs.getString("to_currency"), rs.getBigDecimal("rate"));
		}
	}

	@GET
	@Produces("application/json")
	public Response getRates(@QueryParam("fromCurrency") String fromCurrency, @QueryParam("toCurrency") String toCurrency) {
		List<ExchangeRate> rates;
		if (fromCurrency != null && toCurrency != null)
			rates = helper.map(mapper, "SELECT * FROM Exchange_rates WHERE from_currency = ? AND to_currency = ?", fromCurrency, toCurrency);
		else if (fromCurrency != null)
			rates = helper.map(mapper, "SELECT * FROM Exchange_rates WHERE from_currency = ?", fromCurrency);
		else if (toCurrency != null)
			rates = helper.map(mapper, "SELECT * FROM Exchange_rates WHERE to_currency = ?", toCurrency);
		else
			rates = helper.map(mapper, "SELECT * FROM Exchange_rates");
		return Response.ok(rates).build();
	}

	@GET
	@Path("/{from}")
	@Produces("application/json")
	public Response getExchangeRate(@PathParam("from") String fromCurrency) {
		List<ExchangeRate> rate = helper.map(mapper, "SELECT * FROM Exchange_rates WHERE from_currency = ?", fromCurrency);
		return Response.ok(rate).build();
	}

	@GET
	@Path("/{from}/{to}")
	@Produces("application/json")
	public Response getExchangeRate(@PathParam("from") String fromCurrency, @PathParam("to") String toCurrency) {
		ExchangeRate rate = helper.mapSingle(mapper, "SELECT * FROM Exchange_rates WHERE from_currency = ? AND to_currency = ?", fromCurrency, toCurrency);
		if (rate == null) return Response.status(404).build();
		return Response.ok(rate).build();
	}
}
