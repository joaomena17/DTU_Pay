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
/***
 * Author: Tiago Machado s222963
 */

public class MerchantPort {

    WebTarget baseUrl;
    Client client;

    public MerchantPort() {
        client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8080/merchants");
    }

    public String registerMerchant(DTUPayUser merchant)
            throws Exception {
        Response response = baseUrl.path("register").request()
                .post(Entity.entity(merchant, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 201) {
            throw new Exception(response.readEntity(String.class));
        }
        String merchantId = response.readEntity(String.class);

        response.close();
        return merchantId;
    }
    public Boolean unregisterMerchant(String merchantId)
            throws Exception {
        Response response = baseUrl.path("unregister").request()
                .post(Entity.entity(merchantId, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 201) {
            throw new Exception(response.readEntity(String.class));
        }
        boolean success = response.readEntity(Boolean.class);

        response.close();
        return success;
    }

    public List<PaymentReport> getMerchantReport(String merchantID) throws Exception {

        Response response = baseUrl.path("report").request().post(Entity.entity(merchantID, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 200) {
            String error = response.readEntity(String.class);
            throw new Exception(error);
        }
        List<PaymentReport> report= response.readEntity(new GenericType<>() {});
        response.close();
        return report;
    }

    public boolean pay(Payment payment) throws Exception {

        Response response = baseUrl.path("payments")
                .request()
                .post(Entity.entity(payment, MediaType.APPLICATION_JSON));
        if (response.getStatus() != 201) {
            String error = response.readEntity(String.class);
            throw new Exception(error);
        }
        boolean success= response.readEntity(Boolean.class);
        response.close();
        return success;
    }

}

