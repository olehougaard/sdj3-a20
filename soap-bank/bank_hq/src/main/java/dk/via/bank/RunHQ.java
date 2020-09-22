package dk.via.bank;

import javax.xml.ws.Endpoint;

public class RunHQ {
	public static void main(String[] args) throws Exception {
		RemoteHQ hq = new RemoteHQ();
		Endpoint.publish("http://localhost:8080/customer", hq.getCustomerDAO());
		Endpoint.publish("http://localhost:8080/account", hq.getAccountDAO());
		Endpoint.publish("http://localhost:8080/transaction", hq.getTransactionDAO());
		Endpoint.publish("http://localhost:8080/exchange-rate", hq.getExchangeDAO());
	}
}
