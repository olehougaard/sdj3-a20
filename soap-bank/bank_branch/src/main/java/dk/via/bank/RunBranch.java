package dk.via.bank;

import java.net.URL;

import dk.via.bank.dao.CustomerDAO;
import dk.via.bank.dao.HeadQuarters;
import dk.via.bank.model.Customer;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

public class RunBranch {
	public static void main(String[] args) throws Exception {
		HeadQuarters hq = new HQClient();
		RemoteBranch branch = new RemoteBranch(1234, hq);
		Endpoint.publish("http://localhost:8090/branch", branch);
	}
}
