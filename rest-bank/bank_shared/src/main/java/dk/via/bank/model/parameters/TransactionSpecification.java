package dk.via.bank.model.parameters;

import dk.via.bank.model.Account;
import dk.via.bank.model.Money;
import dk.via.bank.model.transaction.AbstractTransaction;
import dk.via.bank.model.transaction.DepositTransaction;
import dk.via.bank.model.transaction.TransferTransaction;
import dk.via.bank.model.transaction.WithdrawTransaction;

public class TransactionSpecification {
    public static final String DEPOSIT = "deposit";
    public static final String WITHDRAW = "withdraw";
    public static final String TRANSFER = "transfer";

    private String type;
    private Money amount;
    private String text;
    private Account recipient;

    public TransactionSpecification() {
    }

    private TransactionSpecification(String type, Money amount, String text, Account recipient) {
        this.type = type;
        this.amount = amount;
        this.text = text;
        this.recipient = recipient;
    }

    public static TransactionSpecification deposit(Money amount, String text) {
        return new TransactionSpecification(DEPOSIT, amount, text, null);
    }

    public static TransactionSpecification withdraw(Money amount, String text) {
        return new TransactionSpecification(WITHDRAW, amount, text, null);
    }

    public static TransactionSpecification transfer(Money amount, String text, Account recipient) {
        return new TransactionSpecification(TRANSFER, amount, text, recipient);
    }

    public static TransactionSpecification from(AbstractTransaction transaction) {
        if (transaction instanceof DepositTransaction)
            return deposit(transaction.getAmount(), transaction.getText());
        else if (transaction instanceof WithdrawTransaction)
            return withdraw(transaction.getAmount(), transaction.getText());
        else if (transaction instanceof TransferTransaction)
            return transfer(transaction.getAmount(), transaction.getText(), ((TransferTransaction) transaction).getRecipient());
        else
            return null;
    }

    public AbstractTransaction toTransaction(Account account) {
        switch(type) {
            case DEPOSIT:
                return new DepositTransaction(amount, account, text);
            case WITHDRAW:
                return new WithdrawTransaction(amount, account, text);
            case TRANSFER:
                return new TransferTransaction(amount, account, recipient, text);
            default:
                throw new IllegalStateException("Not a valid transaction: " + type);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Account getRecipient() {
        return recipient;
    }

    public void setRecipient(Account recipient) {
        this.recipient = recipient;
    }
}
