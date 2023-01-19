package service;

import entities.Payment;
import entities.PaymentReport;
import messaging.Event;
import messaging.MessageQueue;
import utils.CorrelationID;
import utils.EventTypes;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ReportService {
    private MessageQueue queue;
    private Map<CorrelationID, CompletableFuture<List<PaymentReport>>> correlations = new ConcurrentHashMap<>();

    public ReportService(MessageQueue q){
        this.queue = q;
        this.queue.addHandler(EventTypes.MERCHANT_REPORT_RETURN, this::handleMerchantReport);
        this.queue.addHandler(EventTypes.MANAGER_REPORT_RETURN, this::handleManagerReport);
        this.queue.addHandler(EventTypes.CUSTOMER_REPORT_RETURN, this::handleCustomerReport);

    }

    public void handleCustomerReport(Event event) {
        var report = event.getArgument(0, List.class);
        var correlationId = event.getArgument(1, CorrelationID.class);
        correlations.get(correlationId).complete(report);
    }

    public void handleManagerReport(Event event) {
        var report = event.getArgument(0, List.class);
        var correlationId = event.getArgument(1, CorrelationID.class);
        correlations.get(correlationId).complete(report);
    }

    public void handleMerchantReport(Event event) {
        var report = event.getArgument(0, List.class);
        var correlationId = event.getArgument(1, CorrelationID.class);
        correlations.get(correlationId).complete(report);
    }

    public List<PaymentReport> requestCustomerReport(String cid){
        var correlationID = CorrelationID.randomID();
        correlations.put(correlationID, new CompletableFuture<>());
        Event event = new Event(EventTypes.REQUEST_CUSTOMER_REPORT, new Object[] { cid, correlationID });
        queue.publish(event);
        return correlations.get(correlationID).join();
    }
    public List<PaymentReport> requestMerchantReport(String mid){
        var correlationID = CorrelationID.randomID();
        correlations.put(correlationID, new CompletableFuture<>());
        Event event = new Event(EventTypes.REQUEST_MERCHANT_REPORT, new Object[] { mid, correlationID });
        queue.publish(event);
        return correlations.get(correlationID).join();
    }
    public List<PaymentReport> requestManagerReport(){
        var correlationID = CorrelationID.randomID();
        correlations.put(correlationID, new CompletableFuture<>());
        Event event = new Event(EventTypes.REQUEST_MANAGER_REPORT, new Object[] {correlationID });
        queue.publish(event);
        return correlations.get(correlationID).join();
    }
}
