package dk.via.bank.dao;

import dk.via.bank.model.Account;
import dk.via.bank.model.Customer;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Path("/customers")
public class CustomerDAOService {
	private DatabaseHelper<Customer> helper;
	private AccountDAOService accountDAO;

	public CustomerDAOService() {
		this(DatabaseHelper.JDBC_URL, DatabaseHelper.USERNAME, DatabaseHelper.PASSWORD, new AccountDAOService());
	}
	
	public CustomerDAOService(String jdbcURL, String username, String password, AccountDAOService accountDAO) {
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

	@GET
	public Response readCustomers() {
		return Response.status(403).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCustomer(Customer customer) {
		helper.executeUpdate("INSERT INTO Customer VALUES (?, ?, ?)", customer.getCpr(), customer.getName(), customer.getAddress());
		return Response.status(201).entity(customer).build();
	}

	@GET
	@Path("{cpr}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readCustomer(@PathParam("cpr") String cpr) {
		CustomerMapper mapper = new CustomerMapper();
		Customer customer = helper.mapSingle(mapper, "SELECT * FROM Customer WHERE cpr = ?;", cpr);
		if (customer == null) {
			return Response.status(404).build();
		}
		return Response.status(200).entity(customer).build();
	}

	@PUT
	@Path("{cpr}")
	public Response updateCustomer(@PathParam("cpr") String cpr, Customer customer) {
		if (customer.getCpr() != null && !customer.getCpr().isEmpty() && !customer.getCpr().equals(cpr)) {
			return Response.status(409).build();
		} else if (helper.mapSingle(new CustomerMapper(), "SELECT * FROM Customer WHERE cpr = ?;", cpr) == null) {
			helper.executeUpdate("INSERT INTO Customer VALUES (?, ?, ?)", cpr, customer.getName(), customer.getAddress());
			return Response.status(201).build();
		} else {
			helper.executeUpdate("UPDATE Customer set name = ?, address = ? WHERE cpr = ?", customer.getName(), customer.getAddress(), cpr);
			return Response.status(200).build();
		}
	}

	@DELETE
	@Path("{cpr}")
	public void deleteCustomer(@PathParam("cpr") String cpr) {
		helper.executeUpdate("DELETE FROM Customer WHERE cpr = ?", cpr);
	}
}
