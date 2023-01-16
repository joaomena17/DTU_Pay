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

public class RegisterMerchantSteps {

    /* Scenario: Register merchant is succsessful
    When a succsessful "AccountRegistrationRequested" event for a merchant "Joao" "Silva" with balance 1000 is received
    Then a success "AccountRegistrationCompleted" event is sent
    And the merchant is registered */

    private BankService bank = new BankServiceService().getBankServicePort();
    private MessageQueue queue = mock(MessageQueue.class);
    private AccountService merchantService = new AccountService();
    private DTUPayUser merchant;
    private DTUPayUser expectedMerchant;
    private String bankId;
    private String expectedName = "Joao Silva";
    private String expectedBankId;
    private String expectedRole = "merchant";
    private User user = new User();


    @When("a succsessful {string} event for a merchant {string} {string} with balance {int} is received")
    public void a_succsessful_event_for_a_merchant_is_received(String eventName, String firstName, String lastName, int balance) {

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
        merchant = new DTUPayUser(name, bankId, "merchant");

        merchantService.handleRegisterAccountRequest(new Event(eventName, new Object[] { merchant }));
    }

    @Then("a success {string} event is sent")
    public void a_successful_event_is_sent(String eventName) {

        expectedMerchant = new DTUPayUser(expectedName, expectedBankId, expectedRole);

        var event = new Event(eventName, new Object[] { expectedMerchant });
        verify(queue).publish(event);
    }

    @And("the merchant is registered")
    public void the_merchant_is_registered() {
        assertNotNull(expectedMerchant.getAccountID());
    }

    /* Scenario: Register merchant is unsuccsessful
    When an unsuccsessful "AccountRegistrationRequested" event for a merchant "Joao" "Silva" with balance 1000 is received
    Then a failure "AccountRegistrationFailed" event is sent
    And the merchant is not registered */

    @When("an unsuccsessful {string} event for a merchant {string} {string} with balance {int} is received")
    public void an_unsuccsessful_event_for_a_merchant_is_received(String eventName, String firstName, String lastName, int balance) {

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
        merchant = new DTUPayUser(name, bankId, "merchant");

        merchantService.handleRegisterAccountRequest(new Event(eventName, new Object[] { merchant }));
    }

    @Then("a failure {string} event is sent")
    public void an_unsuccessful_event_is_sent(String eventName) {

        expectedMerchant = new DTUPayUser(expectedName, expectedBankId, expectedRole);

        var event = new Event(eventName, new Object[] { expectedMerchant });
        verify(queue).publish(event);
    }

    @And("the merchant is not registered")
    public void the_merchant_is_not_registered() {
        assertTrue(!merchantService.getAccountList("merchant").contains(expectedMerchant));
    }
}
