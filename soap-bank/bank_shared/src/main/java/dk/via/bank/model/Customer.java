package dk.via.bank.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Customer {
	private String cpr;
	private String name;
	private String address;
	private ArrayList<Account> accounts;

	public Customer(String cpr, String name, String address) {
		this.cpr = cpr;
		this.name = name;
		this.address = address;
		this.accounts = new ArrayList<>();
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
	
	public void addAccount(Account account) {
		accounts.add(account);
	}

	public Account[] getAccounts() {
		return accounts.toArray(new Account[0]);
	}

	//JAX-WS
	public Customer() {this.accounts = new ArrayList<>();}

	public void setCpr(String cpr) {
		this.cpr = cpr;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setAccounts(Account[] accounts) {
		this.accounts = new ArrayList<>(Arrays.asList(accounts));
	}
}
