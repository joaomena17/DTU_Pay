package org.acme;

import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/token")
public class TokenResource{
    private TokenService tService = new TokenService();
    public TokenResource(){

    }

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestSingleToken(RequestSingleToken T) {
        return tService.requestSingleToken(T);
    }

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response requestToken(TokenRequest T) {
        return tService.requestToken(T);
    }
    @DELETE
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteToken(RequestSingleToken T) {
        return tService.deleteToken(T);
    }


    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(CreateUser user) {
        return tService.createUser(user);
    }




}