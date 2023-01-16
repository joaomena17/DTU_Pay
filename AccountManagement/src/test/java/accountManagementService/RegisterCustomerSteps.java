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
import io.cucumber.java.Before;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import dtu.ws.fastmoney.*;

import messaging.Event;
import messaging.MessageQueue;

import java.math.BigDecimal;

public class RegisterCustomerSteps {

    /* Scenario: Register customer is succsessful
    When a succsessful "AccountRegistrationRequested" event for a customer "Joao" "Afonso" with balance 1000 is received
    Then a success "AccountRegistrationCompleted" event is sent
    And the customer is registered */

    private BankService bank = new BankServiceService().getBankServicePort();
    private MessageQueue queue = mock(MessageQueue.class);
    private AccountService customerService = new AccountService();
    private DTUPayUser customer;
    private DTUPayUser expectedCustomer;
    private String bankId;
    private String expectedName = "Joao Afonso";
    private String expectedBankId;
    private String expectedRole = "customer";
    private User user = new User();


    @When("a succsessful {string} event for a customer {string} {string} with balance {int} is received")
    public void a_succsessful_event_for_a_customer_is_received(String eventName, String firstName, String lastName, int balance) {

        user.setCprNumber("289-1234");
        user.setFirstName(firstName);
        user.setLastName(lastName);

        // convert balance to bigDecimal
        BigDecimal bigDecimalBalance = new BigDecimal(balance);

        try {
            bankId = bank.createAccountWithBalance(user, bigDecimalBalance);
            expectedBankId = bankId;
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }

        // make full name from first and last name
        String name = firstName + " " + lastName;
        customer = new DTUPayUser(name, bankId, "customer");

        customerService.handleRegisterAccountRequest(new Event(eventName, new Object[] { customer }));
    }

    @Then("a success {string} event is sent")
    public void a_successful_event_is_sent(String eventName) {

        expectedCustomer = new DTUPayUser(expectedName, expectedBankId, expectedRole);

        var event = new Event(eventName, new Object[] { expectedCustomer });
        verify(queue).publish(event);
    }

    @And("the customer is registered")
    public void the_customer_is_registered() {
        assertNotNull(expectedCustomer.getAccountID());
    }

    /* Scenario: Register customer is unsuccsessful
    When an unsuccsessful "AccountRegistrationRequested" event for a customer "Joao" "Afonso" with balance 1000 is received
    Then a failure "AccountRegistrationFailed" event is sent
    And the customer is not registered */

    @When("an unsuccsessful {string} event for a customer {string} {string} with balance {int} is received")
    public void an_unsuccsessful_event_for_a_customer_is_received(String eventName, String firstName, String lastName, int balance) {

        user.setCprNumber("289-1234");
        user.setFirstName(firstName);
        user.setLastName(lastName);

        // convert balance to bigDecimal
        BigDecimal bigDecimalBalance = new BigDecimal(balance);

        try {
            bankId = bank.createAccountWithBalance(user, bigDecimalBalance);
            expectedBankId = bankId;
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }

        // make full name from first and last name
        String name = firstName + " " + lastName;
        customer = new DTUPayUser(name, bankId, "customer");

        customerService.handleRegisterAccountRequest(new Event(eventName, new Object[] { customer }));
    }

    @Then("a failure {string} event is sent")
    public void an_unsuccessful_event_is_sent(String eventName) {

        expectedCustomer = new DTUPayUser(expectedName, expectedBankId, expectedRole);

        var event = new Event(eventName, new Object[] { expectedCustomer });
        verify(queue).publish(event);
    }

    @And("the customer is not registered")
    public void the_customer_is_not_registered() {
        assertTrue(!customerService.getAccountList("customer").contains(expectedCustomer));
    }
}
