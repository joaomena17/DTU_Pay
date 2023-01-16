package services;
/*
authors:
Tiago Machado s222963
 */

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import accountservice.AccountService;
import accountservice.DTUPayUser;
import messaging.Event;

import java.util.Map;
import messaging.MessageQueue;

public class AccountManagementService {

    MessageQueue queue;
    AccountService accountService = new AccountService();

    public AccountManagementService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler(EventTypes.REGISTER_ACCOUNT_REQUEST, this::handleRegisterAccountRequest);
        this.queue.addHandler(EventTypes.BANK_ACCOUNT_ID_REQUEST, this::handleBankAccountIdRequest);
        this.queue.addHandler(EventTypes.UNREGISTER_ACCOUNT_REQUEST, this::handleUnregisterAccountRequest);
        this.queue.addHandler(EventTypes.GET_ACCOUNT_REQUEST, this::handleGetAccountRequest);
        this.queue.addHandler(EventTypes.GET_LIST_ACCOUNTS_REQUEST, this::handleGetListAccountsRequest);

    }

    public void handleRegisterAccountRequest(Event ev) {
        Event eventCreated;
        var newAccount= ev.getArgument(0, DTUPayUser.class);
        var correlationId= ev.getArgument(1,CorrelationId.class);

        try{
            String newAccountId = accountService.registerAccount(newAccount);
            // Create an "AccountRegistrationCompleted" event
            eventCreated = new Event(EventTypes.REGISTER_ACCOUNT_COMPLETED,new Object[] {newAccountId, correlationId});
        }catch (Exception e){
            // Create an "AccountRegistrationFailed" event
            eventCreated = new Event(EventTypes.REGISTER_ACCOUNT_FAILED,new Object[] {e.getMessage(),correlationId});
        }
        queue.publish(eventCreated);
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
        checkCorrelationID(ev);
        Event event;
        var receivedAccountId = ev.getArgument(0, String.class);
        try{
            boolean accountDeleted =  accHandler.deleteAccountInfo(receivedAccountId);
            if(accountDeleted){
                event = new Event(EventTypes.DELETE_ACCOUNT_SUCCESS, ev.getCorrID(), new Object[] {});
            }
            else{
                event = new Event(EventTypes.DELETE_ACCOUNT_NOT_EXIST, ev.getCorrID(), new Object[] { receivedAccountId });
            }
        }
        catch (Exception e){
            event = new Event(EventTypes.DELETE_ACCOUNT_FAILED, ev.getCorrID(), new Object[] {});
        }
        queue.publish(event);
    }

    public Event handleGetAccountRequest(Event ev){
        checkCorrelationID(ev);
        Event event;
        var receivedAccountId = ev.getArgument(0, String.class);
        try{

            Account accountToBeReturned = accHandler.getAccount(receivedAccountId);

            if(accountToBeReturned != null){
                event = new Event(EventTypes.ACCOUNT_REQUEST_COMPLETED, ev.getCorrID(), new Object[] {new ReturnAccount(accountToBeReturned, true, "")});

            }else{
                event = new Event(EventTypes.ACCOUNT_REQUEST_FAILED, ev.getCorrID(), new Object[] { new ReturnAccount(null, true, "Account " + receivedAccountId + " does not exist") });
            }
        }
        catch (Exception e){
            event = new Event(EventTypes.ACCOUNT_REQUEST_FAILED, ev.getCorrID(), new Object[] {new ReturnAccount(null, false, e.getMessage())});
        }
        queue.publish(event);
        return event;
    }

    //TODO
//    public void handleAccountsRequested(Event ev){
//        Event event;
//        try{
//            var accounts = accHandler.getAccounts();
//            event = new Event("AccountsRequestSucceeded", new Object[] {});
//        }
//        catch (Exception e){
//            event = new Event("AccountsRequestFailed", new Object[] {});
//        }
//        queue.publish(event);
//    }

    public void checkCorrelationID(Event e) {
        String receivedCorrID = e.getCorrID();
        String eventType = e.getType();
        System.out.println("Received event "
                + eventType
                + " with correlationID: "
                + receivedCorrID);
    }
}