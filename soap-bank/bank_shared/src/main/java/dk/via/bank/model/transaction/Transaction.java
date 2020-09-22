package dk.via.bank.model.transaction;

public interface Transaction {
	String getText();
	void accept(TransactionVisitor visitor);
}
