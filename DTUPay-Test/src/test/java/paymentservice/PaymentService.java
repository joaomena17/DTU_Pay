package paymentservice;

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
import paymentservice.Payment;

public class PaymentService {

    WebTarget baseUrl;

    public PaymentService() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8080/");
    }



    public boolean addPay(Payment payment) {
        return baseUrl.path("payments")
            .request()
            .post(Entity.entity(payment, MediaType.APPLICATION_JSON), Boolean.class);
    }

       public List<Payment> getList() {
        return baseUrl.path("payments")
                .request(MediaType.APPLICATION_JSON)
                .get(Response.class)
                .readEntity(new GenericType<>() {});
    }
    public Response addPaymentList(Payment payment) {
        return baseUrl.path("payments")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(payment, MediaType.APPLICATION_JSON));
    }
    public String registerUser(CreateUser data){
        return  baseUrl.path("payments/registerUser")
                .request()
                .post(Entity.entity(data, MediaType.APPLICATION_JSON), String.class);
    }
    public BigDecimal getBalance(String id) {
        return baseUrl.path("payments/getBalance/"+id)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(BigDecimal.class);
    }
    public void transfer(Transfer data){
        baseUrl.path("payments/transfer")
                .request()
                .post(Entity.entity(data, MediaType.APPLICATION_JSON));
    }
    public void deleteAccount(String id){
        baseUrl.path("payments/deleteAccount")
                .request()
                .post(Entity.entity(id, MediaType.APPLICATION_JSON));
    }

}
