package resources;

import dtu.ws.fastmoney.User;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.BankServiceException_Exception;

import services.Customer;

import jakarta.ws.rs.core.Response;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

/* Scenario: Customer registers and unregisters successfully
    Given a customer with name "John" "Doe" and bank account with balance 1000
    When the customer registers with DTU Pay using "json"
    Then the register request is successful
    And the customer unregisters from DTU Pay using "json"
    And the unregister request is successful */

public class ManageCustomerServiceSteps {

    private User user = new User();
    private String bankId;
    private BankService bank = new BankServiceService().getBankServicePort();
    private CustomerResource customerResource = new CustomerResource();
    private Customer customer;
    private Response response;

    @Given("a customer with name {string} {string} and bank account with balance {int}")
    public void a_customer_with_name_and_bank_account_with_balance(String firstName, String lastName, Integer balance) {

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
        customer = new Customer(name, bankId);

    }

    @When("the customer registers with DTU Pay using {string}")
    public void the_customer_registers_with_dtu_pay_using(String mediaType) {

        // build media type string from media type
        String mediaTypeString = "application/" + mediaType;

        response = customerResource.registerCustomerAccount(customer, mediaTypeString);
    }

    @Then("the register request is successful")
    public void the_request_is_successful() {
        boolean success = response.getStatus() == 200;
        assertTrue(success);
    }

    @And("the customer unregisters from DTU Pay using {string}")
    public void the_customer_unregisters_from_dtu_pay_using(String mediaType) {

        // build media type string from media type
        String mediaTypeString = "application/" + mediaType;
        String customerId = customer.getCustomerID();

        response = customerResource.unregisterCustomerAccount(customerId, mediaTypeString);
    }

    @And("the unregister request is successful")
    public void the_unregister_request_is_successful() {
        boolean success = response.getStatus() == 200;
        assertTrue(success);
    }
    
}
