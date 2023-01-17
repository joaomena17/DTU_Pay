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

import messaging.Event;
import messaging.MessageQueue;

import java.math.BigDecimal;

public class UnregisterMerchantSteps {

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

    /* Scenario: Unregister merchant is succsessful
    Given a merchant that is registered with DTU Pay that succeeds in unregistering
    When a succsessful "UnregisterAccountRequested" unregister event for the merchant is received
    Then a success "UnregisterAccountSuccess" event is sent
    And the merchant is unregistered */

    @Given("a merchant that is registered with DTU Pay that succeeds in unregistering")
    public void a_merchant_that_is_registered_with_DTU_Pay() {

        user.setCprNumber("289-1234");
        user.setFirstName("Joao");
        user.setLastName("Silva");

        // convert balance to bigDecimal
        BigDecimal bigDecimalBalance = new BigDecimal(1000);

        try {
            bankId = bank.createAccountWithBalance(user, bigDecimalBalance);
            expectedBankId = bankId;
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }

        // make full name from first and last name
        String name = "Joao Silva";
        merchant = new DTUPayUser(name, bankId, "merchant");
    }

    @When("a succsessful {string} unregister event for the merchant is received")
    public void a_succsessful_unregister_event_for_the_merchant_is_received(String eventName) {

        Event event = new Event(eventName, new Object[] { merchant });
        merchantService.handleUnregisterAccountRequest(event);
    }

    @Then("a success {string} event is sent")
    public void a_success_event_is_sent(String eventName) {

        expectedMerchant = new DTUPayUser(expectedName, expectedBankId, expectedRole);

        var event = new Event(eventName, new Object[] { expectedMerchant });
        verify(queue).publish(event);
    }

    @And("the merchant is unregistered")
    public void the_merchant_is_unregistered() {
        assertNull(expectedMerchant.getAccountID());
    }

    /* Scenario: Unregister merchant is unsuccsessful
    Given a merchant that is registered with DTU Pay that fails to unregister
    When an unsuccsessful "UnregisterAccountRequested" unregister event for a merchant "Joao" "Afonso" is received
    Then a failure "UnregisterAccountRequestFailed" event is sent
    And the merchant is not unregistered */

    @Given("a merchant that is registered with DTU Pay that fails to unregister")
    public void a_merchant_that_is_registered_with_DTU_Pay_and_fails_to_unregister() {

        user.setCprNumber("289-1234");
        user.setFirstName("Joao");
        user.setLastName("Silva");

        // convert balance to bigDecimal
        BigDecimal bigDecimalBalance = new BigDecimal(1000);

        try {
            bankId = bank.createAccountWithBalance(user, bigDecimalBalance);
            expectedBankId = bankId;
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }

        // make full name from first and last name
        String name = "Joao Silva";
        merchant = new DTUPayUser(name, bankId, "merchant");
    }

    @When("an unsuccsessful {string} unregister event for the merchant is received")
    public void an_unsuccsessful_unregister_event_for_the_merchant_is_received(String eventName) {

        Event event = new Event(eventName, new Object[] { merchant });
        merchantService.handleUnregisterAccountRequest(event);
    }

    @Then("a failure {string} event is sent")
    public void a_failure_event_is_sent(String eventName) {

        expectedMerchant = new DTUPayUser(expectedName, expectedBankId, expectedRole);

        var event = new Event(eventName, new Object[] { expectedMerchant });
        verify(queue).publish(event);
    }

    @And("the merchant is not unregistered")
    public void the_merchant_is_not_unregistered() {
        assertNotNull(expectedMerchant.getAccountID());
    }
}
