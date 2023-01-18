package org.acme.TokenService;

import Utils.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.CreateUser;
import org.acme.RequestSingleToken;
import org.acme.Token;
import org.acme.TokenRequest;
import org.acme.Utils.EventTypes;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import java.util.Random;

public class tokenService implements interfaceTokenService {
    private RequestSingleToken requestSingleToken;
    MessageQueue queue;
    private List<String> usedTokens = new ArrayList<String>();
    private List<Token> TokenList = new ArrayList<Token>();
    private interfaceTokenService interfaceTokenService = new interfaceTokenService() {
        @Override
        public boolean registerUser(String user) {
            return false;
        }

        @Override
        public String validateToken(String t) {
            return null;
        }

        @Override
        public String getSingleToken(RequestSingleToken t) {
            return null;
        }

        @Override
        public String requestTokenMessageQueue(TokenRequest tokenRequest) {
            return null;
        }
    };

    public List<Token> getTokenList() {
        return TokenList;
    }
    public void handleRegisterUserTokenRequest(Event event){
        var customerId = event.getArgument(0,String.class);
        var corrId = event.getArgument(1,CorrelationId.class);
        boolean register = registerUser(customerId);
        if(register){
            queue.publish(new Event(EventTypes.REGISTER_TOKEN_USER_SUCCESS,new Object[]{true,corrId}));

        }else{
            queue.publish(new Event(EventTypes.REGISTER_TOKEN_USER_FAILED,new Object[]{false,corrId}));

        }
    }
    public void handleValidateToken(Event event){
        var token = event.getArgument(0,String.class);
        var corrId = event.getArgument(1,CorrelationId.class);
        validateToken(token);
        String response = validateToken(token);
        if (response.equals("error")){
            queue.publish(new Event(EventTypes.VALIDATE_SUCCESS,new Object[]{response,corrId}));
        }
        else {
            queue.publish(new Event(EventTypes.VALIDATE_FAILED,new Object[]{response,corrId}));
        }
    }
    public void handleGetToken(Event event){
        String customerId = event.getArgument(0,String.class);
        RequestSingleToken requestSingleToken = new RequestSingleToken(customerId);
        var corrId = event.getArgument(1, CorrelationId.class);
        String token = getSingleToken(requestSingleToken);
        if (token.equals("error")){
            queue.publish(new Event(EventTypes.GET_TOKEN_FAILED,new Object[]{false,corrId}));
        }
        else{
            queue.publish(new Event(EventTypes.GET_TOKEN_SUCCESS,new Object[]{token,corrId}));
        }
    }
    public void handleRequestToken(Event event){
        String customerId = event.getArgument(0,String.class);
        int number = event.getArgument(2, int.class);
        TokenRequest tokenRequest = new TokenRequest(customerId, number);
        var corrId = event.getArgument(1, CorrelationId.class);
        String reqToken = requestTokenMessageQueue(tokenRequest);
        if(reqToken.equals("success")) {
            queue.publish(new Event(EventTypes.REQUEST_TOKEN_SUCCESS,new Object[]{true,corrId}));
        }else {
            queue.publish(new Event(EventTypes.REQUEST_TOKEN_FAILED,new Object[]{reqToken,corrId}));
        }

    }


    public tokenService(MessageQueue mq, interfaceTokenService p) {
        queue = mq;
        queue.addHandler(EventTypes.REGISTER_TOKEN_USER,this::handleRegisterUserTokenRequest);
        queue.addHandler(EventTypes.VALIDATE_TOKEN, this::handleValidateToken);
        queue.addHandler(EventTypes.GET_TOKEN, this::handleGetToken);
        queue.addHandler(EventTypes.REQUEST_TOKEN,this::handleRequestToken);
        this.interfaceTokenService = p;
    }
    public tokenService() {

    }
    public Token getTokenByUser(String username) {
        for(Token t : TokenList) {
            System.out.println(t.user);
            if (t.user.equals(username)) {
                return t;
            }
        }
        return null;
    }
    public List<String> getAllTokensByUser(String username) {
        for(Token t : TokenList) {
            if (t.user.equals(username)) {
                return t.tokens;
            }
        }
        return null;
    }



    public String createRandomString() {
        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
        String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz-_";
        String numbers = "0123456789";
        String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = 25;
        for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphaNumeric.length());
            char randomChar = alphaNumeric.charAt(index);
            sb.append(randomChar);
        }
        String randomString = sb.toString();
        System.out.println("Random String is: " + randomString);
        return randomString;
    }
    public String createRandomToken() {
        String newToken;
        do {
            newToken = createRandomString();
        }while (usedTokens.contains(newToken));
        return newToken;
    }

    public Response createUser(CreateUser t){
        System.out.println("Creating user");
        if(doesUserExist(t.user)){
            System.out.println("User Already exists");
            return Response.status(Response.Status.PRECONDITION_FAILED).entity("User already exists").build();
        }
        List<String> emptyList = new ArrayList<String>();
        Token token = new Token(t.user, emptyList);
        TokenList.add(token);
        return Response.ok().build();
    }

    @Override
    public boolean registerUser(String user){
        System.out.println("Creating user");
        if(doesUserExist(user)){
            System.out.println("User Already exists");
            return false;
        }
        List<String> emptyList = new ArrayList<String>();
        Token token = new Token(user, emptyList);
        TokenList.add(token);
        return true;
    }
    public boolean doesUserExist(String username){
        System.out.println(username);
        for(Token t : TokenList) {
            System.out.println(t.user);
            if (t.user.equals(username)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public String validateToken(String t){
        String userFound = "";

        for(Token tok: TokenList){

                if(tok.tokens.size()>= 1){
                    System.out.println("Token found:");
                    System.out.println(tok.tokens.get(0));
                    tok.tokens.remove(t);
                    usedTokens.add(t);
                    return tok.user;
                }

        }
        return "Error";
    }
    public Response requestSingleToken(RequestSingleToken t){
        if(doesUserExist(t.user) == false){
            System.out.println("User does not exists");
            return Response.status(Response.Status.NOT_FOUND).entity("404, user not found").build();
        }
        for(Token tok: TokenList){
            if(t.user.equals(tok.user)){
                if(tok.tokens.size()>= 1){
                    System.out.println("Token found:");
                    System.out.println(tok.tokens.get(0));
                    return Response.ok(tok.tokens.get(0)).build();
                }
            }
        }
        return Response.status(Response.Status.PRECONDITION_FAILED).entity("412, user does not have any tokens").build();
    }
    @Override
    public String getSingleToken(RequestSingleToken t){
        if(doesUserExist(t.user) == false){
            System.out.println("User does not exists");
            return "error";
        }
        for(Token tok: TokenList){
            if(t.user.equals(tok.user)){
                if(tok.tokens.size()>= 1){
                    System.out.println("Token found:");
                    System.out.println(tok.tokens.get(0));
                    return tok.tokens.get(0);
                }
            }
        }
        return "error";
    }

    public Response deleteToken(RequestSingleToken t){
        if(doesUserExist(t.user) == false){
            System.out.println("User does not exists");
            return Response.status(Response.Status.NOT_FOUND).entity("404, User not found").build();
        }
        for(Token tok: TokenList){
            if(t.user.equals(tok.user)){
                for(String tokenToDelete : tok.tokens){
                    if(tokenToDelete.equals(t.token)){
                        System.out.println("Token to delete:");
                        System.out.println(tokenToDelete);
                        tok.tokens.remove(t.token);

                        usedTokens.add(tokenToDelete);
                        return Response.ok("200 Success").build();
                    }
                }
                return Response.status(Response.Status.NOT_FOUND).entity("404, Token not found").build();
            }
        }
        return Response.status(Response.Status.PRECONDITION_FAILED).entity("Error").build();
    }
    @Override
    public String requestTokenMessageQueue(TokenRequest tokenRequest) {
        //doesUserExist(tokenRequest.user);
        if(doesUserExist(tokenRequest.user) == false){
            System.out.println("User does not exists");
            return "User does not exists";
        }
        System.out.println("REQUESTING TOKEN 1");
        System.out.println(tokenRequest);
        if(tokenRequest.number > 5){
            System.out.println("Too many tokens requested");
            return "Too many tokens requested";

        }
        if(tokenRequest.number <= 0){
            System.out.println("Too few tokens requested");
            return "Too few tokens requested";
        }
        for(Token t : TokenList){
            if(t.user.equals(tokenRequest.user)){
                if(t.tokens.size()>= 2){
                    System.out.println("Has 2 or more valid tokens");
                    return "User has 2 or more valid tokens";
                }
                if(t.tokens.size() + tokenRequest.number > 6){
                    System.out.println("number requested plus owned larger than 6");
                    return "Number of tokens requested plus owned larger than 6";
                }
                for(int i=1;i<=tokenRequest.number;i++){
                    System.out.println(i);
                    t.tokens.add(createRandomToken());
                }
                return "Success";
            }
        }

        return "Error";
    }


    public Response requestToken(TokenRequest tokenRequest) {
        //doesUserExist(tokenRequest.user);
        if(doesUserExist(tokenRequest.user) == false){
            System.out.println("User does not exists");
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        System.out.println("REQUESTING TOKEN 1");
        System.out.println(tokenRequest);
        if(tokenRequest.number > 5){
            System.out.println("Too many tokens requested");
            return Response.status(Response.Status.PRECONDITION_FAILED).entity("Too many tokens requested").build();

        }
        if(tokenRequest.number <= 0){
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
                return Response.ok("200 Success").build();
            }
        }

        return Response.status(Response.Status.PRECONDITION_FAILED).entity("User not found").build();
    }
}