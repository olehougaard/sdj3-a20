package dk.via.bank;

import javax.xml.ws.Endpoint;
import java.net.URL;

public class RunBranch {
	public static void main(String[] args) throws Exception {
		RemoteBranch branch = new RemoteBranch(1234, new URL("http://localhost:8080/bank_hq"));
		Endpoint.publish("http://localhost:8090/branch", branch);
	}
}
