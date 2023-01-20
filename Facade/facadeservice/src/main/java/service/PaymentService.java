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
    private Map<String, CompletableFuture<String>> correlations = new ConcurrentHashMap<>();

    public PaymentService(MessageQueue q){
        this.queue = q;
        this.queue.addHandler(EventTypes.PAYMENT_SUCCESS, this::handlePaymentSuccess);
        this.queue.addHandler(EventTypes.PAYMENT_FAILED, this::handlePaymentFailure);
    }

    public Boolean requestPayment(Payment payment){
        var correlationID = CorrelationID.randomID();
        correlations.put(payment.getCustomerToken(), new CompletableFuture<>());
        Event event = new Event(EventTypes.PAYMENT_REQUEST, new Object[] { payment, correlationID });
        queue.publish(event);
        System.out.println(correlationID + "RequestCorrId");
        correlations.get(payment.getMerchantBankID()).join();
        return true;
    }

    public void handlePaymentSuccess(Event e){
        var payment = e.getArgument(0, Payment.class);
        var correlationID = e.getArgument(2, String.class);
        System.out.println(correlationID);
        System.out.println(payment.getDescription());
        correlations.get(payment.getCustomerToken()).complete(payment.getMerchantBankID());
    }
    public void handlePaymentFailure(Event e){
        var payment = e.getArgument(0, Payment.class);
        var correlationID = e.getArgument(1, String.class);
        correlations.get(payment.getCustomerToken()).complete(payment.getMerchantBankID());
    }
}
