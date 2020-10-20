package dk.via.bank.model.transaction;

import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Money;

import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({DepositTransaction.class, WithdrawTransaction.class, TransferTransaction.class})
public abstract class AbstractTransaction implements Transaction {
	private Money amount;
	private Account account;
	private String text;

	public AbstractTransaction(Money amount, Account account, String text) {
		this.amount = amount;
		this.account = account;
		this.text = text;
	}

	public Money getAmount() {
		return amount;
	}

	public Account getAccount() {
		return account;
	}

	public boolean includes(AccountNumber accountNumber) {
		return accountNumber.equals(account.getAccountNumber());
	}
	
	@Override
	public String getText() {
		return text;
	}

	// JAX-WS
	public AbstractTransaction() {}

	public void setAmount(Money amount) {
		this.amount = amount;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void setText(String text) {
		this.text = text;
	}
}