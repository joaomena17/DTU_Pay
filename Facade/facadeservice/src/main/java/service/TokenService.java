package service;

import entities.PaymentReport;
import messaging.MessageQueue;
import messaging.Event;
import utils.CorrelationID;
import utils.EventTypes;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class TokenService {
    private MessageQueue queue;
    private Map<CorrelationID, CompletableFuture<List<String>>> correlations = new ConcurrentHashMap<>();

    public TokenService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler(EventTypes.CUSTOMER_TOKENS_SUCCESS, this::handleCustomerTokensSuccess);
        this.queue.addHandler(EventTypes.CUSTOMER_TOKENS_FAIL, this::handleCustomerTokensFail);
    }

    public List<String> customerTokensRequest(String cid){
        var correlationID = CorrelationID.randomID();
        correlations.put(correlationID, new CompletableFuture<>());
        Event event = new Event(EventTypes.CUSTOMER_TOKENS_REQUEST, new Object[] { cid, correlationID });
        queue.publish(event);
        return correlations.get(correlationID).join();
    }

    public void handleCustomerTokensSuccess(Event e){
        var customerTokens = e.getArgument(0, List.class);
        var correlationId = e.getArgument(1, CorrelationID.class);
        correlations.get(correlationId).complete(customerTokens);
    }

    public void handleCustomerTokensFail(Event e){
        var customerTokens = e.getArgument(0, List.class);
        var correlationId = e.getArgument(1, CorrelationID.class);
        correlations.get(correlationId).complete(customerTokens);
    }
}
