package service;

import entities.DTUPayUSer;
import EventTypes;

import java.util.concurrent.CompletableFuture;

import messaging.Event;
import messaging.MessageQueue;
public class AccountService {

    private MessageQueue queue;
    private CompletableFuture<DTUPayUser> user;

    public AccountService(MessageQueue q){
        queue = q;
        queue.addHandler(REGISTER_ACCOUNT_COMPLETED, this::handleAccountRegistration);
        // add remaining events and respective handle functions
    }

    public DTUPayUser registerAccount(DTUPayUser account){
        user = new CompletableFuture<>();
        Event event = new Event(REGISTER_ACCOUNT_REQUEST, new Object[] { account });
        queue.publish(event);
        return user.join();
    }

    public void handleAccountRegistration(Event e){
        var acc = e.getArgument(0, DTUPayUser.class);
        user.complete(acc);
    }
}
