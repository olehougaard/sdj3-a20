package dk.via.cars.rs;

import dk.via.cars.CarDTO;
import dk.via.cars.MoneyDTO;
import dk.via.db.DataMapper;
import dk.via.db.DatabaseHelper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarDAO {
	private DatabaseHelper<CarDTO> helper;

	public CarDAO() {
		helper = new DatabaseHelper<>("jdbc:postgresql://localhost:5432/postgres?currentSchema=car_base", "postgres", "password");
	}

	public CarDTO create(String licenseNo, String model, int year, MoneyDTO price)  {
		helper.executeUpdate("INSERT INTO car VALUES (?, ?, ?, ?, ?)", licenseNo, model, year, price.getAmount(), price.getCurrency());
		return new CarDTO(licenseNo, model, year, price);
	}
	
	private static class CarMapper implements DataMapper<CarDTO> {
		public CarDTO create(ResultSet rs) throws SQLException {
			String licenseNumber = rs.getString("license_number");
			String model = rs.getString("model");
			int year = rs.getInt("year");
			BigDecimal priceAmount = rs.getBigDecimal("price_amount");
			String priceCurrency = rs.getString("price_currency");
			return new CarDTO(licenseNumber, model, year, priceAmount, priceCurrency);
		}
	}
	
	public CarDTO[] readAll() {
		return helper.map(new CarMapper(), "SELECT * FROM car").toArray(new CarDTO[0]);
	}
	
	public void delete(String license_number) {
		helper.executeUpdate("DELETE FROM car WHERE license_number = ?", license_number);
	}
}
