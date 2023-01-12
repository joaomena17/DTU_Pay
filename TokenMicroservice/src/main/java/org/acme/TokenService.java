package org.acme;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.PathParam;
import java.util.*;
import java.util.Random;

public class TokenService {

    private List<Token> TokenList = new ArrayList<Token>();
    private List<String> TokenList1 = new ArrayList<String>();

    private Token token = new Token("user1", TokenList1);


    public TokenService (){
        this.TokenList.add(this.token);
    }
    public String createRandomToken() {
        // create a string of uppercase and lowercase characters and numbers
        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        // combine all strings
        String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;

        // create random string builder
        StringBuilder sb = new StringBuilder();

        // create an object of Random class
        Random random = new Random();

        // specify length of random string
        int length = 10;

        for(int i = 0; i < length; i++) {

            // generate random index number
            int index = random.nextInt(alphaNumeric.length());

            // get character specified by index
            // from the string
            char randomChar = alphaNumeric.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }

        String randomString = sb.toString();
        System.out.println("Random String is: " + randomString);
        return randomString;

    }

    public List<Token> getTokenList() {
        return this.TokenList;
    }

    public Response getToken() {
        return Response.ok().build();
    }

    public Response getTokenById(String token) {
        List<Token> userTokenList = new ArrayList<>();

        for(Token temp : TokenList){
            System.out.println(temp);

            System.out.println(token);
            for(String tokenId : temp.tokens) {
                if(tokenId.equals(token)) {
                    return Response.ok(userTokenList).build();
                }
            }
        }
        return Response.status(Response.Status.PRECONDITION_FAILED).entity("Token ID not found").build();

    }

    public Response getTokenByUser(String user) {
        for(Token temp : TokenList){
            System.out.println(temp);
            System.out.println(temp.user);
            System.out.println(user);

            if(temp.user.equals(user)){
                return Response.ok(temp.tokens).build();
            }
        }
        return Response.status(Response.Status.PRECONDITION_FAILED).entity("Token ID not found").build();

    }
    //Creating token


    public Response createUser(Token t){
        if(doesUserExist(t.user)){
            System.out.println("User Already exists");
            return Response.status(Response.Status.PRECONDITION_FAILED).entity("User already exists").build();
        }
        List<String> emptyList = new ArrayList<String>();

        Token token1 = new Token(t.user, emptyList);
        TokenList.add(token1);
        return Response.ok().build();
    }
    public boolean doesUserExist(String username){
        for(Token t : TokenList) {
            if (t.user.equals(username)) {
                return true;
            }
        }
        return false;
    }
    public Response requestToken(TokenRequest tokenRequest) {
        System.out.println("REQUESTING TOKEN");
        System.out.println(tokenRequest);
        if(tokenRequest.number > 5){
            System.out.println("Too many tokens requested");
            return Response.status(Response.Status.PRECONDITION_FAILED).entity("Too many tokens requested").build();

        }
        if(tokenRequest.number < 0){
            System.out.println("Too few tokens requested");
            return Response.status(Response.Status.PRECONDITION_FAILED).entity("Too few tokens requested").build();
        }
        for(Token t : TokenList){
            if(t.user.equals(tokenRequest.user)){
                if(t.tokens.size()>= 2){
                    System.out.println("Has 2 or more valid tokens");
                    return Response.status(Response.Status.PRECONDITION_FAILED).entity("Has 2 or more valid tokens").build();
                }
                if(t.tokens.size() + tokenRequest.number > 6){
                    System.out.println("number requested plus owned larger than 6");
                    return Response.status(Response.Status.PRECONDITION_FAILED).entity("number requested plus owned larger than 6").build();
                }
                for(int i=1;i<=tokenRequest.number;i++){
                    System.out.println(i);
                    t.tokens.add(createRandomToken());
                }
                return Response.ok().build();
            }
        }

        return Response.status(Response.Status.PRECONDITION_FAILED).entity("User not found").build();
    }
}