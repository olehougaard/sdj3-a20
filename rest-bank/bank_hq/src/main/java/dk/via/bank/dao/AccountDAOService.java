package dk.via.bank.dao;

import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Money;
import dk.via.bank.model.parameters.AccountSpecification;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Path("/customers/{cpr}/accounts")
public class AccountDAOService  {
	private DatabaseHelper<Account> helper;

	public AccountDAOService() {
		this(DatabaseHelper.JDBC_URL, DatabaseHelper.USERNAME, DatabaseHelper.PASSWORD);
	}
	
	public AccountDAOService(String jdbcURL, String username, String password) {
		helper = new DatabaseHelper<>(jdbcURL, username, password);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Account createAccount(@PathParam("cpr") String cpr, AccountSpecification specification) {
		int regNumber = specification.getRegNumber();
		String currency = specification.getCurrency();
		final List<Integer> keys = helper.executeUpdateWithGeneratedKeys("INSERT INTO Account(reg_number, customer, currency) VALUES (?, ?, ?)", 
				regNumber, cpr, currency);
		return getAccount(new AccountNumber(regNumber, keys.get(0)), cpr);
	}
	
	public static class AccountMapper implements DataMapper<Account>{
		@Override
		public Account create(ResultSet rs) throws SQLException {
			AccountNumber accountNumber = new AccountNumber(rs.getInt("reg_number"), rs.getLong("account_number"));
			BigDecimal balance = rs.getBigDecimal("balance");
			String currency = rs.getString("currency");
			String customerCpr = rs.getString("customer");
			return new Account(accountNumber, new Money(balance, currency), customerCpr);
		}
		
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public Collection<Account> readAccountsFor(@PathParam("cpr") String cpr) {
		return helper.map(new AccountMapper(), "SELECT * FROM Account WHERE customer = ? AND active", cpr) ;
	}

	@GET
	@Path("{accountNumber}")
	@Produces(MediaType.APPLICATION_JSON)
    public Response readAccount(@PathParam("cpr") String cpr, @PathParam("accountNumber") String accountString) {
		Account account = getAccount(AccountNumber.fromString(accountString), cpr);
		if (account == null)
			return Response.status(404).build();
		else
			return Response.status(200).entity(account).build();
	}

	Account getAccount(AccountNumber accountNumber, String targetCpr) {
		return helper.mapSingle(new AccountMapper(),
				"SELECT * FROM Account WHERE reg_number = ? AND account_number = ? AND customer LIKE ? AND active",
				accountNumber.getRegNumber(), accountNumber.getAccountNumber(), targetCpr);
	}

	@PUT
	@Path("{accountNumber}")
    public Response updateAccount(@PathParam("cpr") String cpr, @PathParam("accountNumber") String accountString, Account account) {
		AccountNumber accountNumber = AccountNumber.fromString(accountString);
		if (account.getAccountNumber() != null && !account.getAccountNumber().equals(accountNumber)) {
			return Response.status(409).build();
		}
		if (getAccount(accountNumber, cpr) == null) return Response.status(403).build();
		if (!account.getSettledCurrency().equals(account.getBalance().getCurrency())) return Response.status(400).build();
		helper.executeUpdate("UPDATE ACCOUNT SET balance = ?, currency = ? WHERE reg_number = ? AND account_number = ? AND active",
				account.getBalance().getAmount(), account.getSettledCurrency(), accountNumber.getRegNumber(), accountNumber.getAccountNumber());
		return Response.status(200).build();
	}

	@DELETE
	@Path("{accountNumber}")
    public void deleteAccount(@PathParam("cpr") String cpr, @PathParam("accountNumber") String accountString) {
		AccountNumber accountNumber = AccountNumber.fromString(accountString);
		helper.executeUpdate(
				"UPDATE ACCOUNT SET active = FALSE WHERE reg_number = ? AND account_number = ? AND customer = ?",
				accountNumber.getRegNumber(), accountNumber.getAccountNumber(), cpr);
	}
}
