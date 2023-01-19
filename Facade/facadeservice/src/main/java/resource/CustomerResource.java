package resource;

import entities.DTUPayUser;
import entities.PaymentReport;
import entities.TokenRequest;
import service.AccountService;
import service.TokenService;
import service.ReportService;

import javax.ws.rs.Consumes;
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
    public List<PaymentReport> customerRequestReport(DTUPayUser customer){
        return reportService.requestCustomerReport(customer.getAccountID());
    }

    @GET
    @Path("/getTokens")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCustomerTokens(TokenRequest request){
        return tokenService.customerTokensRequest(request.getAccountId(), request.getTokenAmount());
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
