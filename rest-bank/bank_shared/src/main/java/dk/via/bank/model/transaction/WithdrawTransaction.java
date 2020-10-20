package dk.via.bank.model.transaction;

import dk.via.bank.model.Account;
import dk.via.bank.model.Money;

public class WithdrawTransaction extends AbstractTransaction {
	public WithdrawTransaction(Money amount, Account account) {
		this(amount, account, "Withdrew " + amount);
	}

	public WithdrawTransaction(Money amount, Account account, String text) {
		super(amount, account, text);
	}


	@Override
	public void accept(TransactionVisitor visitor) {
		visitor.visit(this);
	}

	// JAX-WS

	public WithdrawTransaction() {
	}
}
