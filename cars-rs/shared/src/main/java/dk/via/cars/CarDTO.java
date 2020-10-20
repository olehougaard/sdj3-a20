package dk.via.cars;

import java.math.BigDecimal;

public class CarDTO {
	private String licenseNumber;
	private String model;
	private int year;
	private MoneyDTO price;

	public CarDTO() {
	}
	
	public CarDTO(String licenseNumber, String model, int year, MoneyDTO price) {
		this.licenseNumber = licenseNumber;
		this.model = model;
		this.year = year;
		this.price = price;
	}

	public CarDTO(String licenseNumber, String model, int year, BigDecimal amount, String currency) {
		this(licenseNumber, model, year, new MoneyDTO(amount, currency));
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public MoneyDTO getPrice() {
		return price;
	}

	public void setPrice(MoneyDTO price) {
		this.price = price;
	}
}
