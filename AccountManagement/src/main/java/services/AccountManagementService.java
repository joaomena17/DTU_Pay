package services;
/*
authors:
Tiago Machado s222963
 */

import java.util.List;

import Utils.EventTypes;
import handlers.AccountService;
import messaging.Event;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import messaging.MessageQueue;
import Entities.DTUPayUser;
import Utils.EventTypes;

public class AccountManagementService {

    public MessageQueue queue;
    AccountService accountService = new AccountService();

    public AccountManagementService(MessageQueue q) {
        this.queue = q;
        q.addHandler(EventTypes.REGISTER_ACCOUNT_REQUEST, this::handleRegisterAccountRequest);
        q.addHandler(EventTypes.BANK_ACCOUNT_ID_REQUEST, this::handleBankAccountIdRequest);
        q.addHandler(EventTypes.UNREGISTER_ACCOUNT_REQUEST, this::handleUnregisterAccountRequest);
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public String handleRegisterAccountRequest(Event ev) {
        Event finalEventCreated;
        var newAccount= ev.getArgument(0, DTUPayUser.class);
        var bankId= ev.getArgument(1,String.class);
        String newAccountId;

        newAccountId = accountService.registerAccount(newAccount);

        if(!newAccountId.equals("")) {
            System.out.println("REGISTER IS GOOD ACCOUNT MANAGEMENT");
            finalEventCreated = new Event(EventTypes.REGISTER_ACCOUNT_COMPLETED, new Object[]{newAccountId, bankId});
        }
        else {
            // Create an "AccountRegistrationFailed" event
            System.out.println("REGISTER FAILED ACCOUNT MANAGEMNET");
            finalEventCreated = new Event(EventTypes.REGISTER_ACCOUNT_FAILED, new Object[]{newAccountId, bankId});
        }

        queue.publish(finalEventCreated);
        return newAccountId;
    }

    public void handleUnregisterAccountRequest(Event ev){
        Event eventCreated;
        var accountToUnregister = ev.getArgument(0, DTUPayUser.class);
        var correlationId= ev.getArgument(1,String.class);

        boolean accountDeleted =  accountService.unregisterAccount(accountToUnregister);
        if(accountDeleted){
            eventCreated = new Event(EventTypes.UNREGISTER_ACCOUNT_SUCCESS,new Object[] {true,correlationId});
        }
        else{
            eventCreated = new Event(EventTypes.UNREGISTER_ACCOUNT_FAILED,new Object[] {false,correlationId });
        }

        queue.publish(eventCreated);
    }


    public void handleBankAccountIdRequest(Event ev){
        Event eventCreated;
        String DTUUserId = ev.getArgument(0,String.class);
        var correlationId= ev.getArgument(1,String.class);

        try {

            var userAccount= accountService.getAccount(DTUUserId);
            var bankAccountId=userAccount.getBankID();
            eventCreated = new Event(EventTypes.BANK_ACCOUNT_ID_SUCCESS, new Object[] {bankAccountId, correlationId});
        }
        catch (Exception e) {

            eventCreated = new Event(EventTypes.BANK_ACCOUNT_ID_FAILED,new Object[] { "",correlationId});
        }
        queue.publish(eventCreated);
    }

}