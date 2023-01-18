import ReportService.PaymentReport;
import Utils.CorrelationId;
import io.cucumber.java.en.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import io.cucumber.java.en.Given;
import messaging.Event;
import messaging.MessageQueue;
import ReportService.reportService;
import ReportService.Payment;
import Repository.PaymentRepository;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class reportsStep {
    private MessageQueue q = mock(MessageQueue.class);
    private PaymentRepository repo = new PaymentRepository();
    private reportService service = new reportService(q,repo);
    private PaymentReport p;
    private CorrelationId corrId;

    public reportsStep() {
    }
    @Given("there is a registered payment of {int} kr with customer {string} and merchant {string} with token {string}")
    public void thereIsARegisteredPaymentOfKrWithCustomerAndMerchant(int amount, String cid, String mid, String tok) {
        p = new PaymentReport();
        p.cid = cid;
        p.mid = mid;
        p.amount = BigDecimal.valueOf(amount);
        p.customerToken = tok;
        repo.addPayment(p);
    }

    @Given("there is an unregistered payment of {int} kr with customer {string} and merchant {string} with token {string}")
    public void thereIsAnUnregisteredPaymentOfKrWithCustomerAndMerchant(int amount, String cid, String mid, String tok) {
        p = new PaymentReport();
        p.cid = cid;
        p.mid = mid;
        p.amount = BigDecimal.valueOf(amount);
        p.customerToken = tok;
    }

    @When("a {string} event is received for customer report")
    public void aEventIsReceivedForCustomerReport(String eventType) {
        corrId = CorrelationId.randomId();
        service.handleCustomerReport(new Event(eventType, new Object[] {p.cid,corrId}));
    }

    @When("a {string} event is received for merchant report")
    public void aEventIsReceivedForMerchantReport(String eventType) {
        corrId = CorrelationId.randomId();
        service.handleMerchantReport(new Event(eventType, new Object[] {p.mid,corrId}));
    }

    @When("a {string} event is received for manager report")
    public void aEventIsReceivedForManagerReport(String eventType) {
        corrId = CorrelationId.randomId();
        service.handleManagerReport(new Event(eventType, new Object[] {corrId}));
    }

    @When("a {string} event is received to complete a payment")
    public void aEventIsReceivedToCompleteAPayment(String eventType) {
        Payment payment = new Payment();
        payment.setAmount(p.amount);
        payment.setMerchantBankID(p.mid);
        payment.setCustomerToken(p.customerToken);
        service.handlePaymentRegisterEvent(new Event(eventType, new Object[] {payment,p.cid}));
    }

    @Then("the report is created containing only customer {string}'s payments")
    public void theReportIsCreatedContainingOnlyCustomersPayments(String cid) {
        var list =  repo.GetCustomerPayments(cid);
        assertTrue(list.stream().allMatch(payment -> payment.cid.equals(cid)));
    }

    @Then("the report is created containing only merchant {string}'s received payments")
    public void theReportIsCreatedContainingOnlyMerchantReceivedPayments(String mid) {
        var list =  repo.GetMerchantPayments(mid);
        assertTrue(list.stream().allMatch(payment -> payment.mid.equals(mid)));
    }

    @Then("the report is created containing customer {string} and {string}'s payments")
    public void theReportIsCreatedContainingCustomerAndPayments(String cid1, String cid2) {
        var list =  repo.GetAllPayments();
        assertTrue(list.stream().allMatch(payment -> payment.cid.equals(cid1) || payment.cid.equals(cid2)));
    }

    @Then("the payment of {int} kr from {string} to {string} with token {string} is added to the repository")
    public void thePaymentIsAddedToTheRepository(int amount, String cid, String mid, String tok) {
        var list =  repo.GetAllPayments();
        boolean match;
        match = list.stream().allMatch(payment -> payment.cid.equals(cid) && payment.mid.equals(mid) && payment.amount.equals(BigDecimal.valueOf(amount)) && payment.customerToken.equals(tok));
        assertTrue(match);
    }

    @And("event {string} is sent with the list of customer {string}'s payments")
    public void eventIsSentWithTheListOfCustomersPayments(String eventType, String cid) {
        var list =  repo.GetCustomerPayments(cid);
        var event = new Event(eventType, new Object[] {list, corrId});
        verify(q).publish(event);
    }

    @And("event {string} is sent with the list of merchant {string}'s received payments")
    public void eventIsSentWithTheListOfMerchantReceivedPayments(String eventType, String mid) {
        var list =  repo.GetMerchantPayments(mid);
        //hack
        /*var newlist = new ArrayList<PaymentReport>();
        for (PaymentReport p : list) {
            newlist.add(new PaymentReport("", p.mid, p.amount, p.customerToken));
        }*/
        var event = new Event(eventType, new Object[] {list, corrId});
        verify(q).publish(event);
    }

    @And("event {string} is sent with the list of customer {string} and {string}'s payments")
    public void eventIsSentWithTheListOfCustomerAndPayments(String eventType, String cid1, String cid2) {
        var list =  repo.GetAllPayments();
        var event = new Event(eventType, new Object[] {list, corrId});
        verify(q).publish(event);
    }
}
