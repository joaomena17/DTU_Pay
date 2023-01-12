package org.acme;

import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/token")
public class TokenResource{
    private TokenService tService = new TokenService();
    public TokenResource(){

    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Token> getTokenJson() {
        return this.tService.getTokenList();
    }
    @Path("/{tokenId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTokenById(@PathParam("tokenId") String tokenId) {
        return tService.getTokenById(tokenId);
    }

    @Path("/user/{userId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTokenByUser(@PathParam("userId") String userId) {
        return tService.getTokenByUser(userId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postToken() {
        return tService.postToken();
    }

    @Path("/request")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response requestToken(TokenRequest T) {
        return tService.requestToken(T);
    }




}