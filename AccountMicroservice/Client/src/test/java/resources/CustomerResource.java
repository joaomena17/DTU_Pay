package resources;

import services.Customer;
import services.DTUPayUser;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

public class CustomerResource {
    WebTarget baseUrl;
    public CustomerResource() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8080/");
    }

    private Response registerCustomerAccount(Customer customer,String mediaType){
        return  baseUrl.path("customer/register")
                .request()
                .post(Entity.entity(customer, MediaType.APPLICATION_JSON));
    }
    private Response unregisterCustomerAccount(String id,String mediaType){
        return  baseUrl.path("customer/deregister")
                .request()
                .post(Entity.entity(id, MediaType.APPLICATION_JSON));
    }
}
