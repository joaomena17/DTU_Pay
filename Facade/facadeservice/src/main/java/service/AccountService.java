package service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import entities.DTUPayUser;
import utils.CorrelationID;
import utils.EventTypes;
import messaging.Event;
import messaging.MessageQueue;

public class AccountService {

    private MessageQueue queue;
    private Map<String, CompletableFuture<String>> correlationsRegister = new ConcurrentHashMap<>();
    private Map<String, CompletableFuture<Boolean>> correlationsUnregister = new ConcurrentHashMap<>();

    public AccountService(MessageQueue q){
        this.queue = q;
        this.queue.addHandler(EventTypes.REGISTER_ACCOUNT_COMPLETED, this::handleAccountRegistrationCompleted);
        this.queue.addHandler(EventTypes.REGISTER_ACCOUNT_FAILED, this::handleAccountRegistrationFailed);
        this.queue.addHandler(EventTypes.UNREGISTER_ACCOUNT_SUCCESS, this::handleAccountDeleteSuccess);
        this.queue.addHandler(EventTypes.UNREGISTER_ACCOUNT_FAILED, this::handleAccountDeleteFailed);
    }

    public String requestAccountRegister(DTUPayUser user){
        System.out.println("requestAccountRegister Start");
        System.out.println("USER BANK ID  " + user.getBankID());
        correlationsRegister.put(user.getBankID(), new CompletableFuture<>());
        System.out.println(user
                .getAccountID() + user.getName() + user.getBankID());
        Event event = new Event(EventTypes.REGISTER_ACCOUNT_REQUEST, new Object[]{ user, user.getBankID() });
        queue.publish(event);
        return correlationsRegister.get(user.getBankID()).join();
    }
    public void handleAccountRegistrationCompleted(Event event){
        var userID = event.getArgument(0, String.class);
        var bankId = event.getArgument(1, String.class);
        System.out.println("USER ID  COMPLETED " + userID);
        correlationsRegister.get(bankId).complete(userID);
    }

    public void handleAccountRegistrationFailed(Event event){
        var userID = event.getArgument(0, String.class);
        var correlationID = event.getArgument(1, String.class);
        correlationsRegister.get(correlationID.toString()).complete(userID);
    }

    public Boolean requestAccountDelete(DTUPayUser user){
        var correlationID = utils.CorrelationID.randomID();
        correlationsUnregister.put(correlationID.toString(), new CompletableFuture<>());
        Event event = new Event(EventTypes.UNREGISTER_ACCOUNT_REQUEST, new Object[]{ user, correlationID.toString() });
        queue.publish(event);
        return correlationsUnregister.get(correlationID.toString()).join();
    }

    public void handleAccountDeleteSuccess(Event event){
        var result = event.getArgument(0, Boolean.class);
        var correlationID = event.getArgument(1, String.class);
        correlationsUnregister.get(correlationID.toString()).complete(result);
    }

    public void handleAccountDeleteFailed(Event event){
        var result = event.getArgument(0, Boolean.class);
        var correlationID = event.getArgument(1, String.class);
        correlationsUnregister.get(correlationID.toString()).complete(result);
    }
}
