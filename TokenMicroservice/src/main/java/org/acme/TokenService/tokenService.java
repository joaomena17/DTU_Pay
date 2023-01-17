package org.acme.TokenService;

import Utils.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.CreateUser;
import org.acme.Repository.TokenRepository;
import org.acme.RequestSingleToken;
import org.acme.Token;
import org.acme.TokenRequest;
import org.acme.Utils.EventTypes;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import java.util.Random;

public class tokenService {
    private TokenRepository tokenRepository;
    private RequestSingleToken requestSingleToken;
    private MessageQueue queue;
    private List<String> usedTokens = new ArrayList<String>();
    private static List<Token> TokenList = new ArrayList<Token>();

    public List<Token> getTokenList() {
        return TokenList;
    }

    public void handleToken(Event event){
        String customerId = event.getArgument(0,String.class);
        RequestSingleToken requestSingleToken = new RequestSingleToken(customerId);
        var corrId = event.getArgument(1, CorrelationId.class);
        Event e = new Event("getTokenEvent", new Object[] {getSingleToken(requestSingleToken),corrId});
        queue.publish(e);
    }

    public void handleDeleteToken(Event event){
        String customerId = event.getArgument(0,String.class);
        RequestSingleToken requestSingleToken = new RequestSingleToken(customerId);
        var corrId = event.getArgument(1, CorrelationId.class);
        Event e = new Event("getTokenEvent", new Object[] {deleteToken(requestSingleToken),corrId});
        queue.publish(e);
    }

    public void handleCreateUser(Event event){
        String customerId = event.getArgument(0,String.class);
        CreateUser createUser = new CreateUser(customerId);
        var corrId = event.getArgument(1, CorrelationId.class);
        Event e = new Event("createTokenUserEvent", new Object[] {createUser(createUser),corrId});
        queue.publish(e);
    }

    public void handleRequestToken(Event event){
        String customerId = event.getArgument(0,String.class);
        int number = event.getArgument(2, int.class);
        TokenRequest tokenRequest = new TokenRequest(customerId, number);
        var corrId = event.getArgument(1, CorrelationId.class);
        Event e = new Event("createTokenUserEvent", new Object[] {requestToken(tokenRequest),corrId});
        queue.publish(e);
    }
    public void handleValidateToken(Event event){
        String token = event.getArgument(0,String.class);
        int number = event.getArgument(2, int.class);
        TokenRequest tokenRequest = new TokenRequest(customerId, number);
        var corrId = event.getArgument(1, CorrelationId.class);
        String response = validateToken(token);
        Event e = new Event("createTokenUserEvent", new Object[] {validateToken(token),corrId});
        queue.publish(e);
    }
    public void handleValidateTokenRequestSuccess(Event event){
        var token = event.getArgument(0,String.class);
        var corrId = event.getArgument(1,CorrelationId.class);
        validateToken(token);
        String response = validateToken(token);
        if (response == "Error"){
            queue.publish(new Event(EventTypes.VALIDATE_SUCCESS,new Object[]{customerId,corrId}));
        }
        else {
            queue.publish(new Event(EventTypes.VALIDATE_FAILED,new Object[]{customerId,corrId}));
        }
    }


    public void handleRegisterUserTokenSuccess(Event event){
        var customerId = event.getArgument(0,String.class);
        var corrId = event.getArgument(1,CorrelationId.class);
        queue.publish(new Event(EventTypes.RegisterUserTokenSuccess,new Object[]{customerId,corrId}));
    }
    public void handleRegisterUserTokenFailed(Event event){
        var customerId = event.getArgument(0,String.class);
        var corrId = event.getArgument(1,CorrelationId.class);
        queue.publish(new Event(EventTypes.RegisterUserTokenFailed,new Object[]{customerId,corrId}));
    }

    public void handleRegisterUserTokenRequest(Event event){
        var customerId = event.getArgument(0,String.class);
        var corrId = event.getArgument(1,CorrelationId.class);
        queue.publish(new Event(EventTypes.RegisterUserTokenFailed,new Object[]{customerId,corrId}));
    }

    public tokenService(MessageQueue mq, TokenRepository p) {
        queue = mq;
        queue.addHandler(EventTypes.GET_TOKEN, this::handleToken);
        queue.addHandler(EventTypes.DELETE_TOKEN, this::handleDeleteToken);
        queue.addHandler(EventTypes"requestToken", this::handleRequestToken);
        queue.addHandler(EventTypes.VALIDATE_TOKEN, this::handleValidateToken);
        queue.addHandler(EventTypes."ValidateTokenRequestSuccess", this::handleValidateTokenRequestSuccess);
        queue.addHandler(EventTypes.REGISTER_TOKEN_USER,this::handleRegisterUserTokenRequest);
        this.tokenRepository = p;
    }
    public tokenService() {

    }
    public static Token getTokenByUser(String username) {
        for(Token t : TokenList) {
            System.out.println(t.user);
            if (t.user.equals(username)) {
                return t;
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

  //  public Response getToken() {
  //      return Response.ok().build();
 //   }

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
    public static boolean doesUserExist(String username){
        System.out.println(username);
        for(Token t : TokenList) {
            System.out.println(t.user);
            if (t.user.equals(username)) {
                return true;
            }
        }
        return false;
    }

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