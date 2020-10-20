package dk.via.bank.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Customer {
	private String cpr;
	private String name;
	private String address;

	public Customer(String cpr, String name, String address) {
		this.cpr = cpr;
		this.name = name;
		this.address = address;
	}

	public String getCpr() {
		return cpr;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public void move(String address) {
		this.address = address;
	}
	
	//JAX-WS
	public Customer() {}

	public void setCpr(String cpr) {
		this.cpr = cpr;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
