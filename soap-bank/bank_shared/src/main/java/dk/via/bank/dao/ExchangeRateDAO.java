package dk.via.bank.dao;

import dk.via.bank.model.ExchangeRate;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public interface ExchangeRateDAO {
	@WebMethod ExchangeRate getExchangeRate(String fromCurrency, String toCurrency);
}
