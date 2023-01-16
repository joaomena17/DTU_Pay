package resources;

import dtu.ws.fastmoney.User;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.BankServiceException_Exception;

import services.Merchant;

import jakarta.ws.rs.core.Response;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.cucumber.java.After;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

/* Scenario: Merchant registers and unregisters successfully
    Given a merchant with name "John" "Doe" and bank account with balance 1000
    When the merchant registers with DTU Pay using "json"
    Then the register merchant request is successful
    And the merchant unregisters from DTU Pay using "json"
    And the unregister merchant request is successful */

public class ManageMerchantServiceSteps {

    private User user = new User();
    private String bankId;
    private BankService bank = new BankServiceService().getBankServicePort();
    private MerchantResource merchantResource = new MerchantResource();
    private Merchant merchant;
    private Response response;

    @Given("a merchant with name {string} {string} and bank account with balance {int}")
    public void a_merchant_with_name_and_bank_account_with_balance(String firstName, String lastName, Integer balance) {

        user.setCprNumber("381299-1234");
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
        merchant = new Merchant(name, bankId);

    }

    @When("the merchant registers with DTU Pay using {string}")
    public void the_merchant_registers_with_dtu_pay_using(String mediaType) {

        // build media type string from media type
        String mediaTypeString = "application/" + mediaType;

        response = merchantResource.registerMerchantAccount(merchant, mediaTypeString);
    }

    @Then("the register merchant request is successful")
    public void the_merchant_request_is_successful() {
        boolean success = response.getStatus() == 200;
        assertTrue(success);
    }

    @And("the merchant unregisters from DTU Pay using {string}")
    public void the_merchant_unregisters_from_dtu_pay_using(String mediaType) {

        // build media type string from media type
        String mediaTypeString = "application/" + mediaType;
        String merchantId = merchant.getMerchantID();

        response = merchantResource.unregisterMerchantAccount(merchantId, mediaTypeString);
    }

    @And("the unregister merchant request is successful")
    public void the_unregister_merchant_request_is_successful() {
        boolean success = response.getStatus() == 200;
        assertTrue(success);
    }

    /* @After
    public void tearDown() {

        try {
            bank.retireAccount(bankId);
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }
    } */
    
}
