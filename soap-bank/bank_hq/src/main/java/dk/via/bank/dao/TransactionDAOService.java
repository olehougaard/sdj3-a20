package dk.via.bank.dao;

import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Money;
import dk.via.bank.model.transaction.*;

import javax.jws.WebService;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@WebService(endpointInterface = "dk.via.bank.dao.TransactionDAO", serviceName="TransactionService")
public class TransactionDAOService implements TransactionDAO {
	private static final String DEPOSIT = "Deposit";
	private static final String TRANSFER = "Transfer";
	private static final String WITHDRAWAL = "Withdrawal";

	private DatabaseHelper<AbstractTransaction> helper;
	private AccountDAO accounts;
	
	public TransactionDAOService(AccountDAO accounts, String jdbcURL, String username, String password) {
		this.accounts = accounts;
		this.helper = new DatabaseHelper<>(jdbcURL, username, password);
	}
	
	private class TransactionMapper implements DataMapper<AbstractTransaction> {
		@Override
		public AbstractTransaction create(ResultSet rs) throws SQLException {
			Money amount = new Money(rs.getBigDecimal("amount"), rs.getString("currency"));
			String text = rs.getString("transaction_text");
			Account primary = readAccount(rs, "primary_reg_number", "primary_account_number");
			switch(rs.getString("transaction_type")) {
			case DEPOSIT:
				return new DepositTransaction(amount, primary, text);
			case WITHDRAWAL:
				return new WithdrawTransaction(amount, primary, text);
			case TRANSFER:
				Account secondaryAccount = readAccount(rs, "secondary_reg_number", "secondary_account_number");
				return new TransferTransaction(amount, primary, secondaryAccount, text);
			default:
				return null;
			}
		}

		private Account readAccount(ResultSet rs, String regNumberAttr, String acctNumberAttr) throws SQLException {
			return accounts.readAccount(new AccountNumber(rs.getInt(regNumberAttr), rs.getLong(acctNumberAttr)));
		}
	}
	
	private class TransactionCreator implements TransactionVisitor {
		@Override
		public void visit(DepositTransaction transaction) {
			Money amount = transaction.getAmount();
			AccountNumber primaryAccount = transaction.getAccount().getAccountNumber();
			helper.executeUpdate("INSERT INTO Transaction(amount, currency, transaction_type, transaction_text, primary_reg_number, primary_account_number) VALUES (?, ?, ?, ?, ?, ?)", 
					amount.getAmount(), amount.getCurrency(), DEPOSIT, transaction.getText(), primaryAccount.getRegNumber(), primaryAccount.getAccountNumber());
		}

		@Override
		public void visit(WithdrawTransaction transaction) {
			Money amount = transaction.getAmount();
			AccountNumber primaryAccount = transaction.getAccount().getAccountNumber();
			helper.executeUpdate("INSERT INTO Transaction(amount, currency, transaction_type, transaction_text, primary_reg_number, primary_account_number) VALUES (?, ?, ?, ?, ?, ?)", 
					amount.getAmount(), amount.getCurrency(), WITHDRAWAL, transaction.getText(), primaryAccount.getRegNumber(), primaryAccount.getAccountNumber());
		}

		@Override
		public void visit(TransferTransaction transaction) {
			Money amount = transaction.getAmount();
			AccountNumber primaryAccount = transaction.getAccount().getAccountNumber();
			AccountNumber secondaryAccount = transaction.getRecipient().getAccountNumber();
			helper.executeUpdate("INSERT INTO Transaction(amount, currency, transaction_type, transaction_text, primary_reg_number, primary_account_number, secondary_reg_number, secondary_account_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", 
					amount.getAmount(), amount.getCurrency(), TRANSFER, transaction.getText(), primaryAccount.getRegNumber(), primaryAccount.getAccountNumber(), secondaryAccount.getRegNumber(), secondaryAccount.getAccountNumber());
		}
	}
	
	private final TransactionCreator creator = new TransactionCreator();
	
	public void createTransactions(AbstractTransaction transaction) {
		transaction.accept(creator);
	}

	public AbstractTransaction readTransaction(int transactionId) {
		return helper.mapSingle(new TransactionMapper(), "SELECT * FROM Transaction WHERE transaction_id = ?", transactionId);
	}

	public List<AbstractTransaction> readTransactionsFor(Account account) {
		AccountNumber accountNumber = account.getAccountNumber();
		return helper.map(new TransactionMapper(), 
				"SELECT * FROM Transaction WHERE (primary_reg_number = ? AND primary_account_number = ?) OR (secondary_reg_number = ? AND secondary_account_number = ?)",
				accountNumber.getRegNumber(), accountNumber.getAccountNumber(),accountNumber.getRegNumber(), accountNumber.getAccountNumber());
	}

	public void deleteTransactionsFor(Account account) {
		AccountNumber accountNumber = account.getAccountNumber();
		helper.executeUpdate("DELETE FROM Transaction WHERE (primary_reg_number = ? AND primary_account_number = ?) OR (secondary_reg_number = ? AND secondary_account_number = ?)",
				accountNumber.getRegNumber(), accountNumber.getAccountNumber(),accountNumber.getRegNumber(), accountNumber.getAccountNumber());
		
	}
}
