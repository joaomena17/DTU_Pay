package accountManagementService;

import dtu.ws.fastmoney.*;
import services.*;
import Entities.*;
import handlers.*;

import Utils.CorrelationId;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import messaging.Event;
import messaging.MessageQueue;

import java.math.BigDecimal;

public class UnregisterCustomerServiceSteps {

    private User user = new User();
    private String name = "Joao Silva";
    private String bankId;
    private String role = "customer";
    private BankService bank = new BankServiceService().getBankServicePort();
    private DTUPayUser customer;
    private DTUPayUser expected;
    private CorrelationId correlationId;
    private AccountManagementService customerService = new AccountManagementFactory().getService();
    private MessageQueue queue = mock(MessageQueue.class);

    /* Scenario: Unregister customer is successful
    Given a customer that is registered with DTU Pay that succeeds in unregistering
    When a successful "UnregisterAccountRequested" unregister event for the customer is received
    Then a success "UnregisterAccountSuccess" event is sent
    And the customer is unregistered */

    @Before
    public void setup() {

        user.setCprNumber("289-1234");
        user.setFirstName("Joao");
        user.setLastName("Silva");
        BigDecimal bigDecimalBalance = new BigDecimal(1000);

        try {
            bankId = bank.createAccountWithBalance(user, bigDecimalBalance);
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }

        customer = new DTUPayUser(name, bankId, role);
    }

    @After
    public void tearDown() {

        try {
            bank.retireAccount(bankId);
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }
    }

    @Given("a customer that is registered with DTU Pay that succeeds in unregistering")
    public void a_customer_that_is_registered_with_DTU_Pay() {
        assertNotNull(customer.getAccountID());
    }

    @When("a successful {string} unregister event for the customer is received")
    public void a_succsessful_unregister_event_for_the_customer_is_received(String eventName) {

        correlationId = CorrelationId.randomId();
        Event event = new Event(eventName, new Object[] { customer, correlationId });
        customerService.handleUnregisterAccountRequest(event);
    }

    @Then("a success {string} event is sent")
    public void a_success_event_is_sent(String eventName) {

        expected = new DTUPayUser(name, bankId, role);
        var event = new Event(eventName, new Object[] {expected, correlationId});
        verify(queue).publish(event);
    }

    @And("the customer is unregistered")
    public void the_customer_is_unregistered() {
        assertNull(expected.getAccountID());
    }

    /* Scenario: Unregister customer is unsuccessful
    Given a customer that is registered with DTU Pay that fails to unregister
    When an unsuccsessful "UnregisterAccountRequest" unregister event for a customer is received
    Then a failure "UnregisterAccountRequestFailed" event is sent
    And the customer is not unregistered */

    @Given("a customer that is registered with DTU Pay that fails to unregister")
    public void a_customer_that_is_registered_with_DTU_Pay_and_fails() {
        assertNotNull(customer.getAccountID());
    }

    @When("an unsuccsessful {string} unregister event for a customer is received")
    public void an_unsuccsessful_unregister_event_for_a_customer_is_received(String eventName) {

        correlationId = CorrelationId.randomId();
        Event event = new Event(eventName, new Object[] { customer, correlationId });
        customerService.handleUnregisterAccountRequest(event);
    }

    @Then("a failure {string} event is sent")
    public void a_failure_event_is_sent(String eventName) {

        expected = new DTUPayUser(name, bankId, role);
        var event = new Event(eventName, new Object[] {expected, correlationId});
        verify(queue).publish(event);
    }

    @And("the customer is not unregistered")
    public void the_customer_is_not_unregistered() {
        assertNotNull(expected.getAccountID());
    }
}
