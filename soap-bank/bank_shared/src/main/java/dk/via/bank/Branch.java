package dk.via.bank;

import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Customer;
import dk.via.bank.model.Money;
import dk.via.bank.model.transaction.AbstractTransaction;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.Collection;
import java.util.List;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public interface Branch {
	@WebMethod Customer createCustomer(String cpr, String name, String address);
	@WebMethod Customer getCustomer(String cpr);
	@WebMethod Account createAccount(Customer customer, String currency);
	@WebMethod Account getAccount(AccountNumber accountNumber);
	@WebMethod void cancelAccount(Account account);
	@WebMethod Collection<Account> getAccounts(Customer customer);
	@WebMethod void execute(AbstractTransaction t);
	@WebMethod Money exchange(Money amount, String targetCurrency);
	@WebMethod List<AbstractTransaction> getTransactionsFor(Account account);
}
