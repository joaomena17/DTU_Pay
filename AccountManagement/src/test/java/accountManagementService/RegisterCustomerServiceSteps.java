package accountManagementService;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;

import dtu.ws.fastmoney.*;
import customerservice.*;

/* Scenario: Customer registers successfully
    Given a customer with name "John Doe" and bank account with balance 1000
    When the customer registers with DTU Pay
    Then the customer is saved in the customer list
    And the customer can get correctly retrieved from the list */

public class RegisterCustomerServiceSteps {

    private User user = new User();
    private String bankId;
    private Customer customer;
    private CustomerService customerService = new CustomerService();
    BankService bank = new BankServiceService().getBankServicePort();

    @Given("a customer with name {string} {string} and bank account with balance {int}")
    public void a_customer_with_name_and_bank_id(String firstName, String lastName, int balance) {

        user.setCprNumber("271299-1234");
        user.setFirstName(firstName);
        user.setLastName(lastName);

        // convert balance to bigDecimal
        BigDecimal bigDecimalBalance = new BigDecimal(balance);
        // make full name from first and last name
        String name = firstName + " " + lastName;

        try {
            bankId = bank.createAccountWithBalance(user, bigDecimalBalance);

            /* DEBUG */
            /* System.out.println("\n\n>>> BankID: " + bankId + "\n\n"); */

        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }

        customer = new Customer(name, bankId);
    }

    @When("the customer registers with DTU Pay")
    public void the_customer_registers_with_dtu_pay() {
        customerService.registerCustomer(customer);
    }

    @Then("the customer is saved in the customer list")
    public void the_customer_is_saved_in_the_system() {
        assertTrue(customerService.getCustomerList().contains(customer));
    }

    @And("the customer can be correctly retrieved from the list")
    public void the_customer_can_get_correctly_retrieved_from_the_list() {
        assertEquals(customer, customerService.getCustomer(customer.getCustomerID()));
    }

    @After
    public void tearDown() {

        try {
            bank.retireAccount(bankId);
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }

        customerService.unregisterCustomer(customer);
    }

}
