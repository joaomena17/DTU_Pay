package resources;

import merchantservice.IMerchantService;
import merchantservice.MerchantService;
import merchantservice.Merchant;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;

@Path("/merchant")
public class MerchantResources {

    private IMerchantService merchantService= new MerchantService();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMerchantJson(@PathParam("id") String id) {
        try{
            Merchant merchant=merchantService.getMerchant(id);
            return Response.ok(merchant).build();
        }
        catch(Exception e){
            System.err.println("Got error: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getCause()).build();
        }
    }
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerMerchantJson(Merchant merchant ) {
        String merchant_id=new String();
        try {
            merchant_id= merchantService.registerMerchant(merchant);
            return Response.ok(merchant_id).build();
        }
        catch(Exception e){
            System.err.println("Got error: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getCause()).build();
        }
    }
    @POST
    @Path("/deregister")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAccountJson(Merchant merchant) {
        try {
            boolean success_delete = merchantService.unregisterMerchant(merchant);
            return Response.ok(success_delete).build();
        }
        catch(Exception e){
            System.err.println("Got error: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getCause()).build();
        }
    }

}



