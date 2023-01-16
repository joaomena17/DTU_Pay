package paymentservice;

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

    private PaymentFactory pf = new PaymentFactory();
    private PaymentService pService = pf.getService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Payment> getPayment() {
        return pService.getPaymentList();
    }

    @GET
    @Path("/getBalance/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBalance(@PathParam("id") String id) { return pService.getBalance(id); }

    @POST
    @Path("/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    public void transferMoney(Payment p) {
        pService.makePayment(p);
        //return pService.makePayment(p);
    }

}

