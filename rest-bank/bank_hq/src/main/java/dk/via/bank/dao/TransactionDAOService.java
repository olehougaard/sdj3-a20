package dk.via.bank.dao;

import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Money;
import dk.via.bank.model.parameters.TransactionSpecification;
import dk.via.bank.model.transaction.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/customers/{cpr}/accounts/{accountNumber}/transactions")
public class TransactionDAOService {
	private static final String DEPOSIT = "Deposit";
	private static final String TRANSFER = "Transfer";
	private static final String WITHDRAWAL = "Withdrawal";

	private DatabaseHelper<AbstractTransaction> helper;
	private AccountDAOService accounts;

	@SuppressWarnings("unused")
	public TransactionDAOService() {
		this(new AccountDAOService(), DatabaseHelper.JDBC_URL, DatabaseHelper.USERNAME, DatabaseHelper.PASSWORD);
	}

	public TransactionDAOService(AccountDAOService accounts, String jdbcURL, String username, String password) {
		this.accounts = accounts;
		this.helper = new DatabaseHelper<>(jdbcURL, username, password);
	}
	
	private class TransactionMapper implements DataMapper<AbstractTransaction> {
		private String cpr;
		private AccountNumber accountNumber;

		public TransactionMapper(String cpr, String accountString) {
			this.cpr = cpr;
			this.accountNumber = AccountNumber.fromString(accountString);
		}

		@Override
		public AbstractTransaction create(ResultSet rs) throws SQLException {
			Money amount = new Money(rs.getBigDecimal("amount"), rs.getString("currency"));
			String text = rs.getString("transaction_text");
			Account primary = readAccount(rs, "primary_reg_number", "primary_account_number", cpr);
			AbstractTransaction transaction = null;
			switch(rs.getString("transaction_type")) {
			case DEPOSIT:
				transaction = new DepositTransaction(amount, primary, text);
				break;
			case WITHDRAWAL:
				transaction = new WithdrawTransaction(amount, primary, text);
				break;
			case TRANSFER:
				Account secondaryAccount = readAccount(rs, "secondary_reg_number", "secondary_account_number", "%");
				transaction = new TransferTransaction(amount, primary, secondaryAccount, text);
			default:
			}
			if (transaction != null && !transaction.includes(accountNumber)) return null;
			return transaction;
		}

		private Account readAccount(ResultSet rs, String regNumberAttr, String acctNumberAttr, String cpr) throws SQLException {
			return accounts.getAccount(new AccountNumber(rs.getInt(regNumberAttr), rs.getInt(acctNumberAttr)), cpr);
		}
	}
	
	private class TransactionCreator implements TransactionVisitor {
		public int lastId;

		@Override
		public void visit(DepositTransaction transaction) {
			Money amount = transaction.getAmount();
			AccountNumber primaryAccount = transaction.getAccount().getAccountNumber();
			List<Integer> keys = helper.executeUpdateWithGeneratedKeys(
					"INSERT INTO Transaction(amount, currency, transaction_type, transaction_text, primary_reg_number, primary_account_number) VALUES (?, ?, ?, ?, ?, ?)",
					amount.getAmount(), amount.getCurrency(), DEPOSIT, transaction.getText(),
					primaryAccount.getRegNumber(), primaryAccount.getAccountNumber());
			lastId = keys.get(0);
		}

		@Override
		public void visit(WithdrawTransaction transaction) {
			Money amount = transaction.getAmount();
			AccountNumber primaryAccount = transaction.getAccount().getAccountNumber();
			List<Integer> keys = helper.executeUpdateWithGeneratedKeys(
					"INSERT INTO Transaction(amount, currency, transaction_type, transaction_text, primary_reg_number, primary_account_number) VALUES (?, ?, ?, ?, ?, ?)",
					amount.getAmount(), amount.getCurrency(), WITHDRAWAL, transaction.getText(),
					primaryAccount.getRegNumber(), primaryAccount.getAccountNumber());
			lastId = keys.get(0);
		}

		@Override
		public void visit(TransferTransaction transaction) {
			Money amount = transaction.getAmount();
			AccountNumber primaryAccount = transaction.getAccount().getAccountNumber();
			AccountNumber secondaryAccount = transaction.getRecipient().getAccountNumber();
			List<Integer> keys = helper.executeUpdateWithGeneratedKeys(
					"INSERT INTO Transaction(amount, currency, transaction_type, transaction_text, primary_reg_number, primary_account_number, secondary_reg_number, secondary_account_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
					amount.getAmount(), amount.getCurrency(), TRANSFER, transaction.getText(),
					primaryAccount.getRegNumber(), primaryAccount.getAccountNumber(),
					secondaryAccount.getRegNumber(), secondaryAccount.getAccountNumber());
			lastId = keys.get(0);
		}
	}
	
	private final TransactionCreator creator = new TransactionCreator();

	@POST
	@Produces("application/json")
	public Response createTransaction(@PathParam("cpr") String cpr, @PathParam("accountNumber") String accountString, TransactionSpecification transactionSpec) {
		Account account = accounts.getAccount(AccountNumber.fromString(accountString), cpr);
		if (account == null) return Response.status(404).build();
		Transaction transaction = transactionSpec.toTransaction(account);
		transaction.accept(creator);
		return Response.ok(TransactionSpecification.from(getTransaction(cpr, accountString, creator.lastId))).build();
	}

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response readTransaction(@PathParam("cpr") String cpr, @PathParam("accountNumber") String accountString, @PathParam("id") int transactionId) {
		AbstractTransaction transaction = getTransaction(cpr, accountString, transactionId);
		if (transaction == null) return Response.status(404).build();
		if (!transaction.includes(AccountNumber.fromString(accountString))) return Response.status(404).build();
		return Response.status(200).entity(TransactionSpecification.from(transaction)).build();
	}

	private AbstractTransaction getTransaction(String cpr, String accountString, int transactionId) {
		return helper.mapSingle(new TransactionMapper(cpr, accountString), "SELECT * FROM Transaction WHERE transaction_id = ?", transactionId);
	}

	@GET
	@Produces("application/json")
	public List<TransactionSpecification> readTransactionsFor(@PathParam("cpr") String cpr, @PathParam("accountNumber") String accountString) {
		AccountNumber accountNumber = AccountNumber.fromString(accountString);
		return helper.map(new TransactionMapper(cpr, accountString),
				"SELECT * FROM Transaction WHERE (primary_reg_number = ? AND primary_account_number = ?) OR (secondary_reg_number = ? AND secondary_account_number = ?)",
				accountNumber.getRegNumber(), accountNumber.getAccountNumber(),accountNumber.getRegNumber(), accountNumber.getAccountNumber())
				.stream()
				.map(TransactionSpecification::from)
				.collect(toList());
	}
}
