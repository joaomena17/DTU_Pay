package accountManagementService;

import dtu.ws.fastmoney.*;
import accountservice.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertTrue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.After;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import messaging.Event;
import messaging.MessageQueue;

import java.math.BigDecimal;

public class RegisterCustomerSteps {
    
    /* Scenario: Register customer is succsessful
    When a succsessful "AccountRegistrationRequested" event for a customer "Joao" "Afonso" is received
    Then a "AccountRegistrationCompleted" event is sent
    And the customer is registered */

    private MessageQueue queue = mock(MessageQueue.class);
    private AccountService customerService = new AccountService();
    private DTUPayUser customer;
    private DTUPayUser expected;
    private String expectedName = "Joao Afonso";
    private String expectedBankId = "bankId";
    private String expectedRole = "customer";

    @When("a succsessful {string} event for a customer {string} {string} is received")
    public void a_succsessful_event_for_a_customer_is_received(String eventName, String firstName, String lastName) {
        
        String name = firstName + " " + lastName;

        // (???) is this bankId ok?
        customer = new DTUPayUser(name, "bankId", "customer");

        customerService.handleAccountRegistrationRequested(new Event(eventName, new Object[] {customer}));
    }

    @Then("a {string} event is sent")
    public void a_event_is_sent(String eventName) {

        expected = new DTUPayUser(expectedName, expectedBankId, expectedRole);
        var event = new Event(eventName, new Object[] {expected});
		verify(queue).publish(event);
    }

    @And("the customer is registered")
    public void the_customer_is_registered() {
        assertNotNull(expected.getAccountID()); // (???) why expected
    }
}
