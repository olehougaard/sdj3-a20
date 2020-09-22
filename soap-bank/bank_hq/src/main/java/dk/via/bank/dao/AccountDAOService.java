package dk.via.bank.dao;

import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Customer;
import dk.via.bank.model.Money;

import javax.jws.WebService;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@WebService(endpointInterface = "dk.via.bank.dao.AccountDAO", serviceName="AccountService")
public class AccountDAOService implements AccountDAO {
	private DatabaseHelper<Account> helper;
	
	public AccountDAOService(String jdbcURL, String username, String password) {
		helper = new DatabaseHelper<>(jdbcURL, username, password);
	}

	@Override
	public Account createAccount(int regNumber, Customer customer, String currency) {
		final List<Integer> keys = helper.executeUpdateWithGeneratedKeys("INSERT INTO Account(reg_number, customer, currency) VALUES (?, ?, ?)", 
				regNumber, customer.getCpr(), currency);
		return readAccount(new AccountNumber(regNumber, keys.get(0)));
	}
	
	public static class AccountMapper implements DataMapper<Account>{
		@Override
		public Account create(ResultSet rs) throws SQLException {
			AccountNumber accountNumber = new AccountNumber(rs.getInt("reg_number"), rs.getLong("account_number"));
			BigDecimal balance = rs.getBigDecimal("balance");
			String currency = rs.getString("currency");
			return new Account(accountNumber, new Money(balance, currency));
		}
		
	}

    public Account readAccount(AccountNumber accountNumber) {
		return helper.mapSingle(new AccountMapper(), "SELECT * FROM Account WHERE reg_number = ? AND account_number = ? AND active", 
				accountNumber.getRegNumber(), accountNumber.getAccountNumber());
	}

    public Collection<Account> readAccountsFor(Customer customer) {
		return helper.map(new AccountMapper(), "SELECT * FROM Account WHERE customer = ? AND active", customer.getCpr()) ;
	}

    public void updateAccount(Account account) {
		helper.executeUpdate("UPDATE ACCOUNT SET balance = ?, currency = ? WHERE reg_number = ? AND account_number = ? AND active", 
				account.getBalance().getAmount(), account.getSettledCurrency(), account.getAccountNumber().getRegNumber(), account.getAccountNumber().getAccountNumber());
	}

    public void deleteAccount(Account account) {
		helper.executeUpdate("UPDATE ACCOUNT SET active = FALSE WHERE reg_number = ? AND account_number = ?", 
				account.getAccountNumber().getRegNumber(), account.getAccountNumber().getAccountNumber());
	}
}
