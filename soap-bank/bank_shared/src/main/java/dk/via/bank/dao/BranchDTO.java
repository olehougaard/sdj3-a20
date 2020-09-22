package dk.via.bank.dao;

public class BranchDTO {
	private int regNumber;
	private String address;

	public BranchDTO(int regNumber, String address) {
		this.regNumber = regNumber;
		this.address = address;
	}

	public int getRegNumber() {
		return regNumber;
	}

	public String getAddress() {
		return address;
	}
}
