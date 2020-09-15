package dk.via.car_base;

public class RemoteCar implements Car {
	private static final long serialVersionUID = 1L;
	private String model;
	private int year;
	private Money price;

	public RemoteCar(String model, int year, Money price) {
		this.model = model;
		this.year = year;
		this.price = price;
	}

	@Override
	public String getModel() {
		return model;
	}

	@Override
	public int getYear() {
		return year;
	}

	@Override
	public Money getPrice() {
		return price;
	}

	@Override
	public void setPrice(Money price) {
		this.price = price;
	}
}
