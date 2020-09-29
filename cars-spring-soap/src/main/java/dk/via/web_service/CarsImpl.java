package dk.via.web_service;

import dk.via.cars.CarDAO;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class CarsImpl {
    private final CarDAO dao = new CarDAO();

    @PayloadRoot(namespace = WebServiceConfig.NAMESPACE_URI, localPart = "CreateRequest")
    @ResponsePayload
    public CreateResponse create(@RequestPayload CreateRequest request) {
        CarDTO car = dao.create(request.getLicenseNumber(), request.getModel(), request.getYear(), request.getPrice());
        CreateResponse response = new CreateResponse();
        response.setCar(car);
        return response;
    }

    @PayloadRoot(namespace = WebServiceConfig.NAMESPACE_URI, localPart = "ReadAllRequest")
    @ResponsePayload
    public ReadAllResponse readAll(@RequestPayload ReadAllRequest request) {
        ReadAllResponse response = new ReadAllResponse();
        response.getCars().addAll(dao.readAll());
        return response;
    }

    @PayloadRoot(namespace = WebServiceConfig.NAMESPACE_URI, localPart = "DeleteRequest")
    @ResponsePayload
    public DeleteResponse delete(@RequestPayload DeleteRequest request) {
        dao.delete(request.getLicenseNumber());
        return new DeleteResponse();
    }
}
