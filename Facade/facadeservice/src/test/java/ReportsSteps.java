//Author: Tiago Machado s222963


import entities.DTUPayUser;
import entities.PaymentReport;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.input.LineSeparatorDetector;
import service.ReportService;
import utils.CorrelationID;
import utils.EventTypes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReportsSteps {
    private Map<String, CompletableFuture<Event>> publishedEvents = new HashMap<>();
    private Map<String, CorrelationID> correlationIds = new HashMap<>();
    private CompletableFuture<List<PaymentReport>> reportCompletableFuture = new CompletableFuture<>();
    String reportId=new String();

    private MessageQueue queue = new MessageQueue() {

        @Override
        public void publish(Event event) {
            String id;
            if(event.getType().equals("generateManagerReport")) id="0";
            else id = event.getArgument(0, String.class);
            publishedEvents.get(id).complete(event);
        }

        @Override
        public void addHandler(String eventType, Consumer<Event> handler) {
        }
    };

    ReportService reportService= new ReportService(queue);
    @Given("the user has a DTUPay account with id {string}")
    public void theUserHasADTUPayAccountWithId(String id) {
        reportId=id;
        publishedEvents.put(reportId, new CompletableFuture<>());
    }

    @When("the customer asks for a report")
    public void theCustomerAsksForAReport() {
        new Thread(() -> {
            try {
                var reportList = reportService.requestCustomerReport(reportId);
                reportCompletableFuture.complete(reportList); //wait
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Then("the event {string} is published asking for report")
    public void theEventIsPublishedAskingForReport(String eventName) {
        Event event = publishedEvents.get(reportId).join();
        assertEquals(eventName, event.getType());
        var idSent = event.getArgument(0, String.class);
        var correlationId = event.getArgument(1, CorrelationID.class);
        correlationIds.put(reportId, correlationId);
    }

    @When("the costumer report is received from the report service")
    public void theCostumerReportIsReceivedFromTheReportService() {
        PaymentReport payment= new PaymentReport(reportId,"mid1",new BigDecimal(100),"token1");
        List<PaymentReport> listReport= new ArrayList<>();
        listReport.add(payment);

        reportService.handleCustomerReport (
                new Event(EventTypes.CUSTOMER_REPORT_RETURN,
                        new Object[] {listReport,correlationIds.get(reportId)}));
    }

    @Then("the report is created")
    public void theReportIsCreated() {
        assertNotNull(reportCompletableFuture.join());
    }

    @When("the merchant asks for a report")
    public void theMerchantAsksForAReport() {
        new Thread(() -> {
            try {
                var reportList = reportService.requestMerchantReport(reportId);
                reportCompletableFuture.complete(reportList); //wait
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    @When("the merchant report is received from the report service")
    public void theMerchantReportIsReceivedFromTheReportService() {
        PaymentReport payment= new PaymentReport("cid1",reportId,new BigDecimal(100),"token1");
        List<PaymentReport> listReport= new ArrayList<>();
        listReport.add(payment);

        reportService.handleCustomerReport (
                new Event(EventTypes.MERCHANT_REPORT_RETURN,
                        new Object[] {listReport,correlationIds.get(reportId)}));
    }

    @When("the manager asks for a report")
    public void theManagerAsksForAReport() {
        reportId="0";
        publishedEvents.put(reportId, new CompletableFuture<>());
        new Thread(() -> {
        try {
            var reportList = reportService.requestManagerReport();
            reportCompletableFuture.complete(reportList); //wait
        } catch (Exception e) {
            e.printStackTrace();
        }
    }).start();
    }

    @When("the manager report is received from the report service")
    public void theManagerReportIsReceivedFromTheReportService() {
        PaymentReport payment= new PaymentReport("cid1","mid1",new BigDecimal(100),"token1");
        List<PaymentReport> listReport= new ArrayList<>();
        listReport.add(payment);
        reportService.handleManagerReport (
                new Event(EventTypes.MANAGER_REPORT_RETURN,
                        new Object[] {listReport,correlationIds.get(reportId)}));
    }

    @Then("the event {string} is published asking for manager report")
    public void theEventIsPublishedAskingForManagerReport(String eventName) {
        Event event = publishedEvents.get(reportId).join();
        assertEquals(eventName, event.getType());
        var correlationId = event.getArgument(0, CorrelationID.class);
        correlationIds.put(reportId, correlationId);
    }

}
