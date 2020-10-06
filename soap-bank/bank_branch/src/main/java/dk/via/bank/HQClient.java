package dk.via.bank;

import dk.via.bank.dao.*;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

public class HQClient implements HeadQuarters {
    private String baseURL;
    private ExchangeRateDAO exchangeRateDAO;
    private AccountDAO accountDAO;
    private CustomerDAO customerDAO;
    private TransactionDAO transactionDAO;

    public HQClient(String baseURL) {
        this.baseURL = baseURL;
    }

    private<T> T createServiceClient(String path, String serviceName, Class<T> targetClass) {
        try {
            URL wsdl = new URL(baseURL + path + "?wsdl");
            QName name = new QName("http://dao.bank.via.dk/", serviceName);
            Service service = Service.create(wsdl, name);
            return service.getPort(targetClass);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized ExchangeRateDAO getExchangeDAO() {
        if (exchangeRateDAO == null) {
            exchangeRateDAO = createServiceClient("exchange-rate", "ExchangeRateService", ExchangeRateDAO.class);
        }
        return exchangeRateDAO;
    }

    @Override
    public synchronized AccountDAO getAccountDAO() {
        if (accountDAO == null) {
            accountDAO = createServiceClient("account", "AccountService", AccountDAO.class);
        }
        return accountDAO;
    }

    @Override
    public synchronized CustomerDAO getCustomerDAO() {
        if (customerDAO == null) {
            customerDAO = createServiceClient("customer", "CustomerService", CustomerDAO.class);
        }
        return customerDAO;
    }

    @Override
    public synchronized TransactionDAO getTransactionDAO() {
        if (transactionDAO == null) {
            transactionDAO = createServiceClient("transaction", "TransactionService", TransactionDAO.class);
        }
        return transactionDAO;
    }
}
