package resources;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;

import java.math.BigDecimal;
import java.util.List;

import jakarta.ws.rs.core.Response;
import org.apache.http.util.EntityUtils;
import services.*;

public class PaymentResource {

    WebTarget baseUrl;

    public PaymentResource() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8080/");
    }

    public List<Payment> getList() {
        return baseUrl.path("payments")
                .request(MediaType.APPLICATION_JSON)
                .get(Response.class)
                .readEntity(new GenericType<>() {});
    }
    public BigDecimal getBalance(String id) {
        return baseUrl.path("payments/getBalance/"+id)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(BigDecimal.class);
    }
    public void transfer(Payment data){
        baseUrl.path("payments/transfer")
                .request()
                .post(Entity.entity(data, MediaType.APPLICATION_JSON));
    }

}

