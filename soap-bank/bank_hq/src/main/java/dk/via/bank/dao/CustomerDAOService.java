package dk.via.bank.dao;

import dk.via.bank.model.Account;
import dk.via.bank.model.Customer;

import javax.jws.WebService;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@WebService(endpointInterface = "dk.via.bank.dao.CustomerDAO", serviceName="CustomerService")
public class CustomerDAOService implements CustomerDAO {
	private DatabaseHelper<Customer> helper;
	private AccountDAO accountDAO;
	
	public CustomerDAOService(String jdbcURL, String username, String password, AccountDAO accountDAO) {
		this.accountDAO = accountDAO;
		this.helper = new DatabaseHelper<>(jdbcURL, username, password);
	}
	
	private static class CustomerMapper implements DataMapper<Customer> {
		@Override
		public Customer create(ResultSet rs) throws SQLException {
			String cpr = rs.getString("cpr");
			String name = rs.getString("name");
			String address = rs.getString("address");
			return new Customer(cpr, name, address);
		}
	}
	
	public Customer createCustomer(String cpr, String name, String address) {
		helper.executeUpdate("INSERT INTO Customer VALUES (?, ?, ?)", cpr, name, address);
		return new Customer(cpr, name, address);
	}

	public Customer readCustomer(String cpr) {
		CustomerMapper mapper = new CustomerMapper();
		Customer customer = helper.mapSingle(mapper, "SELECT * FROM Customer WHERE cpr = ?;", cpr);
		Collection<Account> accounts = accountDAO.readAccountsFor(customer);
		for(Account account: accounts) {
			customer.addAccount(account);
		}
		return customer;
	}

	public void updateCustomer(Customer customer) {
		helper.executeUpdate("UPDATE Customer set name = ?, address = ? WHERE cpr = ?", customer.getName(), customer.getAddress(), customer.getCpr());
	}

	public void deleteCustomer(Customer customer) {
		helper.executeUpdate("DELETE FROM Customer WHERE cpr = ?", customer.getCpr());
	}
}
