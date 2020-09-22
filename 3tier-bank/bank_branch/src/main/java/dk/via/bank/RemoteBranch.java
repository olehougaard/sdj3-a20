package dk.via.bank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.List;

import dk.via.bank.dao.AccountDAO;
import dk.via.bank.dao.CustomerDAO;
import dk.via.bank.dao.ExchangeRateDAO;
import dk.via.bank.dao.HeadQuarters;
import dk.via.bank.dao.TransactionDAO;
import dk.via.bank.model.*;
import dk.via.bank.model.transaction.*;

public class RemoteBranch extends UnicastRemoteObject implements Branch, TransactionVisitor {
	private static final long serialVersionUID = 1;
	private int regNumber;
	private AccountDAO accountDAO;
	private CustomerDAO customerDAO;
	private TransactionDAO transactionDAO;
	private ExchangeRateDAO exchangeDAO;
	
	public RemoteBranch(int regNumber, HeadQuarters hq) throws RemoteException {
		this.regNumber = regNumber;
		this.accountDAO = hq.getAccountDAO();
		this.customerDAO = hq.getCustomerDAO();
		this.transactionDAO = hq.getTransactionDAO();
		this.exchangeDAO = hq.getExchangeDAO();
	}

	@Override
	public Customer createCustomer(String cpr, String name, String address) throws RemoteException {
		return customerDAO.create(cpr, name, address);
	}

	@Override
	public Customer getCustomer(String cpr) throws RemoteException {
		return customerDAO.read(cpr);
	}

	@Override
	public Account createAccount(Customer customer, String currency) throws RemoteException {
		return accountDAO.create(regNumber, customer, currency);
	}

	@Override
	public Account getAccount(AccountNumber accountNumber) throws RemoteException {
		return accountDAO.read(accountNumber);
	}
	
	@Override
	public void cancelAccount(Account account) throws RemoteException {
		accountDAO.delete(account);
	}

	@Override
	public Collection<Account> getAccounts(Customer customer) throws RemoteException {
		return accountDAO.readAccountsFor(customer);
	}
	
	@Override
	public void execute(Transaction t) throws RemoteException {
		t.accept(this);
		transactionDAO.create(t);
	}
	
	private Money translateToSettledCurrency(Money amount, Account account) throws RemoteException {
		if (!amount.getCurrency().equals(account.getSettledCurrency())) {
			ExchangeRate rate = exchangeDAO.getExchangeRate(amount.getCurrency(), account.getSettledCurrency());
			amount = rate.exchange(amount);
		}
		return amount;
	}

	@Override
	public void visit(DepositTransaction transaction) throws RemoteException {
		Account account = transaction.getAccount();
		Money amount = transaction.getAmount();
		amount = translateToSettledCurrency(amount, account);
		account.deposit(amount);
		accountDAO.update(account);
	}
	
	@Override
	public void visit(WithdrawTransaction transaction) throws RemoteException {
		Account account = transaction.getAccount();
		Money amount = transaction.getAmount();
		amount = translateToSettledCurrency(amount, account);
		account.withdraw(amount);
		accountDAO.update(account);
	}
	
	@Override
	public void visit(TransferTransaction transaction) throws RemoteException {
		visit(transaction.getDepositTransaction());
		visit(transaction.getWithdrawTransaction());
	}
	
	@Override
	public Money exchange(Money amount, String targetCurrency) throws RemoteException {
		if (targetCurrency.equals(amount.getCurrency()))
			return amount;
		ExchangeRate rate = exchangeDAO.getExchangeRate(amount.getCurrency(), targetCurrency);
		return rate.exchange(amount);
	}
	
	@Override
	public List<Transaction> getTransactionsFor(Account primaryAccount) throws RemoteException {
		return transactionDAO.readAllFor(primaryAccount);
	}
}
