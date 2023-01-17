package service;

import entities.DTUPayUser;
import entities.Payment;
import messaging.MessageQueue;
import messaging.Event;
import utils.CorrelationID;
import utils.EventTypes;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentService {

    private MessageQueue queue;
    private Map<CorrelationID, CompletableFuture<Payment>> correlations = new ConcurrentHashMap<>();

    public PaymentService(MessageQueue q){
        this.queue = q;
        this.queue.addHandler(EventTypes.PAYMENT_SUCCESS, this::handlePaymentSuccess);
        this.queue.addHandler(EventTypes.PAYMENT_FAILED, this::handlePaymentFailure);
    }

    public Payment requestPayment(Payment payment){
        var correlationID = CorrelationID.randomID();
        correlations.put(correlationID, new CompletableFuture<>());
        Event event = new Event(EventTypes.PAYMENT_REQUEST, new Object[] { payment, correlationID });
        queue.publish(event);
        return correlations.get(correlationID).join();
    }

    public void handlePaymentSuccess(Event e){
        var payment = e.getArgument(0, Payment.class);
        var correlationID = e.getArgument(1, CorrelationID.class);
        correlations.get(correlationID).complete(payment);
    }
    public void handlePaymentFailure(Event e){
        var payment = e.getArgument(0, Payment.class);
        var correlationID = e.getArgument(1, CorrelationID.class);
        correlations.get(correlationID).complete(payment);
    }
}
