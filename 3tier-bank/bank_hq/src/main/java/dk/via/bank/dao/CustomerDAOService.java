package dk.via.bank.dao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import dk.via.bank.model.Account;
import dk.via.bank.model.Customer;

public class CustomerDAOService extends UnicastRemoteObject implements CustomerDAO {
	private static final long serialVersionUID = 1L;
	private DatabaseHelper<Customer> helper;
	private AccountDAO accountDAO;
	
	public CustomerDAOService(String jdbcURL, String username, String password, AccountDAO accountDAO) throws RemoteException {
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
	
	@Override
	public Customer create(String cpr, String name, String address) throws RemoteException {
		helper.executeUpdate("INSERT INTO Customer VALUES (?, ?, ?)", cpr, name, address);
		return new Customer(cpr, name, address);
	}

	@Override
	public Customer read(String cpr) throws RemoteException {
		CustomerMapper mapper = new CustomerMapper();
		Customer cust = helper.mapSingle(mapper, "SELECT * FROM Customer WHERE cpr = ?;", cpr);
		Collection<Account> accounts = accountDAO.readAccountsFor(cust);
		for(Account account: accounts) {
			cust.addAccount(account);
		}
		return cust;
	}

	@Override
	public void update(Customer customer) throws RemoteException {
		helper.executeUpdate("UPDATE Customer set name = ?, address = ? WHERE cpr = ?", customer.getName(), customer.getAddress(), customer.getCpr());
	}

	@Override
	public void delete(Customer customer) throws RemoteException {
		helper.executeUpdate("DELETE FROM Customer WHERE cpr = ?", customer.getCpr());
	}
}
