package dk.via.bank;

import dk.via.bank.dao.AccountDAO;
import dk.via.bank.dao.CustomerDAO;
import dk.via.bank.dao.ExchangeRateDAO;
import dk.via.bank.dao.HeadQuarters;
import dk.via.bank.dao.TransactionDAO;

import javax.xml.ws.Endpoint;

public class RemoteHQ implements HeadQuarters {
	private static final long serialVersionUID = 1L;
	private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=bank";
	private static final String USERNAME = "postgres";
	private static final String PASSWORD = "password";

	private final ExchangeRateDAO exchangeDao;
	private final AccountDAO accountDAO;
	private final CustomerDAO customerDAO;
	private final TransactionDAO transactionDAO;

	public RemoteHQ(ExchangeRateDAO exchangeDao, AccountDAO accountDAO, CustomerDAO customerDAO, TransactionDAO transactionDAO) {
		this.exchangeDao = exchangeDao;
		this.accountDAO = accountDAO;
		this.customerDAO = customerDAO;
		this.transactionDAO = transactionDAO;
	}

	public void publish(String url) {
		Endpoint.publish(url + "customer", getCustomerDAO());
		Endpoint.publish(url + "account", getAccountDAO());
		Endpoint.publish(url + "transaction", getTransactionDAO());
		Endpoint.publish(url + "exchange-rate", getExchangeDAO());
	}

	@Override
	public ExchangeRateDAO getExchangeDAO() {
		return exchangeDao;
	}

	@Override
	public AccountDAO getAccountDAO() {
		return accountDAO;
	}

	@Override
	public CustomerDAO getCustomerDAO() {
		return customerDAO;
	}

	@Override
	public TransactionDAO getTransactionDAO() {
		return transactionDAO;
	}
}
