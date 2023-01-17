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
        this.queue.addHandler("MerchantReportReturnEvent", this::handleMerchantReport);
        this.queue.addHandler("ManagerReportReturnEvent", this::handleManagerReport);
        this.queue.addHandler("CustomerReportReturnEvent", this::handleCustomerReport);

    }

    private void handleCustomerReport(Event event) {
        var report = event.getArgument(0, List.class);
        var correlationId = event.getArgument(1, CorrelationID.class);
        correlations.get(correlationId).complete(report);
    }

    private void handleManagerReport(Event event) {
        var report = event.getArgument(0, List.class);
        var correlationId = event.getArgument(1, CorrelationID.class);
        correlations.get(correlationId).complete(report);
    }

    private void handleMerchantReport(Event event) {
        var report = event.getArgument(0, List.class);
        var correlationId = event.getArgument(1, CorrelationID.class);
        correlations.get(correlationId).complete(report);
    }

    public List<PaymentReport> requestCustomerReport(String cid){
        var correlationID = CorrelationID.randomID();
        correlations.put(correlationID, new CompletableFuture<>());
        Event event = new Event("generateCustomerReport", new Object[] { cid, correlationID });
        queue.publish(event);
        return correlations.get(correlationID).join();
    }
    public List<PaymentReport> requestMerchantReport(String mid){
        var correlationID = CorrelationID.randomID();
        correlations.put(correlationID, new CompletableFuture<>());
        Event event = new Event("generateMerchantReport", new Object[] { mid, correlationID });
        queue.publish(event);
        return correlations.get(correlationID).join();
    }
    public List<PaymentReport> requestManagerReport(){
        var correlationID = CorrelationID.randomID();
        correlations.put(correlationID, new CompletableFuture<>());
        Event event = new Event("generateManagerReport", new Object[] {correlationID });
        queue.publish(event);
        return correlations.get(correlationID).join();
    }
}
