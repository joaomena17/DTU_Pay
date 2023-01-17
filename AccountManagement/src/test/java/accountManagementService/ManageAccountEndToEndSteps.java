package accountManagementService;

import dtu.ws.fastmoney.*;
import services.*;
import Entities.*;
import handlers.*;

import java.util.concurrent.CompletableFuture;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

public class ManageAccountEndToEndSteps {

    private User user = new User();
    private String name = "Joao Afonso";
    private String bankId;
    private String role = "customer";
    private BankService bank = new BankServiceService().getBankServicePort();
    private DTUPayUser customer;
    private CompletableFuture<DTUPayUser> result = new CompletableFuture<DTUPayUser>();
    // private AccountManagementService customerService = new AccountManagementFactory().getService();
    private AccountService customerService = new AccountService();

    /* @Before
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
    } */
    
    /* Scenario:
    Given a customer that is not registered with DTU Pay
    When the customer is being registered
    Then the customer is registered in the system
    And the customer is being unregistered
    And the customer is unregistered from the system */

    @Given("a customer that is not registered with DTU Pay")
    public void a_customer_that_is_not_registered_with_DTU_Pay() {
        assertNull(customer.getAccountID());
    }

    @When("the customer is being registered")
    public void the_customer_is_being_registered() {
        // result.complete(customerService.registerAccount(customer));
    }

    @Then("the customer is registered in the system")
    public void the_customer_is_registered() {
        // test needs to go here (???)
        assertNotNull(result.join().getAccountID());
    }

    @And("the customer is being unregistered")
    public void the_customer_is_being_unregistered() {
        // result.complete(customerService.unregisterAccount(customer));
    }

    @And("the customer is unregistered from the system")
    public void the_customer_is_unregistered() {
        assertNull(result.join().getAccountID());
    }
}
