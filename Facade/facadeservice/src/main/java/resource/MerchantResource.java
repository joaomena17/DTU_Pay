package resource;

import entities.DTUPayUser;
import service.AccountService;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;

@Path("/merchants")
public class MerchantResource {

    private AccountService service = new DTUPayFactory().getService().getAccountService();

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public void registerMerchant(DTUPayUser merchant){ service.requestAccountRegister(merchant);
    }

    @POST
    @Path("/unregister")
    @Consumes(MediaType.APPLICATION_JSON)
    public void unregisterMerchant(DTUPayUser merchant){ service.requestAccountDelete(merchant);
    }
}