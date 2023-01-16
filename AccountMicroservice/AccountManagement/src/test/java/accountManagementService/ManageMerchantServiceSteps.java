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

import java.math.BigDecimal;

/* Scenario: Merchant registers and unregisters successfully
    Given a merchant with name "Tiago" "Silverio" and bank account with balance 1000
    When the merchant registers with DTU Pay
    Then the merchant is saved in the merchant list
    And the merchant can be retrieved from the merchant list
    And the merchant unregisters from DTU Pay
    And the merchant is removed from the merchant list */

public class ManageMerchantServiceSteps {

    private User user = new User();
    private String bankId;
    private BankService bank = new BankServiceService().getBankServicePort();
    private DTUPayUser merchant;
    private AccountService merchantService = new AccountService();

    @Given("a merchant with name {string} {string} and bank account with balance {int}")
    public void a_merchant_with_name_and_bank_id(String firstName, String lastName, int balance) {

        user.setCprNumber("499-1234");
        user.setFirstName(firstName);
        user.setLastName(lastName);

        // convert balance to bigDecimal
        BigDecimal bigDecimalBalance = new BigDecimal(balance);

        try {
            bankId = bank.createAccountWithBalance(user, bigDecimalBalance);
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }

        // make full name from first and last name
        String name = firstName + " " + lastName;
        merchant = new DTUPayUser(name, bankId, "merchant");
    }

    @When("the merchant registers with DTU Pay")
    public void the_merchant_registers_with_dtu_pay() {
        merchantService.registerAccount(merchant);
    }

    @Then("the merchant is saved in the merchant list")
    public void the_merchant_is_saved_in_the_system() {
        assertTrue(merchantService.getAccountList("merchant").contains(merchant));
    }

    @And("the merchant can be retrieved from the merchant list")
    public void the_merchant_can_get_correctly_retrieved_from_the_list() {
        assertEquals(merchant, merchantService.getAccount(merchant.getAccountID(), "merchant"));
    }

    @And("the merchant unregisters from DTU Pay")
    public void the_merchant_unregisters_from_dtu_pay() {
        merchantService.unregisterAccount(merchant);
    }

    @And("the merchant is removed from the merchant list")
    public void the_merchant_is_removed_from_the_merchant_list() {
        assertTrue(!merchantService.getAccountList("merchant").contains(merchant));
    }

    @After
    public void tearDown() {

        try {
            bank.retireAccount(bankId);
        } catch (BankServiceException_Exception e) {
            // e.printStackTrace();
        }
    }

}