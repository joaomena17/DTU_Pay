import io.cucumber.java.en.*;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import io.cucumber.java.en.Given;
import messaging.Event;
import messaging.MessageQueue;
import ReportService.reportService;
import ReportService.Payment;
import Repository.PaymentRepository;

public class reportsStep {
    private CompletableFuture<Event> publishedEvent = new CompletableFuture<>();

    private MessageQueue q = new MessageQueue() {

        @Override
        public void publish(Event event) {
            publishedEvent.complete(event);
        }

        @Override
        public void addHandler(String eventType, Consumer<Event> handler) {
        }

    };

    private PaymentRepository repo = new PaymentRepository();

    private reportService service = new reportService(q,repo);

    private Payment p;
    public reportsStep() {
    }

    @Given("there is a registered payment with customer id {String} and merchant id {String}")
    public void thereIsARegisteredPaymentWithCustomerIdCidAndMerchantIdMid(String cid, String mid) {
        p = new Payment();
        p.cid = cid;
        p.mid = mid;
        p.amount = new BigDecimal(20);

        repo.addPayment(p);
    }

    @When("a customer requests a report")
    public void aCustomerRequestsAReport() {
    }

    @Then("the report is created containing only customer with customer id {String] payments is created")
    public void theReportIsCreatedContainingOnlyCustomersPaymentsIsCreated(String cid) {

      var list =  repo.GetCustomerPayments(cid);
      assertTrue(list.stream().allMatch(payment -> payment.cid.equals(cid)));
    }

    @And("event CustomerReportReturnEvent is sent")
    public void eventCustomerReportReturnEventIsSent() {

    }
}
