package resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;

import entities.Payment;
import service.AccountService;
import service.PaymentService;

@Path("/merchants")
public class PaymentResource {

    private PaymentService service = new DTUPayFactory().getService().getPaymentService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void requestPayment(Payment payment){ service.requestPayment(payment); }
}
