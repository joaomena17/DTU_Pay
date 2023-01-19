package resource;


import entities.DTUPayUser;
import entities.PaymentReport;
import service.ReportService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
/***
 * Author: Tiago Machado s222963
 */
@Path("/manager")
public class ReportResource {
    private ReportService reportService = new DTUPayFactory().getService().getReportService();

    @GET
    @Path("/report")
    @Produces(MediaType.APPLICATION_JSON)
    public Response managerRequestReport( ){

        try {
            List<PaymentReport> report= reportService.requestManagerReport();
            return Response.ok(report).build();
        }
        catch (Exception e){
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getMessage()).build();
        }
    }
}
