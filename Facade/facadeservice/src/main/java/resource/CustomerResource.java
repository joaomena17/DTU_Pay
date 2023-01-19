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
import java.util.List;

@Path("/customers")
public class CustomerResource {

    private AccountService accountservice = new DTUPayFactory().getService().getAccountService();
    private ReportService reportService = new DTUPayFactory().getService().getReportService();

    private TokenService tokenService = new DTUPayFactory().getService().getTokenService();

    @GET
    @Path("/report")
    @Produces(MediaType.APPLICATION_JSON)
    public Response customerRequestReport(DTUPayUser customer){
        try{
            var result = reportService.requestCustomerReport(customer.getAccountID());
            return Response.ok(result).build();
        }
        catch (Exception e){
            return Response.
        }
    }

    @GET
    @Path("/getTokens")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCustomerTokens(TokenRequest request){
        var result = tokenService.customerTokensRequest(request.getAccountId(), request.getTokenAmount());
        return 
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public String registerCustomer(DTUPayUser customer){
        return accountservice.requestAccountRegister(customer);
    }

    @POST
    @Path("/unregister")
    @Consumes(MediaType.APPLICATION_JSON)
    public Boolean unregisterCustomer(DTUPayUser customer){
        return accountservice.requestAccountDelete(customer);
    }
}
