package service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import entities.DTUPayUser;
import utils.CorrelationID;
import utils.EventTypes;
import messaging.Event;
import messaging.MessageQueue;

public class AccountService {

    private MessageQueue queue;
    private Map<CorrelationID, CompletableFuture<String>> correlationsRegister = new ConcurrentHashMap<>();
    private Map<CorrelationID, CompletableFuture<Boolean>> correlationsUnregister = new ConcurrentHashMap<>();

    public AccountService(MessageQueue q){
        this.queue = q;
        this.queue.addHandler(EventTypes.REGISTER_ACCOUNT_COMPLETED, this::handleAccountRegistrationCompleted);
        this.queue.addHandler(EventTypes.REGISTER_ACCOUNT_FAILED, this::handleAccountRegistrationFailed);
        this.queue.addHandler(EventTypes.UNREGISTER_ACCOUNT_SUCCESS, this::handleAccountDeleteSuccess);
        this.queue.addHandler(EventTypes.UNREGISTER_ACCOUNT_FAILED, this::handleAccountDeleteFailed);
    }

    public String requestAccountRegister(DTUPayUser user){
        var correlationID = utils.CorrelationID.randomID();
        correlationsRegister.put(correlationID, new CompletableFuture<>());
        Event event = new Event(EventTypes.REGISTER_ACCOUNT_REQUEST, new Object[]{ user, correlationID });
        queue.publish(event);
        return correlationsRegister.get(correlationID).join();
    }
    public void handleAccountRegistrationCompleted(Event event){
        var userID = event.getArgument(0, String.class);
        var correlationID = event.getArgument(1, CorrelationID.class);
        correlationsRegister.get(correlationID).complete(userID);
    }

    public void handleAccountRegistrationFailed(Event event){
        var userID = event.getArgument(0, String.class);
        var correlationID = event.getArgument(1, CorrelationID.class);
        correlationsRegister.get(correlationID).complete(userID);
    }

    public Boolean requestAccountDelete(DTUPayUser user){
        var correlationID = utils.CorrelationID.randomID();
        correlationsUnregister.put(correlationID, new CompletableFuture<>());
        Event event = new Event(EventTypes.UNREGISTER_ACCOUNT_REQUEST, new Object[]{ user, correlationID });
        queue.publish(event);
        return correlationsUnregister.get(correlationID).join();
    }

    public void handleAccountDeleteSuccess(Event event){
        var result = event.getArgument(0, Boolean.class);
        var correlationID = event.getArgument(1, CorrelationID.class);
        correlationsUnregister.get(correlationID).complete(result);
    }

    public void handleAccountDeleteFailed(Event event){
        var result = event.getArgument(0, Boolean.class);
        var correlationID = event.getArgument(1, CorrelationID.class);
        correlationsUnregister.get(correlationID).complete(result);
    }
}
