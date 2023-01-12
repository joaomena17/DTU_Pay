package org.acme;

import dtu.ws.fastmoney.User;

import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/payments")
public class PaymentResource {
    private PaymentService pService = new PaymentService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Payment> getPaymentJson() {
        return pService.getPaymentList();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Payment> getPaymentXml() {
        return pService.getPaymentList();
    }

    @GET
    @Path("/getBalance/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBalanceJson(@PathParam("id") String id) { return pService.getBalance(id); }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean postPaymentJSON(Payment p) {
        return pService.addPayment(p);
    }

    @POST
    @Path("/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transferMoneyJson(Transfer data) {
        return pService.transferMoney(data);
    }


    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public boolean postPaymentXML(Payment p) {
        return pService.addPayment(p);
    }

    @POST
    @Path("/registerUser")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUserJson(CreateUser data) {
        return pService.registerUser(data);
    }
    @POST
    @Path("/deleteAccount")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAccountJson(String id) {
        return pService.deleteAccount(id);
    }


}

