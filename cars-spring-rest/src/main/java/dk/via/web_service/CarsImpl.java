package dk.via.web_service;

import dk.via.cars.CarDTO;
import dk.via.cars.ws.Cars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarsImpl {
    private final Cars dao;

    @Autowired
    public CarsImpl(Cars dao) {
        this.dao = dao;
    }

    @GetMapping
    public List<CarDTO> readAll() {
        return dao.readAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO create(@RequestBody CarDTO car)  {
        try {
            return dao.create(car.getLicenseNumber(), car.getModel(), car.getYear(), car.getPrice());
        } catch(RuntimeException e) {
            if (e.getMessage().contains("duplicate key"))
                throw new DuplicateKeyException(e);
            else
                throw e;
        }
    }

    @DeleteMapping(value = "/{license}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("license") String license_number) {
        dao.delete(license_number);
    }
}
