package resource;

import entities.DTUPayUser;
import entities.PaymentReport;
import entities.TokenRequest;
import service.AccountService;
import service.TokenService;
import service.ReportService;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
/***
 * Author: Tiago Machado s222963
 */
@Path("/customers")
public class CustomerResource {

    private AccountService accountservice = new DTUPayFactory().getService().getAccountService();
    private ReportService reportService = new DTUPayFactory().getService().getReportService();

    private TokenService tokenService = new DTUPayFactory().getService().getTokenService();

    @GET
    @Path("/report")
    @Produces(MediaType.APPLICATION_JSON)
        public Response customerRequestReport(DTUPayUser customer){
        try {
            List<PaymentReport> report= reportService.requestCustomerReport(customer.getAccountID());
            return Response.ok(report).build();
        }
        catch (Exception e){
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getMessage()).build();
        }

    }

    @GET
    @Path("/getTokens")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerTokens(TokenRequest request){
        try{
            String success = tokenService.customerTokensRequest(request.getAccountId(), request.getTokenAmount());
            if(success.equals("Success")){
                return Response.ok(success).build();
            }
            else return Response.status(Response.Status.PRECONDITION_FAILED).entity("Tokens request not completed successfully").build();
        }
        catch(Exception e){
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getMessage()).build();
        }

    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerCustomer(DTUPayUser customer){
        String idRegistered;
        try {
            idRegistered = accountservice.requestAccountRegister(customer);
            if (idRegistered == "") {
                return Response.status(Response.Status.PRECONDITION_FAILED).entity("User was not registered").build();
            }
            return Response.ok(idRegistered).build();
        }
        catch(Exception e){
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getMessage()).build();
        }

    }

    @POST
    @Path("/unregister")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response unregisterCustomer(DTUPayUser customer){
        boolean success;
        try{
            success=accountservice.requestAccountDelete(customer);
            if (!success) {
                return Response.status(Response.Status.PRECONDITION_FAILED).entity("Unregister account request failed").build();
            }
            return Response.ok(true).build();

        }catch (Exception e){
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getMessage()).build();
        }
    }
}
