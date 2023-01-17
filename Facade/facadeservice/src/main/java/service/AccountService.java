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
    private Map<CorrelationID, CompletableFuture<DTUPayUser>> correlations = new ConcurrentHashMap<>();

    public AccountService(MessageQueue q){
        this.queue = q;
        this.queue.addHandler(EventTypes.REGISTER_ACCOUNT_COMPLETED, this::handleAccountRegistrationCompleted);
        this.queue.addHandler(EventTypes.REGISTER_ACCOUNT_FAILED, this::handleAccountRegistrationFailed);
        this.queue.addHandler(EventTypes.UNREGISTER_ACCOUNT_SUCCESS, this::handleAccountDeleteSuccess);
        this.queue.addHandler(EventTypes.UNREGISTER_ACCOUNT_FAILED, this::handleAccountDeleteFailed);
        this.queue.addHandler(EventTypes.UNREGISTER_ACCOUNT_NOT_EXIST, this::handleAccountDeleteNotExist);
    }

    public DTUPayUser requestAccountRegister(DTUPayUser user){
        var correlationID = utils.CorrelationID.randomID();
        correlations.put(correlationID, new CompletableFuture<>());
        Event event = new Event(EventTypes.REGISTER_ACCOUNT_REQUEST, new Object[]{ user, correlationID });
        queue.publish(event);
        return correlations.get(correlationID).join();
    }
    public void handleAccountRegistrationCompleted(Event event){
        var user = event.getArgument(0, DTUPayUser.class);
        var correlationID = event.getArgument(1, CorrelationID.class);
        correlations.get(correlationID).complete(user);
    }

    public void handleAccountRegistrationFailed(Event event){
        var user = event.getArgument(0, DTUPayUser.class);
        var correlationID = event.getArgument(1, CorrelationID.class);
        correlations.get(correlationID).complete(user);
    }

    public DTUPayUser requestAccountDelete(DTUPayUser user){
        var correlationID = utils.CorrelationID.randomID();
        correlations.put(correlationID, new CompletableFuture<>());
        Event event = new Event(EventTypes.UNREGISTER_ACCOUNT_REQUEST, new Object[]{ user, correlationID });
        queue.publish(event);
        return correlations.get(correlationID).join();
    }

    public void handleAccountDeleteSuccess(Event event){
        var user = event.getArgument(0, DTUPayUser.class);
        var correlationID = event.getArgument(1, CorrelationID.class);
        correlations.get(correlationID).complete(user);
    }

    public void handleAccountDeleteFailed(Event event){
        var user = event.getArgument(0, DTUPayUser.class);
        var correlationID = event.getArgument(1, CorrelationID.class);
        correlations.get(correlationID).complete(user);
    }

    public void handleAccountDeleteNotExist(Event event){
        var user = event.getArgument(0, DTUPayUser.class);
        var correlationID = event.getArgument(1, CorrelationID.class);
        correlations.get(correlationID).complete(user);
    }
}
