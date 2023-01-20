import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import entities.*;
import messaging.Event;
import messaging.MessageQueue;
import service.PaymentService;
import utils.CorrelationID;
import utils.EventTypes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/***
 * Author: Tiago Machado s222963
 */
public class PaymentSteps {
    private Map<String, CompletableFuture<Event>> publishedEvents = new HashMap<>();
    private Map<Payment, CorrelationID> correlationIds = new HashMap<>();
    private CompletableFuture<Boolean> paymentCompletableFuture = new CompletableFuture<>();

    private MessageQueue queue = new MessageQueue() {

        @Override
        public void publish(Event event) {
            var payId = event.getArgument(0, Payment.class);
            publishedEvents.get(payId.getMerchantBankID()).complete(event);
        }

        @Override
        public void addHandler(String eventType, Consumer<Event> handler) {
        }
    };

    PaymentService paymentService= new PaymentService(queue);
    String customerId,merchantId;
    Payment payment,paymentRequested;
    @Given("a Customer registered")
    public void aCustomerRegistered() {
        customerId="739398222";
    }

    @And("a Merchant registered")
    public void aMerchantRegistered() {
        merchantId="378383922039";
        publishedEvents.put(merchantId, new CompletableFuture<>());
    }

    @When("the merchant initiates a payment for {int} kr by customer")
    public void theMerchantInitiatesAPaymentForKrByCustomer(int amount) {
        payment= new Payment(merchantId,"token1","first payment",new BigDecimal(amount));
        new Thread(() -> {
            try {
                var success = paymentService.requestPayment(payment);
                paymentCompletableFuture.complete(success); //wait
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Then("the {string} is published requesting payment")
    public void theIsPublishedRequestingPayment(String eventName) {
        Event event = publishedEvents.get(merchantId).join();
        assertEquals(eventName, event.getType());
        paymentRequested = event.getArgument(0,Payment.class);
        var correlationId = event.getArgument(1, CorrelationID.class);
        correlationIds.put(paymentRequested, correlationId);
    }

    @When("the payment service notifies the success of the payment")
    public void thePaymentServiceNotifiesTheSuccessOfThePayment() {
        paymentService.handlePaymentSuccess(
                new Event(EventTypes.PAYMENT_SUCCESS,
                        new Object[] {payment,correlationIds.get(paymentRequested)}));
    }

    @Then("the payment was successful")
    public void thePaymentWasSuccessful() {
        assertTrue(paymentCompletableFuture.join());
    }
}
