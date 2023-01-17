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

public class ManageAccountServiceSteps {

    private User user = new User();
    private String name = "Joao Silva";
    private String bankId;
    private String role = "customer";
    private BankService bank = new BankServiceService().getBankServicePort();
    private DTUPayUser customer;
    private DTUPayUser expected;
    private CorrelationId correlationId;
    private AccountManagementService customerService = new AccountManagementFactory().getService();
    private MessageQueue queue = customerService.queue;

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

    /* Scenario: Register and Unregister customer are successful
    Given a customer that is not registered with DTU Pay that succeeds in registering and unregistering
    When a successful "RegisterAccountRequest" register event for the customer is received
    Then a success "RegisterAccountSuccess" event is sent
    And the customer is registered
    And a successful "UnregisterAccountRequest" unregister event for the customer is received
    And a success "UnregisterAccountSuccess" event is sent
    And the customer is unregistered */

    @Given("a customer that is not registered with DTU Pay that succeeds in registering and unregistering")
    public void a_customer_that_is_not_registered_with_DTU_Pay_that_succeeds_in_registering_and_unregistering() {
        assertNull(customer.getAccountID());
    }

    @When("a successful {string} register event for the customer is received")
    public void a_succsessful_register_event_for_the_customer_is_received(String eventName) {

        correlationId = CorrelationId.randomId();
        Event event = new Event(eventName, new Object[] { customer, correlationId });
        customerService.handleRegisterAccountRequest(event);
    }

    @Then("a success {string} event is sent")
    public void a_success_register_event_is_sent(String eventName) {

        expected = new DTUPayUser(name, bankId, role);
        var event = new Event(eventName, new Object[] {expected, correlationId});
        verify(queue).publish(event);
    }

    @And("the customer is registered")
    public void the_customer_is_registered() {
        assertNotNull(expected.getAccountID());
    }

    @And("a successful {string} unregister event for the customer is received")
    public void a_succsessful_unregister_event_for_the_customer_is_received(String eventName) {

        correlationId = CorrelationId.randomId();
        Event event = new Event(eventName, new Object[] { customer, correlationId });
        customerService.handleUnregisterAccountRequest(event);
    }

    @And("a success {string} event is sent")
    public void a_success_unregister_event_is_sent(String eventName) {

        expected = new DTUPayUser(name, bankId, role);
        var event = new Event(eventName, new Object[] {expected, correlationId});
        verify(queue).publish(event);
    }

    @And("the customer is unregistered")
    public void the_customer_is_unregistered() {
        assertNull(expected.getAccountID());
    }

    /* Scenario: Register customer is successful and Unregister customer is unsuccessful
    Given a customer that is not registered with DTU Pay that succeeds in registering but not unregistering
    When a successful "RegisterAccountRequest" register event for the customer that cannot unregister is received
    Then a success "RegisterAccountSuccess" event is sent for the customer that cannot unregister
    And the customer that cannot unregister is registered
    And an unsuccessful "UnregisterAccountRequest" unregister event for the registered customer is received
    And a failure "UnregisterAccountFailed" event is sent to the registered customer
    And the registered customer is registered */

    @Given("a customer that is not registered with DTU Pay that succeeds in registering but not unregistering")
    public void a_customer_that_is_not_registered_with_DTU_Pay_that_succeeds_in_registering_but_not_unregistering() {
        assertNull(customer.getAccountID());
    }

    @When("a successful {string} register event for the customer that cannot unregister is received")
    public void a_succsessful_register_event_for_the_customer_that_cannot_unregister_is_received(String eventName) {

        correlationId = CorrelationId.randomId();
        Event event = new Event(eventName, new Object[] { customer, correlationId });
        customerService.handleRegisterAccountRequest(event);
    }

    @Then("a success {string} event is sent for the customer that cannot unregister")
    public void a_success_register_event_is_sent_for_the_customer_that_cannot_unregister(String eventName) {

        expected = new DTUPayUser(name, bankId, role);
        var event = new Event(eventName, new Object[] {expected, correlationId});
        verify(queue).publish(event);
    }

    @And("the customer that cannot unregister is registered")
    public void the_customer_that_cannot_unregister_is_registered() {
        assertNotNull(expected.getAccountID());
    }

    @And("an unsuccessful {string} unregister event for the registered customer is received")
    public void an_unsuccsessful_unregister_event_for_the_registered_customer_is_received(String eventName) {

        correlationId = CorrelationId.randomId();
        Event event = new Event(eventName, new Object[] { customer, correlationId });
        customerService.handleUnregisterAccountRequest(event);
    }

    @And("a failure {string} event is sent to the registered customer")
    public void a_failure_unregister_event_is_sent_to_the_registered_customer(String eventName) {

        expected = new DTUPayUser(name, bankId, role);
        var event = new Event(eventName, new Object[] {expected, correlationId});
        verify(queue).publish(event);
    }

    @And("the registered customer is registered")
    public void the_registered_customer_is_registered() {
        assertNotNull(expected.getAccountID());
    }

    /* Scenario: Register and Unregister customer are unsuccessful
    Given a customer that is not registered with DTU Pay that fails to register
    When an unsuccessful "RegisterAccountRequest" register event for the customer is received
    Then a failure "RegisterAccountSuccess" event is sent
    And the customer that cannot register is unregistered
    And an unsuccessful "UnregisterAccountRequest" unregister event for the customer is received
    And a failure "UnregisterAccountFailed" event is sent
    And the customer that could not register is unregistered */

    @Given("a customer that is not registered with DTU Pay that fails to register")
    public void a_customer_that_is_not_registered_with_DTU_Pay_that_fails_to_register() {
        assertNull(customer.getAccountID());
    }

    @When("an unsuccessful {string} register event for the customer is received")
    public void an_unsuccsessful_register_event_for_the_customer_is_received(String eventName) {

        correlationId = CorrelationId.randomId();
        Event event = new Event(eventName, new Object[] { customer, correlationId });
        customerService.handleRegisterAccountRequest(event);
    }

    @Then("a failure {string} event is sent")
    public void a_failure_register_event_is_sent(String eventName) {

        expected = new DTUPayUser(name, bankId, role);
        var event = new Event(eventName, new Object[] {expected, correlationId});
        verify(queue).publish(event);
    }

    @And("the customer that cannot register is unregistered")
    public void the_customer_that_cannot_register_is_unregistered() {
        assertNull(expected.getAccountID());
    }

    @And("an unsuccessful {string} unregister event for the customer is received")
    public void an_unsuccsessful_unregister_event_for_the_customer_is_received(String eventName) {

        correlationId = CorrelationId.randomId();
        Event event = new Event(eventName, new Object[] { customer, correlationId });
        customerService.handleUnregisterAccountRequest(event);
    }

    @And("a failure {string} event is sent")
    public void a_failure_unregister_event_is_sent(String eventName) {

        expected = new DTUPayUser(name, bankId, role);
        var event = new Event(eventName, new Object[] {expected, correlationId});
        verify(queue).publish(event);
    }

    @And("the customer that could not register is unregistered")
    public void the_customer_that_could_not_register_is_unregistered() {
        assertNull(expected.getAccountID());
    }
}
