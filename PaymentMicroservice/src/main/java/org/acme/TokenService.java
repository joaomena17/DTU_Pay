package org.acme;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.PathParam;


public class TokenService {

    private List<Token> TokenList = new ArrayList<>();

    private Token token = new Token("asd123", "user 1");
    public TokenService (){

        this.TokenList.add(this.token);
    }

    public List<Token> getTokenList() {
        return this.TokenList;
    }

    public Response getToken() {
        return Response.ok().build();
    }

    public Response getTokenById(String id) {
        
        return Response.ok(id).build();
    }
    //Creating token
    public Response postToken(){
        Token token1 = new Token("asd1236", "user 2");
        TokenList.add(token1);
        return Response.ok().build();
    }
}