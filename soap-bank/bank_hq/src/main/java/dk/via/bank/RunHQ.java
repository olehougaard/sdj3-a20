package dk.via.bank;

import dk.via.bank.dao.*;

public class RunHQ {
	private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=bank";
	private static final String USERNAME = "postgres";
	private static final String PASSWORD = "password";

	public static void main(String[] args) throws Exception {
		ExchangeRateDAO exchangeDao = new ExchangeRateDAOService(JDBC_URL, USERNAME, PASSWORD);
		AccountDAO accountDAO = new AccountDAOService(JDBC_URL, USERNAME, PASSWORD);
		CustomerDAO customerDAO = new CustomerDAOService(JDBC_URL, USERNAME, PASSWORD, accountDAO);
		TransactionDAO transactionDAO = new TransactionDAOService(accountDAO, JDBC_URL, USERNAME, PASSWORD);

		RemoteHQ hq = new RemoteHQ(exchangeDao, accountDAO, customerDAO, transactionDAO);

		hq.publish("http://localhost:8080/");
	}

}
