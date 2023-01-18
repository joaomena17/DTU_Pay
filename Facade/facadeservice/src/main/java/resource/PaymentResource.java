package resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;

import entities.Payment;
import service.PaymentService;

@Path("/merchants")
public class PaymentResource {

    private PaymentService paymentservice = new DTUPayFactory().getService().getPaymentService();

    @POST
    @Path("/pay")
    @Consumes(MediaType.APPLICATION_JSON)
    public void requestPayment(Payment payment){ paymentservice.requestPayment(payment); }
}
