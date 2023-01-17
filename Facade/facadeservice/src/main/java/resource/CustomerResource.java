package resource;

import entities.DTUPayUser;
import entities.PaymentReport;
import service.AccountService;
import service.ReportService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/customers")
public class CustomerResource {

    private AccountService service = new DTUPayFactory().getService().getAccountService();
    private ReportService reportService = new DTUPayFactory().getService().getReportService();

    @GET
    @Path("/report")
    @Consumes(MediaType.APPLICATION_JSON)
    public List<PaymentReport> customerRequestReport(DTUPayUser customer){
        return reportService.requestCustomerReport(customer.getAccountID());
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public DTUPayUser registerCustomer(DTUPayUser customer){
        return service.requestAccountRegister(customer);
    }

    @POST
    @Path("/unregister")
    @Consumes(MediaType.APPLICATION_JSON)
    public DTUPayUser unregisterCustomer(DTUPayUser customer){
        return service.requestAccountDelete(customer);
    }
}
