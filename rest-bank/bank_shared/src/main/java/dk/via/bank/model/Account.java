package dk.via.bank.model;

public class Account {
	private AccountNumber accountNumber;
	private Money balance;
	private String customerCpr;

	public Account(AccountNumber accountNumber, String currency, String customerCpr) {
		this(accountNumber, Money.zero(currency), customerCpr);
	}

	public Account(AccountNumber accountNumber, Money balance, String customerCpr) {
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.customerCpr = customerCpr;
	}

	public AccountNumber getAccountNumber() {
		return accountNumber;
	}

	public Money getBalance() {
		return balance;
	}

	public String getCustomerCpr() {
		return customerCpr;
	}

	public String getSettledCurrency() {
		return balance.getCurrency();
	}

	public synchronized void deposit(Money amount) {
		this.balance = balance.add(amount);
	}

	public synchronized void withdraw(Money amount) {
		this.balance = balance.subtract(amount);
	}

	//JAX-WS
	public Account() {}

	public void setAccountNumber(AccountNumber accountNumber) {
		this.accountNumber = accountNumber;
	}

	public void setCustomerCpr(String customerCpr) {
		this.customerCpr = customerCpr;
	}

	public void setBalance(Money balance) {
		this.balance = balance;
	}
}
