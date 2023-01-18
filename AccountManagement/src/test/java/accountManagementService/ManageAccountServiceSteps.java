package accountManagementService;

import dtu.ws.fastmoney.*;
import groovy.xml.Entity;
import services.*;
import Entities.*;
import handlers.*;
import Utils.EventTypes;

// import Utils.CorrelationId;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import messaging.Event;
import messaging.MessageQueue;

import java.math.BigDecimal;

public class ManageAccountServiceSteps {

    private User user = new User();
    private String name = "Joao Silva";
    private String bankId;
    private String role = "customer";
    private BankService bank = new BankServiceService().getBankServicePort();
    private DTUPayUser customer;
    private String expected;
    CorrelationId correlationId;

    private MessageQueue queue = mock(MessageQueue.class);
    private AccountManagementService customerService = new AccountManagementService(queue);

    @Before
    public void setup() {

        user.setCprNumber("354-1235");
        user.setFirstName("Tiago");
        user.setLastName("Gomes");
        BigDecimal bigDecimalBalance = new BigDecimal(1000);

        bankId="11111";

        customer = new DTUPayUser(name, bankId, role);
    }


    /* Scenario: Register and Unregister customer are successful
    Given a customer that is not registered with DTU Pay that succeeds in registering and unregistering
    When a successful "RegisterAccountRequest" register event for the customer is received
    And a successful "RegisterUserTokenSuccess" event is received
    Then a success "RegisterAccountSuccess" event is ssent
    And a successful "UnregisterAccountRequest" unregister event for the customer is received
    And a success "UnregisterAccountSuccess" event is sent*/

    @Given("a customer that is not registered with DTU Pay that succeeds in registering and unregistering")
    public void a_customer_that_is_not_registered_with_DTU_Pay_that_succeeds_in_registering_and_unregistering() {
        assertNull(customer.getAccountID());
    }

    @When("a successful {string} register event for the customer is received")
    public void a_succsessful_register_event_for_the_customer_is_received(String eventName) {

        correlationId = CorrelationId.randomId();
        Event event = new Event(eventName, new Object[] { customer, correlationId });
        // new Thread(() -> {
        expected=customerService.handleRegisterAccountRequest(event);
        customer.setAccountID(expected);
        // }).start();
    }

    /* @And("a successful {string} event is received")
    public void a_succsessful_register_event_is_received(String eventName) {
        var tokenCorrId = customerService.tokenCorrelationId;
        Event event = new Event(eventName, new Object[] { true, tokenCorrId });
        customerService.handleRegisterUserTokenSuccess(event);
    } */

    @Then("a success {string} event is ssent")
    public void a_success_register_event_is_sent(String eventName) {

        // var event = new Event(eventName, new Object[] {expected, correlationId});
        var event = new Event(EventTypes.REGISTER_ACCOUNT_COMPLETED, new Object[] {expected});
        verify(queue).publish(event);
    }

    @And("a successful {string} unregister event for the customer is received")
    public void a_succsessful_unregister_event_for_the_customer_is_received(String eventName) {

        correlationId = CorrelationId.randomId();
        Event event = new Event(eventName, new Object[] { customer, correlationId });
        customerService.handleUnregisterAccountRequest(event);
    }

    @And("a success {string} event is sent")
    public void a_success_unregister_event_is_sent(String eventName) {

        var event = new Event(eventName, new Object[] {true, correlationId});
        verify(customerService.queue).publish(event);
    }

    /* Scenario: Register and Unregister customer are unsuccessful
    Given a customer that is not registered with DTU Pay that fails to register
    When an unsuccessful "RegisterAccountRequest" register event for the customer is received
    Then a failure "RegisterAccountRequestFailed" event is ssent
    And the customer that cannot register is unregistered
    And an unsuccessful "UnregisterAccountRequest" unregister event for the customer is received
    And a failure "UnregisterAccountFailed" event is sent */


    @Given("a customer that is not registered with DTU Pay that fails to register")
    public void a_customer_that_is_not_registered_with_DTU_Pay_that_fails_to_register() {
        customer.set_name("");
        assertNull(customer.getAccountID());
    }

    @When("an unsuccessful {string} register event for the customer is received")
    public void an_unsuccsessful_register_event_for_the_customer_is_received(String eventName) {
        correlationId = CorrelationId.randomId();
        Event event = new Event(eventName, new Object[] { customer, correlationId });
        customerService.handleRegisterAccountRequest(event);
    }

    @Then("a failure {string} event is ssent")
    public void a_failure_register_event_is_sent(String eventName) {
        var event = new Event(eventName, new Object[] {"", correlationId});
        // verify(queue).publish(event);
    }

    @And("an unsuccessful {string} unregister event for the customer is received")
    public void an_unsuccsessful_unregister_event_for_the_customer_is_received(String eventName) {
        correlationId = CorrelationId.randomId();
        Event event = new Event(eventName, new Object[] { customer, correlationId });
        customerService.handleUnregisterAccountRequest(event);
    }

    @And("a failure {string} event is sent")
    public void a_failure_unregister_event_is_sent(String eventName) {
        var event = new Event(eventName, new Object[] {false, correlationId});
        verify(queue).publish(event);
    }


}
