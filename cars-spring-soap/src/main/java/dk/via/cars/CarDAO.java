package dk.via.cars;

import dk.via.cars.ws.Cars;
import dk.via.db.DataMapper;
import dk.via.db.DatabaseHelper;
import dk.via.web_service.CarDTO;
import dk.via.web_service.MoneyDTO;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CarDAO implements Cars {
	private DatabaseHelper<CarDTO> helper;

	@Resource(name="jdbcUrl")
	private String jdbcUrl;

	@Resource(name="username")
	private String username;

	@Resource(name="password")
	private String password;

	public CarDAO() {
	}

	private DatabaseHelper<CarDTO> helper() {
		if (helper == null)
			helper = new DatabaseHelper<>(jdbcUrl, username, password);
		return helper;
	}

	private static CarDTO createCarDTO(String licenseNo, String model, int year, MoneyDTO price) {
		CarDTO car = new CarDTO();
		car.setLicenseNumber(licenseNo);
		car.setModel(model);
		car.setYear(year);
		car.setPrice(price);
		return car;
	}

	public CarDTO create(String licenseNo, String model, int year, MoneyDTO price)  {
		helper().executeUpdate("INSERT INTO car VALUES (?, ?, ?, ?, ?)", licenseNo, model, year, price.getAmount(), price.getCurrency());
		return createCarDTO(licenseNo, model, year, price);
	}

	private static class CarMapper implements DataMapper<CarDTO> {
		public CarDTO create(ResultSet rs) throws SQLException {
			String licenseNumber = rs.getString("license_number");
			String model = rs.getString("model");
			int year = rs.getInt("year");
			BigDecimal priceAmount = rs.getBigDecimal("price_amount");
			String priceCurrency = rs.getString("price_currency");
			MoneyDTO price = new MoneyDTO();
			price.setAmount(priceAmount);
			price.setCurrency(priceCurrency);
			return createCarDTO(licenseNumber, model, year, price);
		}
	}
	
	public List<CarDTO> readAll() {
		return helper().map(new CarMapper(), "SELECT * FROM car");
	}
	
	public void delete(String license_number) {
		helper().executeUpdate("DELETE FROM car WHERE license_number = ?", license_number);
	}
}
