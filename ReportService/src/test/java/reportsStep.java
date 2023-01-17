import ReportService.PaymentReport;
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
    private CompletableFuture<Event> publishedEvent = new CompletableFuture<>();

    //private MessageQueue q = new MessageQueue() {
    private MessageQueue q = mock(MessageQueue.class);/* {

        @Override
        public void publish(Event event) {
            publishedEvent.complete(event);
        }

        @Override
        public void addHandler(String eventType, Consumer<Event> handler) {
        }

    };*/

    private PaymentRepository repo = new PaymentRepository();

    private reportService service = new reportService(q,repo);

    //private Payment p;
    private PaymentReport p;
    public reportsStep() {
    }

    @Given("there is a registered payment of {int} kr with customer id {string} and merchant id {string}")
    public void thereIsARegisteredPaymentOfKrWithCustomerIdAndMerchantId(int amount, String cid, String mid) {
        p = new PaymentReport();
        p.cid = cid;
        p.mid = mid;
        p.amount = BigDecimal.valueOf(amount);
        repo.addPayment(p);
    }


    @Given("there is a wish about a payment of {int} kr with customer id {string} and merchant id {string}")
    public void thereIsAWishAboutAPaymentOfKrWithCustomerIdAndMerchantId(int amount, String cid, String mid) {
        p = new PaymentReport();
        p.cid = cid;
        p.mid = mid;
        //p.amount = BigDecimal.valueOf(amount);
        p.amount = new BigDecimal(10);
    }

    @When("a {string} event is received for customer report")
    public void aEventIsReceivedForCustomerReport(String eventType) {
        service.handleCustomerReport(new Event(eventType, new Object[] {p.cid}));
    }

    @When("a {string} event is received for merchant report")
    public void aEventIsReceivedForMerchantReport(String eventType) {
        service.handleMerchantReport(new Event(eventType, new Object[] {p.mid}));
    }

    @When("a {string} event is received for manager report")
    public void aEventIsReceivedForManagerReport(String eventType) {
        service.handleManagerReport(new Event(eventType));
    }

    @When("a {string} event is received")
    public void aEventIsReceived(String eventType) {
        service.handlePaymentRegisterEvent(new Event(eventType, new Object[] {p}));
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
        //should check if list does not contain the info about cid?
    }

    @Then("the report is created containing customer {string} and {string}'s payments")
    public void theReportIsCreatedContainingCustomerAndPayments(String cid1, String cid2) {
        var list =  repo.GetAllPayments();
        assertTrue(list.stream().anyMatch(payment -> payment.cid.equals(cid1)));
        assertTrue(list.stream().anyMatch(payment -> payment.cid.equals(cid2)));
    }

    //using bigdecimal in text
    @Then("the payment of {int} kr from {string} to {string} is added to the repository")
    public void thePaymentIsAddedToTheRepository(int amount, String cid, String mid) {
        var list =  repo.GetAllPayments();
        List<PaymentReport> compareList = new ArrayList<>();
        PaymentReport payCompare = new PaymentReport();
        //payCompare.amount = BigDecimal.valueOf(amount);
        payCompare.amount = new BigDecimal(10);
        payCompare.cid = cid;
        payCompare.mid = mid;

        compareList.add(payCompare);

        System.out.println("Size of list"+list.size());

        System.out.println("Size of compareList"+compareList.size());

        System.out.println("Compare.amount:" +compareList.get(0).amount+"Compare.cid:"+compareList.get(0).cid+"Compare.mid"+compareList.get(0).mid);
        System.out.println("list.amount:" +list.get(0).amount+"list.cid:"+list.get(0).cid+"list.mid"+list.get(0).mid);

        assertTrue(list.stream().anyMatch(payment -> payment.cid.equals(cid)));
        assertTrue(list.stream().anyMatch(payment -> payment.mid.equals(mid)));
        assertTrue(list.stream().anyMatch(payment -> payment.amount.equals(BigDecimal.valueOf(amount))));

        assertTrue(list.stream().anyMatch(payment -> payment.cid.equals(payCompare.cid)));
        assertTrue(list.stream().anyMatch(payment -> payment.mid.equals(payCompare.mid)));
        assertTrue(list.stream().anyMatch(payment -> payment.amount.equals((payCompare.amount))));
        System.out.println("BOOL value:" + list.stream().anyMatch(payment -> payment.equals(payCompare)));

        //assertTrue(list.stream().anyMatch(payment -> payment.equals(payCompare)));

    }

    @And("event {string} is sent with the list of customer {string}'s payments")
    public void eventIsSentWithTheListOfCustomersPayments(String eventType, String cid) {
        var list =  repo.GetCustomerPayments(cid);
        var event = new Event(eventType, new Object[] {list});
        verify(q).publish(event);
    }
    @And("event {string} is sent with the list of merchant {string}'s received payments")
    public void eventIsSentWithTheListOfMerchantReceivedPayments(String eventType, String mid) {
        var list =  repo.GetMerchantPayments(mid);
        var event = new Event(eventType, new Object[] {list});
        verify(q).publish(event);
    }
    @And("event {string} is sent with the list of customer {string} and {string}'s payments")
    public void eventIsSentWithTheListOfCustomerAndPayments(String eventType) {
        var list =  repo.GetAllPayments();
        var event = new Event(eventType, new Object[] {list});
        verify(q).publish(event);
    }
}
