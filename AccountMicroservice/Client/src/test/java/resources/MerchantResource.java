package resources;

import services.Merchant;
import services.DTUPayUser;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

public class MerchantResource {
    WebTarget baseUrl;
    public MerchantResource() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8080/");
    }

    private Response registerMerchantAccount(Merchant merchant,String mediaType){
        return  baseUrl.path("merchant/register")
                .request()
                .post(Entity.entity(merchant, MediaType.APPLICATION_JSON));
    }
    private Response unregisterMerchantAccount(String id,String mediaType){
        return  baseUrl.path("merchant/deregister")
                .request()
                .post(Entity.entity(id, MediaType.APPLICATION_JSON));
    }
}
