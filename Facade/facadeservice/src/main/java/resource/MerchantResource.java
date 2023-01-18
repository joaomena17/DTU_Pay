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

@Path("/merchants")
public class MerchantResource {

    private AccountService service = new DTUPayFactory().getService().getAccountService();

    private ReportService reportService = new DTUPayFactory().getService().getReportService();

    @GET
    @Path("/report")
    @Consumes(MediaType.APPLICATION_JSON)
    public List<PaymentReport> merchantRequestReport(DTUPayUser merchant){
        return reportService.requestMerchantReport(merchant.getBankID());
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public String registerMerchant(DTUPayUser merchant){ return service.requestAccountRegister(merchant);
    }

    @POST
    @Path("/unregister")
    @Consumes(MediaType.APPLICATION_JSON)
    public Boolean unregisterMerchant(DTUPayUser merchant){ return service.requestAccountDelete(merchant);
    }
}