package dk.via.bank.model.transaction;

import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Money;

public class TransferTransaction extends AbstractTransaction {
	private WithdrawTransaction withdrawTransaction;
	private DepositTransaction depositTransaction;
	
	public TransferTransaction(Money amount, Account account, Account recipient) {
		super(amount, account, "Transferred " + amount + " to " + recipient);
		this.withdrawTransaction = new WithdrawTransaction(amount, account, "Transferred " + amount + " to " + recipient);
		this.depositTransaction = new DepositTransaction(amount, recipient, "Transferred" + amount + "from " + recipient);
	}
	
	public TransferTransaction(Money amount, Account account, Account recipient, String text) {
		super(amount, account, text);
		this.withdrawTransaction = new WithdrawTransaction(amount, account, text);
		this.depositTransaction = new DepositTransaction(amount, recipient, text);
	}

	@Override
	public boolean includes(AccountNumber accountNumber) {
		return depositTransaction.includes(accountNumber) || withdrawTransaction.includes(accountNumber);
	}

	public Money getAmount() {
		return withdrawTransaction.getAmount();
	}

	public Account getAccount() {
		return withdrawTransaction.getAccount();
	}
	
	public Account getRecipient() {
		return depositTransaction.getAccount();
	}
	
	public WithdrawTransaction getWithdrawTransaction() {
		return withdrawTransaction;
	}

	public DepositTransaction getDepositTransaction() {
		return depositTransaction;
	}

	@Override
	public void accept(TransactionVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String getText() {
		return withdrawTransaction.getText();
	}

	// JAX-WS

	public TransferTransaction() {
	}

	public void setWithdrawTransaction(WithdrawTransaction withdrawTransaction) {
		this.withdrawTransaction = withdrawTransaction;
	}

	public void setDepositTransaction(DepositTransaction depositTransaction) {
		this.depositTransaction = depositTransaction;
	}
}
