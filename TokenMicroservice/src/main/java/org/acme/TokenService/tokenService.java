package org.acme.TokenService;

import Utils.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Token;
import org.acme.Utils.EventTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class tokenService implements interfaceTokenService {
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
        public String getSingleToken(String user) {
            return null;
        }

        @Override
        public String requestTokenMessageQueue(String token, int number) {
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
        var corrId = event.getArgument(1, CorrelationId.class);
        String token = getSingleToken(customerId);
        if (token.equals("error")){
            queue.publish(new Event(EventTypes.GET_TOKEN_FAILED,new Object[]{false,corrId}));
        }
        else{
            queue.publish(new Event(EventTypes.GET_TOKEN_SUCCESS,new Object[]{token,corrId}));
        }
    }
    public void handleRequestToken(Event event){
        String customerId = event.getArgument(0,String.class);
        var corrId = event.getArgument(1, CorrelationId.class);
        var number = event.getArgument(2, int.class);
        String reqToken = requestTokenMessageQueue(customerId, number);
        if(reqToken.equals("success")) {
            queue.publish(new Event(EventTypes.REQUEST_TOKEN_SUCCESS,new Object[]{true,corrId}));
        }else {
            queue.publish(new Event(EventTypes.REQUEST_TOKEN_FAILED,new Object[]{reqToken,corrId}));
        }

    }
    public void handleCustomerTokens(Event event){
        String customerId = event.getArgument(0,String.class);
        var corrId = event.getArgument(1, CorrelationId.class);
        List<String> tokenList = getAllTokensByUser(customerId);
        if(tokenList !=  null) {
            queue.publish(new Event(EventTypes.CUSTOMER_TOKENS_SUCCESS,new Object[]{tokenList,corrId}));
        }else {
            queue.publish(new Event(EventTypes.CUSTOMER_TOKENS_FAILED,new Object[]{"Error",corrId}));
        }

    }


    public tokenService(MessageQueue mq, interfaceTokenService p) {
        queue = mq;
        queue.addHandler(EventTypes.REGISTER_TOKEN_USER,this::handleRegisterUserTokenRequest);
        queue.addHandler(EventTypes.VALIDATE_TOKEN, this::handleValidateToken);
        queue.addHandler(EventTypes.GET_TOKEN, this::handleGetToken);
        queue.addHandler(EventTypes.REQUEST_TOKEN,this::handleRequestToken);
        queue.addHandler(EventTypes.CUSTOMER_TOKENS_REQUEST,this::handleCustomerTokens);

        this.interfaceTokenService = p;
    }
    public tokenService() {

    }
    public Token getTokenByUser(String username) {
        for(Token t : TokenList) {
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
        return randomString;
    }
    public String createRandomToken() {
        String newToken;
        do {
            newToken = createRandomString();
        }while (usedTokens.contains(newToken));
        return newToken;
    }

    @Override
    public boolean registerUser(String user){
        if(doesUserExist(user)){
            return false;
        }
        List<String> emptyList = new ArrayList<String>();
        Token token = new Token(user, emptyList);
        TokenList.add(token);
        return true;
    }
    public boolean doesUserExist(String username){
        for(Token t : TokenList) {
            if (t.user.equals(username)) {
                return true;
            }
        }
        return false;
    }

    // Checks if token is valid, marks the token as used and returns the user
    @Override
    public String validateToken(String t){
        for(Token tok: TokenList){
            if(tok.tokens.size()>= 1){
                for(String token : tok.tokens){
                    if(token.equals(t)){
                        tok.tokens.remove(t);
                        usedTokens.add(t);
                        return tok.user;
                    }
                }
            }
        }
        return "Error";
    }
    // If the user has a valid token then it returns a token
    @Override
    public String getSingleToken(String user){
        if(doesUserExist(user) == false){
            return "error";
        }
        for(Token tok: TokenList){
            if(user.equals(tok.user)){
                if(tok.tokens.size()>= 1){
                    return tok.tokens.get(0);
                }
            }
        }
        return "error";
    }

    // Used when customers request new tokens
    @Override
    public String requestTokenMessageQueue(String user, int number) {
        //Check if user exists
        if(doesUserExist(user) == false){
            registerUser(user); // Create user in the token service
        }
        // Check if customer is requesting more than 5 tokens
        if(number > 5){
            return "Too many tokens requested";

        }
        // Check if customer is requesting more than 0 or negative tokens
        if(number <= 0){
            return "Too few tokens requested";
        }
        for(Token t : TokenList){
            if(t.user.equals(user)){
                //Check if customer has 2 or more valid tokens
                if(t.tokens.size()>= 2){
                    return "User has 2 or more valid tokens";
                }
                // Check if the combined number of owned tokens + requested is larger than 6
                if(t.tokens.size() + number > 6){
                    return "Number of tokens requested plus owned larger than 6";
                }
                // create the tokens
                for(int i=1;i<=number;i++){
                    t.tokens.add(createRandomToken());
                }
                return "Success";
            }
        }

        return "Error";
    }

}