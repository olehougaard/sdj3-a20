package dk.via.web_service;

import dk.via.cars.CarDAO;
import dk.via.cars.ws.Cars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DAOConfig {
    @Bean
    @Scope("singleton")
    public Cars carDAO() {
        return new CarDAO();
    }

    @Bean(name="jdbcUrl")
    public String jdbcUrl() {
        return "jdbc:postgresql://localhost:5432/postgres?currentSchema=car_base";
    }

    @Bean(name="username")
    public String username() {
        return "postgres";
    }

    @Bean(name="password")
    public String password() {
        return "password";
    }
}
