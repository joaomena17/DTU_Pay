package accountManagementService;

import dtu.ws.fastmoney.*;
import Entities.DTUPayUser;
import Utils.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertTrue;

import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import messaging.Event;
import messaging.MessageQueue;
import services.AccountManagementFactory;
import services.AccountManagementService;

import java.math.BigDecimal;

public class RegisterCustomerSteps {

    /* Scenario: Register customer is successful
    When a successful "AccountRegistrationRequested" event for a customer "Joao" "Afonso" with balance 1000 is received
    Then a success "AccountRegistrationCompleted" event is sent
    And the customer is registered */

    private BankService bank = new BankServiceService().getBankServicePort();

    private AccountManagementService customerService = new AccountManagementFactory().getService();

    private MessageQueue queue =  customerService.queue;
    private DTUPayUser customer;
    private String bankId;
    private User user = new User();

    private CorrelationId correlationId,expectedCorrelationId,correlationId2,expectedCorrelationId2;

    @When("a successful {string} event for a customer {string} {string} with balance {int} is received")
    public void a_successful_event_for_a_customer_is_received(String eventName, String firstName, String lastName, int balance) {

        user.setCprNumber("289-1234");
        user.setFirstName(firstName);
        user.setLastName(lastName);

        try {
            bankId = bank.createAccountWithBalance(user,  new BigDecimal(balance));

        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }
        // make full name from first and last name
        String name = firstName + " " + lastName;
        customer = new DTUPayUser(name, bankId, "customer");
        correlationId=CorrelationId.randomId();

        //event is received
        customerService.handleRegisterAccountRequest(new Event(eventName, new Object[] { customer ,correlationId}));
    }
    @And("a successful {string} event is received")
    public void aSuccessfulEventIsReceived(String eventName) {
        customerService.handleRegisterUserTokenSuccess(new Event(eventName, new Object[] { customer,customerService.tokenCorrelationId}));
    }
    @Then("a success {string} event is sent")
    public void a_successful_event_is_sent(String eventName) {

        expectedCorrelationId=correlationId;
        var event = new Event(eventName, new Object[] { "1",expectedCorrelationId});
        verify(queue).publish(event);
    }

    /* Scenario: Register customer is unsuccessful
    When an unsuccessful "AccountRegistrationRequested" event for a customer "Joao" "Afonso" with balance 1000 is received
    Then a failure "AccountRegistrationFailed" event is sent
    And the customer is not registered */

    @When("an unsuccessful {string} event for a customer {string} {string} with balance {int} is received")
    public void an_unsuccessful_event_for_a_customer_is_received(String eventName, String firstName, String lastName, int balance) {

        user.setCprNumber("3-14");
        user.setFirstName(firstName);
        user.setLastName(lastName);

        //using a bank account id not valid
        bankId="DTU";
        correlationId2=CorrelationId.randomId();
        expectedCorrelationId2=correlationId2;
        // make full name from first and last name
        String name = firstName + " " + lastName;
        customer = new DTUPayUser(name, bankId, "customer");

        customerService.handleRegisterAccountRequest(new Event(eventName, new Object[] { customer,correlationId }));
    }

    @Then("a failure {string} event is sent")
    public void an_unsuccessful_event_is_sent(String eventName) {


        var event = new Event(eventName, new Object[] { "Invalid Account to register at DTU Pay",correlationId2 });
        verify(queue).publish(event);
    }

    @And("the customer is not registered")
    public void the_customer_is_not_registered() {

    }


}
