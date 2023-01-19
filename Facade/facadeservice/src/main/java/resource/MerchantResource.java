package resource;

import entities.DTUPayUser;
import entities.Payment;
import entities.PaymentReport;
import service.AccountService;
import service.PaymentService;
import service.ReportService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;



/***
 * Author: Tiago Machado s222963
 */
@Path("/merchants")
public class MerchantResource {

    private AccountService service = new DTUPayFactory().getService().getAccountService();

    private ReportService reportService = new DTUPayFactory().getService().getReportService();

    private PaymentService paymentservice = new DTUPayFactory().getService().getPaymentService();

    @GET
    @Path("/report")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response merchantRequestReport(DTUPayUser merchant){
        try {
            List<PaymentReport> report= reportService.requestMerchantReport(merchant.getAccountID());
            return Response.ok(report).build();
        }
        catch (Exception e){
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerMerchant(DTUPayUser merchant){
        String idRegistered= new String();
        try{
            idRegistered= service.requestAccountRegister(merchant);
            if (idRegistered.equals("")) {
                return Response.status(Response.Status.PRECONDITION_FAILED).entity("User was not registered").build();
            }
            return Response.ok(idRegistered).build();
        }catch(Exception e){
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getMessage()).build();
        }
    }
    @POST
    @Path("/unregister")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response unregisterMerchant(DTUPayUser merchant){
        boolean success;
        try{
            success=service.requestAccountDelete(merchant);
            if (!success) {
                return Response.status(Response.Status.PRECONDITION_FAILED).entity("Unregister account request failed ").build();
            }
            return Response.ok(true).build();

        }catch (Exception e){
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/pay")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response requestPayment(Payment payment){
        try{
            Boolean success= paymentservice.requestPayment(payment);
            return Response.ok(success).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getMessage()).build();
        }

    }
}