package tokenMicroservice;
import dtu.ws.fastmoney.*;
import org.acme.*;
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

import org.acme.CreateUser;
import org.acme.RequestSingleToken;
import org.acme.Token;
import org.acme.TokenRequest;
import org.acme.TokenService.tokenService;
import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.*;
import org.junit.Test;

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
    private tokenService tokenservice = new tokenService();
    private RequestSingleToken requestSingleToken = new RequestSingleToken();
    private TokenRequest tokenRequest = new TokenRequest();


    //Scenario 1
    @Given("a customer {string} creates an account on DTU PAY and the token micro service creates him as a user")
    public void a_customer_creates_an_account_on_dtu_pay_and_the_token_micro_service_creates_him_as_a_user(String username) {
        createUser = new CreateUser(username);
        tokenservice.createUser(createUser);
        //Check if user was created
        assertTrue(tokenservice.doesUserExist(username));
    }
    @When("a customer with name {string} requests {int} tokens")
    public void a_customer_with_name_requests_tokens(String username, int number) {
        tokenRequest = new TokenRequest(username, number);
        tokenservice.requestToken(tokenRequest);
        Token testToken = tokenservice.getTokenByUser(username);
        assertNotNull(testToken);
        assertEquals(number,testToken.tokens.size());
    }

    @Then("a customer with name {string} requests {int} more tokens")
    public void a_customer_with_name_requests_tokens2(String username, int number) {
        tokenRequest = new TokenRequest(username, number);
        tokenservice.requestToken(tokenRequest);
        Token testToken = tokenservice.getTokenByUser(username);
        assertNotNull(testToken);
        assertEquals(number + 1,testToken.tokens.size());
    }

    //Scenario 2
    @Given("a customer {string} has an account on DTU pay with {int} token")
    public void a_customer_has_an_account_on_dtu_pay_with_token(String user, int number) {
        createUser = new CreateUser(user);
        tokenservice.createUser(createUser);
        tokenRequest = new TokenRequest(user, number);
        tokenservice.requestToken(tokenRequest);
        assertTrue(tokenservice.doesUserExist(user));
        Token testToken = tokenservice.getTokenByUser(user);
        assertNotNull(testToken);
        assertEquals(number, testToken.tokens.size());
    }

    @When("the customer {string} request {int} token")
    public void the_customer_request_token(String user, Integer number) {
        createUser = new CreateUser(user);
        tokenservice.createUser(createUser);
        tokenRequest = new TokenRequest(user, number);
        tokenservice.requestToken(tokenRequest);
    }

    @Then("customer {string} does not receive more tokens")
    public void customer_receives_error_that_he_has_to_many_tokens_to_make_an_request(String user) {
        assertTrue(tokenService.doesUserExist(user));
        Token testToken = tokenService.getTokenByUser(user);
        assertNotNull(testToken);
        assertEquals(2, testToken.tokens.size());
    }
    //Scenario 3
    @Given("Customer {string} has an account and {int} tokens")
    public void a_customer_has_an_account_on_dtu_pay_with_token2(String user, int number) {
        createUser = new CreateUser(user);
        tokenservice.createUser(createUser);
        tokenRequest = new TokenRequest(user, number);
        tokenservice.requestToken(tokenRequest);
        assertTrue(tokenservice.doesUserExist(user));
        Token testToken = tokenservice.getTokenByUser(user);
        assertNotNull(testToken);
        assertEquals(number, testToken.tokens.size());
    }

    @When("The customer {string} requests to few or {int} tokens")
    public void the_customer_requests_to_few_or_tokens(String user, Integer number) {
        createUser = new CreateUser(user);
        tokenservice.createUser(createUser);
        tokenRequest = new TokenRequest(user, number);
        tokenservice.requestToken(tokenRequest);
        Token testToken = tokenservice.getTokenByUser(user);
        assertNotNull(testToken);
        assertEquals(1, testToken.tokens.size());
    }

    //Scenario 4
    @Given("Customer {string} creates a new account")
    public void Customer_John_Doe_has_an_account_and_1_tokens(String user) {
        createUser = new CreateUser(user);
        tokenservice.createUser(createUser);
        //Check if user was created
        assertTrue(tokenservice.doesUserExist(user));
    }
    @Then("The customer {string} requests {int} new token")
    public void The_customer_John_Doe_requests_1_new_token(String user, int number){
        tokenRequest = new TokenRequest(user, number);
        tokenservice.requestToken(tokenRequest);
        Token testToken = tokenservice.getTokenByUser(user);
        assertNotNull(testToken);
        assertEquals(number, testToken.tokens.size());
    }
    @Then("The customer {string} requests the token, uses the token and checks if he gets the same token again")
    public void The_customer_John_Doe_requests_the_token(String user){
        requestSingleToken = new RequestSingleToken(user, "");
        String testToken = tokenservice.getSingleToken(requestSingleToken);
        requestSingleToken = new RequestSingleToken(user, testToken);
        tokenservice.deleteToken(requestSingleToken);
        assertNotEquals(testToken, tokenservice.getSingleToken(requestSingleToken));

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
