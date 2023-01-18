package resource;


import entities.DTUPayUser;
import entities.PaymentReport;
import service.ReportService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/manager")
public class ReportResource {
    private ReportService reportService = new DTUPayFactory().getService().getReportService();

    @GET
    @Path("/report")
    @Consumes(MediaType.APPLICATION_JSON)
    public List<PaymentReport> managerRequestReport( ){
        return reportService.requestManagerReport();
    }
}
