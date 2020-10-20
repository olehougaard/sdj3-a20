package dk.via.bank.model.parameters;

public class AccountSpecification {
    private int regNumber;
    private String currency;

    public AccountSpecification() {
    }

    public AccountSpecification(int regNumber, String currency) {
        this.regNumber = regNumber;
        this.currency = currency;
    }

    public int getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(int regNumber) {
        this.regNumber = regNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
