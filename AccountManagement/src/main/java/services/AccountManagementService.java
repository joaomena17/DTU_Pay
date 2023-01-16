package services;
/*
authors:
Tiago Machado s222963
 */


import accountservice.*;
import messaging.Event;
import messaging.MessageQueue;
import utils.EventTypes;

public class AccountManagementService {

    MessageQueue queue;
    AccountService accHandler = new AccountService();

    public AccountService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler(EventTypes.REGISTER_ACCOUNT_REQUEST, this::handleRegisterAccountRequest);
        this.queue.addHandler(EventTypes.BANK_ACCOUNT_ID_REQUEST, this::handleBankAccountIdRequest);
        this.queue.addHandler(EventTypes.UNREGISTER_ACCOUNT_REQUEST, this::handleUnregisterAccountRequest);
        this.queue.addHandler(EventTypes.GET_ACCOUNT_REQUEST, this::handleGetAccountRequest);
        this.queue.addHandler(EventTypes.GET_LIST_ACCOUNTS_REQUEST, this::handleGetListAccountsRequest);

    }

    public Event handleRegisterAccountRequest(Event ev) {
        checkCorrelationID(ev);
        Event event;
        var receivedAccount = ev.getArgument(0, DTUPayUser.class);

        try{
            var newAccountId = accHandler.createAccountFromData(receivedAccount);
            // Create an "AccountRegistrationCompleted" event
            event = new Event(EventTypes.ACCOUNT_REGISTRATION_COMPLETED, ev.getCorrID(), new Object[] {newAccountId});
        }catch (Exception e){
            // Create an "AccountRegistrationFailed" event
            event = new Event(EventTypes.ACCOUNT_REGISTRATION_FAILED, ev.getCorrID(), new Object[] {e.getMessage()});
        }
        queue.publish(event);
        return event;
    }

    public Event handleBankAccountIdRequest(Event ev){
        checkCorrelationID(ev);
        Event event;

        int count = ev.getArgument(0,int.class);

        String[] ids = new String[count];
        for (int i = 0; i < count; i++) {
            ids[i] = ev.getArgument(i+1, String.class);
        }

        try{

            Object[] arguments = new Object[count];
            for (int i = 0; i < count; i++) {
                arguments[i] = accHandler.getAccountBankId(ids[i]);
            }
            event = new Event(EventTypes.BANK_ACCOUNT_ID_RETRIEVAL_SUCCESS, ev.getCorrID(), arguments);
        }
        catch (Exception e) {
            event = new Event(EventTypes.BANK_ACCOUNT_ID_RETRIEVAL_FAILED, ev.getCorrID(), new Object[] { e.getMessage()});
        }
        queue.publish(event);
        return event;
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