package dk.via.bank;

import dk.via.bank.model.*;
import dk.via.bank.model.parameters.AccountSpecification;
import dk.via.bank.model.parameters.TransactionSpecification;
import dk.via.bank.model.transaction.*;

import javax.jws.WebService;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("ValidExternallyBoundObject")
@WebService(endpointInterface = "dk.via.bank.Branch")
public class RemoteBranch implements Branch {
	private int regNumber;
	private WebTarget target;

	public RemoteBranch(int regNumber, URL url) {
		this.regNumber = regNumber;
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		try {
			target = client.target(url.toURI());
		} catch (URISyntaxException e) {
			// Java is broken, flee.
			System.exit(1);
		}
	}

	private WebTarget path(Object ...pathSpec) {
		WebTarget path = target;
		for(Object element: pathSpec) {
			path = path.path(element.toString());
		}
		return path;
	}

	private <T> T get(Class<T> clazz, Object... pathSpec) {
		return path(pathSpec)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get(clazz);
	}

	private <T> T get(GenericType<T> type, Object... pathSpec) {
		return path(pathSpec)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get(type);
	}

	private <T> T post(Object params, Class<T> clazz, Object... pathSpec) {
	return path(pathSpec)
			.request(MediaType.APPLICATION_JSON)
			.post(Entity.json(params))
			.readEntity(clazz);
	}

	@Override
	public Customer createCustomer(String cpr, String name, String address) {
		return post(new Customer(cpr, name, address), Customer.class, "customers");
	}

	@Override
	public Customer getCustomer(String cpr) {
		return get(Customer.class, "customers", cpr);
	}

	@Override
	public Account createAccount(Customer customer, String currency) {
		return post(new AccountSpecification(regNumber, currency), Account.class, "customers", customer.getCpr(), "accounts");
	}

	@Override
	public Account getAccount(Customer customer, AccountNumber accountNumber) {
		return get(Account.class, "customers", customer.getCpr(), "accounts", accountNumber);
	}
	
	@Override
	public void cancelAccount(Account account) {
		path("customers", account.getCustomerCpr(), "accounts", account.getAccountNumber())
				.request()
				.delete();
	}

	@Override
	public Collection<Account> getAccounts(Customer customer) {
		return path("customers", customer.getCpr(), "accounts")
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get(new GenericType<ArrayList<Account>>() {});
	}

	private class BranchTransactionVisitor implements TransactionVisitor {
		private void updateAccount(Account account) {
			path("customers", account.getCustomerCpr(), "accounts", account.getAccountNumber())
					.request(MediaType.APPLICATION_JSON)
					.put(Entity.json(account));
		}

		@Override
		public void visit(DepositTransaction transaction) {
			Account account = transaction.getAccount();
			Money amount = transaction.getAmount();
			amount = translateToSettledCurrency(amount, account);
			account.deposit(amount);
			updateAccount(account);
		}

		@Override
		public void visit(WithdrawTransaction transaction) {
			Account account = transaction.getAccount();
			Money amount = transaction.getAmount();
			amount = translateToSettledCurrency(amount, account);
			account.withdraw(amount);
			updateAccount(account);

		}

		@Override
		public void visit(TransferTransaction transaction) {
			visit(transaction.getDepositTransaction());
			visit(transaction.getWithdrawTransaction());
		}
	}
	
	@Override
	public void execute(AbstractTransaction t) {
		t.accept(new BranchTransactionVisitor());
		path("customers", t.getAccount().getCustomerCpr(), "accounts", t.getAccount().getAccountNumber(), "transactions")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(TransactionSpecification.from(t)));
	}

	private ExchangeRate getExchangeRate(String fromCurrency, String toCurrency) {
		return get(ExchangeRate.class, "exchange_rate", fromCurrency, toCurrency);
	}

	private Money exchange(Money money, String fromCurrency, String toCurrency) {
			ExchangeRate rate = getExchangeRate(fromCurrency, toCurrency);
			return rate.exchange(money);
	}
	
	private Money translateToSettledCurrency(Money amount, Account account) {
		if (amount.getCurrency().equals(account.getSettledCurrency())) {
			return amount;
		} else {
			return exchange(amount, amount.getCurrency(), account.getSettledCurrency());
		}
	}

	@Override
	public Money exchange(Money amount, String targetCurrency) {
		if (targetCurrency.equals(amount.getCurrency()))
			return amount;
		return exchange(amount, amount.getCurrency(), targetCurrency);
	}
	
	@Override
	public List<AbstractTransaction> getTransactionsFor(Account primaryAccount) {
		return get(new GenericType<ArrayList<TransactionSpecification>>() {},
				"customers", primaryAccount.getCustomerCpr(), "accounts", primaryAccount.getAccountNumber(), "transactions")
				.stream()
				.map(spec -> spec.toTransaction(primaryAccount))
				.collect(toList());
	}
}
