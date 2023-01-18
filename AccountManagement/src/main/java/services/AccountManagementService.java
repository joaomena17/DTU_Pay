package services;
/*
authors:
Tiago Machado s222963
 */

import java.util.List;

import Utils.CorrelationId;
import Utils.EventTypes;
import handlers.AccountService;
import messaging.Event;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import messaging.MessageQueue;
import Entities.DTUPayUser;
import Utils.EventTypes;
import Utils.CorrelationId;

public class AccountManagementService {

    public MessageQueue queue;
    AccountService accountService = new AccountService();

    private Map<CorrelationId, CompletableFuture<Boolean>> correlations = new ConcurrentHashMap<>();
    public CorrelationId tokenCorrelationId ;
    public AccountManagementService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler(EventTypes.REGISTER_ACCOUNT_REQUEST, this::handleRegisterAccountRequest);
        this.queue.addHandler(EventTypes.BANK_ACCOUNT_ID_REQUEST, this::handleBankAccountIdRequest);
        this.queue.addHandler(EventTypes.UNREGISTER_ACCOUNT_REQUEST, this::handleUnregisterAccountRequest);
        this.queue.addHandler(EventTypes.GET_ACCOUNT_REQUEST, this::handleGetAccountRequest);
        this.queue.addHandler(EventTypes.GET_LIST_ACCOUNTS_REQUEST, this::handleGetListAccountsRequest);
        this.queue.addHandler(EventTypes.REGISTER_USER_TOKEN_SUCCESS, this::handleRegisterUserTokenSuccess);
        this.queue.addHandler(EventTypes.REGISTER_USER_TOKEN_FAILED, this::handleRegisterUserTokenFailed);

    }

    public AccountService getAccountService() {
        return accountService;
    }

    public String handleRegisterAccountRequest(Event ev) {
        Event finalEventCreated;
        var newAccount= ev.getArgument(0, DTUPayUser.class);
        var correlationId= ev.getArgument(1,CorrelationId.class);
        String newAccountId;
        try{
            newAccountId = accountService.registerAccount(newAccount);
            tokenCorrelationId= CorrelationId.randomId();
            correlations.put(tokenCorrelationId,new CompletableFuture<>());
            //Create an "RegisterUserTokenRequest" event
            Event tokenEventCreated=new Event(EventTypes.REGISTER_USER_TOKEN_REQUEST,new Object[] {newAccountId, tokenCorrelationId});
            queue.publish(tokenEventCreated);

            if (correlations.get(tokenCorrelationId).join()) {
                // Create an "AccountRegistrationCompleted" event
                finalEventCreated = new Event(EventTypes.REGISTER_ACCOUNT_COMPLETED, new Object[]{newAccountId, correlationId});
            }
            else {
                accountService.unregisterAccount(newAccount);
                throw new IllegalArgumentException("Error requesting to register user token");
            }
        }catch (Exception e){

            System.out.println(String.format("Exception: %s", e.getMessage()));
            // Create an "AccountRegistrationFailed" event
            finalEventCreated = new Event(EventTypes.REGISTER_ACCOUNT_FAILED,new Object[] {e.getMessage(),correlationId});
            newAccountId="";
        }
        queue.publish(finalEventCreated);
        return newAccountId;
    }

    public void handleBankAccountIdRequest(Event ev){
        Event eventCreated;

        String DTUUserId = ev.getArgument(0,String.class);
        var correlationId= ev.getArgument(1,CorrelationId.class);

        try{

            var userAccount= accountService.getAccount(DTUUserId);
            var bankAccountId=userAccount.getBankID();
            eventCreated = new Event(EventTypes.BANK_ACCOUNT_ID_SUCCESS, new Object[] {bankAccountId, correlationId});
        }
        catch (Exception e) {
            eventCreated = new Event(EventTypes.BANK_ACCOUNT_ID_FAILED,new Object[] { e.getMessage(),correlationId});
        }
        queue.publish(eventCreated);
    }

    public void handleUnregisterAccountRequest(Event ev){
        Event eventCreated;
        var unregisterAccount = ev.getArgument(0, DTUPayUser.class);
        var correlationId= ev.getArgument(1,CorrelationId.class);
        try{
            boolean accountDeleted =  accountService.unregisterAccount(unregisterAccount);
            if(accountDeleted){
                eventCreated = new Event(EventTypes.UNREGISTER_ACCOUNT_SUCCESS,new Object[] {true,correlationId});
            }
            else{
                eventCreated = new Event(EventTypes.UNREGISTER_ACCOUNT_NOT_EXIST,new Object[] {false,correlationId });
            }
        }
        catch (Exception e){
            eventCreated = new Event(EventTypes.UNREGISTER_ACCOUNT_FAILED,new Object[] {e.getMessage(),correlationId});
        }
        queue.publish(eventCreated);
    }

    public void handleGetAccountRequest(Event ev){
        Event eventCreated;
        var receivedAccountId = ev.getArgument(0, String.class);
        var correlationId= ev.getArgument(1,CorrelationId.class);
        try{
            DTUPayUser accountToBeReturned = accountService.getAccount(receivedAccountId);

            eventCreated = new Event(EventTypes.GET_ACCOUNT_COMPLETED,new Object[] {accountToBeReturned,correlationId});
        }
        catch (Exception e){
            eventCreated = new Event(EventTypes.GET_ACCOUNT_FAILED, new Object[] {e.getMessage(),correlationId});
        }
        queue.publish(eventCreated);
    }

    public void handleGetListAccountsRequest(Event ev){
        Event eventCreated;
        var typeList = ev.getArgument(0, String.class);
        var correlationId= ev.getArgument(1,CorrelationId.class);
        try{
            List<DTUPayUser> RequestedList;
            RequestedList= accountService.getAccountList(typeList);

            eventCreated = new Event(EventTypes.GET_LIST_ACCOUNTS_COMPLETED,new Object[] {RequestedList,correlationId});
        }
        catch (Exception e){
            eventCreated = new Event(EventTypes.GET_LIST_ACCOUNTS_FAILED, new Object[] {e.getMessage(),correlationId});
        }
        queue.publish(eventCreated);
    }
    public void handleRegisterUserTokenSuccess(Event ev) {
        System.out.println("PENULTIMO HANDLE");
        var success = ev.getArgument(0, boolean.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        System.out.println(String.format("PENULTIMO HANDLE CORR ID: %s", correlationId));
        correlations.get(correlationId).complete(success);
    }
    public void handleRegisterUserTokenFailed(Event ev) {
        var success = ev.getArgument(0, boolean.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        correlations.get(correlationId).complete(success);
    }
}