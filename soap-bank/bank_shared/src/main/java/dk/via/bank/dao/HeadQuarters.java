package dk.via.bank.dao;

public interface HeadQuarters {
	ExchangeRateDAO getExchangeDAO();
	AccountDAO getAccountDAO();
	CustomerDAO getCustomerDAO();
	TransactionDAO getTransactionDAO();
}
