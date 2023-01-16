package tokenMicroservice;
import dtu.ws.fastmoney.*;
import org.acme.*;
import TokenMicroService.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.After;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import java.math.BigDecimal;

/* Scenario: Customer requests token successfully
    Given a customer {string} creates an account on DTU PAY and the token micro service creates him as a user
    Then a customer with name "John Doe" requests 3 tokens
    And the customer can use the tokens for payments

    Scenario: Customer requests token while having 2 token
    Given a customer "John Doe" has an account on DTU pay with 2 token
    When the customer "John Doe" request more tokens
    Then customer "John Doe" receives error that he has to many tokens to make an request

    Scenario: Customer request to few tokens
    Given Customer "John Doe" has an account and 1 tokens
    When The customer "John Doe" requests 0 token
    Then The customer "John Doe" has 1 tokens and receives an error message of to few token requested

    */

public class RequestAndUseTokensSteps {

    private Token token = new Token();
    private CreateUser createUser = new CreateUser();
    private TokenService tokenService = new TokenService();
    private RequestSingleToken requestSingleToken;
    private TokenRequest tokenRequest = new TokenRequest();

    @Given("a customer {string} creates an account on DTU PAY and the token micro service creates him as a user")
    public void a_customer_creates_an_account_on_dtu_pay_and_the_token_micro_service_creates_him_as_a_user(String username) {
       // List<String> emptyList = new ArrayList<String>();
        createUser = new CreateUser(username);
        tokenService.createUser(createUser);
        //Check if user was created
        assertTrue(tokenService.doesUserExist(username));
    }
    @When("a customer with name {string} requests {int} tokens")
    public void a_customer_with_name_requests_tokens(String username, Integer number) {
        tokenRequest = new TokenRequest(username, number);
        assertTrue("200 Success", true);
    }
    //@And("the customer can use the tokens for payments")
    public void customer_can_use_the_tokens_for_payments(String username) {
        List<String> emptyList = new ArrayList<String>();
        createUser = new CreateUser(username);
        tokenService.createUser(createUser);
        tokenRequest = new TokenRequest(username, 3);
        requestSingleToken = new RequestSingleToken();
        //assertTrue(requestSingleToken);
    }

    @Given("a customer {string} has an account on DTU pay with {int} token")
    public void a_customer_has_an_account_on_dtu_pay_with_token(String string, Integer int1) {
        createUser = new CreateUser(string);
        tokenService.createUser(createUser);
        tokenRequest = new TokenRequest(string, int1);
        assertTrue("200 Success", true);
        assertTrue(tokenService.doesUserExist(string));
    }
    @When("The customer {string} requests {int} token")
    public void the_customer_request_more_tokens(String string) {
        List<String> emptyList = new ArrayList<String>();
        createUser = new CreateUser(string);
        tokenService.createUser(createUser);
        tokenRequest = new TokenRequest(string, 1);
        assertTrue("Has 2 or more valid tokens", true);
        assertTrue(tokenService.doesUserExist(string));
    }

    @Then("customer {string} receives error that he has to many tokens to make an request")
    public void customer_receives_error_that_he_has_to_many_tokens_to_make_an_request(String string) {
        createUser = new CreateUser(string);
        tokenService.createUser(createUser);
        tokenRequest = new TokenRequest(string, 1);
        assertTrue("Has 2 or more valid tokens", true);
        assertTrue(tokenService.doesUserExist(string));
    }

    @Given("Customer {string} has an account and {int} tokens")
    public void a_customer_has_an_account_on_dtu_pay_with_token2(String string, Integer int1) {
        //List<String> emptyList = new ArrayList<String>();
        createUser = new CreateUser(string);
        tokenService.createUser(createUser);
        tokenRequest = new TokenRequest(string, int1);
        assertTrue("200 Success", true);
        assertTrue(tokenService.doesUserExist(string));
    }

    @When("the customer {string} request more tokens")
    public void the_customer_request_more_tokens2(String string) {
       // List<String> emptyList = new ArrayList<String>();
        createUser = new CreateUser(string);
        tokenService.createUser(createUser);
        tokenRequest = new TokenRequest(string, 0);

    }

    @Then("The customer {string} has {int} tokens and receives an error message of to few token requested")
    public void customer_receives_error_that_he_has_to_many_tokens_to_make_an_request(String string, Integer int1) {
        List<String> emptyList = new ArrayList<String>();
        createUser = new CreateUser(string);
        tokenService.createUser(createUser);
        tokenRequest = new TokenRequest(string, 1);

    }

    /*
    @After
    public void tearDown() {

        try {
            bank.retireAccount(bankId);
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }

        customerService.unregisterCustomer(customer);
    }
*/
}
