package resource;

import entities.DTUPayUser;
import service.AccountService;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;

@Path("/customers")
public class CustomerResource {

    private AccountService service = new DTUPayFactory().getService().getAccountService();

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
