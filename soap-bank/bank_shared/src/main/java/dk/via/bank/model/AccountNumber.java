package dk.via.bank.model;

public final class AccountNumber {
	private int regNumber;
	private long accountNumber;

	public AccountNumber(int regNumber, long accountNumber) {
		this.regNumber = regNumber;
		this.accountNumber = accountNumber;
	}

	public int getRegNumber() {
		return regNumber;
	}

	public long getAccountNumber() {
		return accountNumber;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(regNumber) ^ Long.hashCode(accountNumber);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AccountNumber)) {
			return false;
		}
		AccountNumber that = (AccountNumber) obj;
		return this.regNumber == that.regNumber && this.accountNumber == that.accountNumber;
	}
	
	public String toString() {
		return String.format("%s %010d", regNumber, accountNumber);
	}

	// JAX-WS
	public AccountNumber() {}

	public void setRegNumber(int regNumber) {
		this.regNumber = regNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
}
