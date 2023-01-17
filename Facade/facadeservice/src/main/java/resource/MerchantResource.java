package resource;

import entities.DTUPayUser;
import service.AccountService;

import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;

@Path("/merchants")
public class MerchantResource {

    private AccountService service = new DTUPayFactory().getService().getAccountService();

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public DTUPayUser registerMerchant(DTUPayUser merchant){
        return service.requestAccountRegister(merchant);
    }

    @POST
    @Path("/unregister")
    @Produces(MediaType.APPLICATION_JSON)
    public DTUPayUser unregisterMerchant(DTUPayUser merchant){
        return service.requestAccountDelete(merchant);
    }
}