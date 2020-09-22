package dk.via.bank.dao;

import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Customer;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.Collection;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public interface AccountDAO {
	@WebMethod Account createAccount(int regNumber, Customer customer, String currency);
    @WebMethod Account readAccount(AccountNumber accountNumber);
    @WebMethod Collection<Account> readAccountsFor(Customer customer);
    @WebMethod void updateAccount(Account account);
    @WebMethod void deleteAccount(Account account);
}
