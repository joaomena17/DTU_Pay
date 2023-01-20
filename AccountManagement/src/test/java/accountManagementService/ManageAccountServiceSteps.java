package accountManagementService;

/* @author: Joao Silva s222961 */

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
    Given a customer that is not registered with DTU Pay
    When a register event for the customer is received
    #Then a TokenUserRequest event is sent
    #And a successful "RegisterUserTokenSuccess" event is received
    Then a success register event is sent
    And a unregister event for the customer is received
    And a unregister success event is sent */

    @Given("a customer that is not registered with DTU Pay")
    public void a_customer_that_is_not_registered_with_DTU_Pay() {

        assertNull(customer.getAccountID());
    }

    @When("a register event for the customer is received")
    public void a_register_event_for_the_customer_is_received() {

        correlationId = CorrelationId.randomId();
        Event event = new Event(EventTypes.REGISTER_ACCOUNT_REQUEST, new Object[] { customer, correlationId });
        // new Thread(() -> {
        expected=customerService.handleRegisterAccountRequest(event);
        customer.setAccountID(expected);
        // }).start();
    }

    /* @And("a {string} event is received")
    public void a_succsessful_register_event_is_received(String eventName) {

        var tokenCorrId = customerService.tokenCorrelationId;
        Event event = new Event(eventName, new Object[] { true, tokenCorrId });
        customerService.handleRegisterUserTokenSuccess(event);
    } */

    @Then("a success register event is sent")
    public void a_success_register_event_is_sent() {

        var event = new Event(EventTypes.REGISTER_ACCOUNT_COMPLETED, new Object[] {expected, correlationId});
        // verify(customerService.queue).publish(event);
    }

    @And("an unregister event for the customer is received")
    public void a_succsessful_unregister_event_for_the_customer_is_received() {

        correlationId = CorrelationId.randomId();
        Event event = new Event(EventTypes.UNREGISTER_ACCOUNT_REQUEST, new Object[] { customer, correlationId });
        customerService.handleUnregisterAccountRequest(event);
    }

    @And("an unregister success event is sent")
    public void an_unregister_success_event_is_sent() {

        var event = new Event(EventTypes.UNREGISTER_ACCOUNT_SUCCESS, new Object[] {true, correlationId});
        // verify(customerService.queue).publish(event);
    }

    /* Scenario: Register and Unregister customer are unsuccessful
    Given a customer that is not registered with DTU Pay
    When a register event for the customer is received
    Then a failure register event is sent
    And an unregister event for the customer is received
    And an unregister failure event is sent */

    // @Given("a customer that is not registered with DTU Pay")

    // @When("a register event for the customer is received")

    @Then("a failure register event is sent")
    public void a_failure_register_event_is_sent() {

        var event = new Event(EventTypes.REGISTER_ACCOUNT_FAILED, new Object[] {"", correlationId});
        // verify(queue).publish(event);
    }

    // @And("an unregister event for the customer is received")

    @And("an unregister failure event is sent")
    public void a_unregister_failure_event_is_sent() {

        var event = new Event(EventTypes.UNREGISTER_ACCOUNT_FAILED, new Object[] {false, correlationId});
        // verify(queue).publish(event);
    }

}
