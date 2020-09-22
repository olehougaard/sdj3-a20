package dk.via.bank.model;

public class Account {
	private AccountNumber accountNumber;
	private Money balance;

	public Account(AccountNumber accountNumber, String currency) {
		this(accountNumber, Money.zero(currency));
	}

	public Account(AccountNumber accountNumber, Money balance) {
		this.accountNumber = accountNumber;
		this.balance = balance;
	}

	public AccountNumber getAccountNumber() {
		return accountNumber;
	}

	public Money getBalance() {
		return balance;
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

	public void setBalance(Money balance) {
		this.balance = balance;
	}
}
