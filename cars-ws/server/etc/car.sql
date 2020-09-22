CREATE TABLE Car (
  license_number VARCHAR(20) PRIMARY KEY,
  model VARCHAR(50) NOT NULL,
  year INT NOT NULL CHECK(year >= 1900),
  price_amount DECIMAL NOT NULL CHECK(price_amount > 0),
  price_currency CHAR(3) NOT NULL);

INSERT INTO car VALUES('AV 41 213', 'Ford', 2014, 100000, 'DKK');
