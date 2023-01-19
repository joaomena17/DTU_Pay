package MobileApp;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import Entities.*;
import jakarta.ws.rs.core.Response;

public class ManagerPort {
    WebTarget baseUrl;
    Client client;

    public ManagerPort() {
        client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8080/manager");
    }
    public List<PaymentReport> getDTUPayReport() throws Exception {
        Response response = baseUrl.path("report").request().get();
        if (response.getStatus() != 200) {
            String error = response.readEntity(String.class);
            throw new Exception(error);
        }
        List<PaymentReport> report= response.readEntity(new GenericType<List<PaymentReport>>() {});

        response.close();

        return report;
    }
}
