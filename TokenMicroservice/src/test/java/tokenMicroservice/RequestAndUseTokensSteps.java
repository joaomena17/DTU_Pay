package tokenMicroservice;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.*;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.acme.Token;
import org.acme.TokenService.tokenService;
/* author:Þorfinnur s212953 and Kári s212434 */


public class RequestAndUseTokensSteps {

    private Token token = new Token();
    private tokenService service = new tokenService();


    //Scenario 1
    @Given("a customer {string} creates an account on DTU PAY and the token micro service creates him as a user")
    public void a_customer_creates_an_account_on_dtu_pay_and_the_token_micro_service_creates_him_as_a_user(String username) {
        service.registerUser(username);
        //Check if user was created
        assertTrue(service.doesUserExist(username));
    }
    @When("a customer with name {string} requests {int} tokens")
    public void a_customer_with_name_requests_tokens(String username, int number) {
        service.requestTokenMessageQueue(username, number);
        Token testToken = service.getTokenByUser(username);
        assertNotNull(testToken);
        assertEquals(number,testToken.tokens.size());
    }

    @Then("a customer with name {string} requests {int} more tokens")
    public void a_customer_with_name_requests_tokens2(String username, int number) {
        service.requestTokenMessageQueue(username, number);
        Token testToken = service.getTokenByUser(username);
        assertNotNull(testToken);
        assertEquals(number + 1,testToken.tokens.size());
    }

    //Scenario 2
    @Given("a customer {string} has an account on DTU pay with {int} token")
    public void a_customer_has_an_account_on_dtu_pay_with_token(String user, int number) {
        service.registerUser(user);
        service.requestTokenMessageQueue(user, number);
        assertTrue(service.doesUserExist(user));
        Token testToken = service.getTokenByUser(user);
        assertNotNull(testToken);
        assertEquals(number, testToken.tokens.size());
    }

    @When("the customer {string} request {int} token")
    public void the_customer_request_token(String user, Integer number) {
        service.registerUser(user);
        service.requestTokenMessageQueue(user, number);
    }

    @Then("customer {string} does not receive more tokens")
    public void customer_receives_error_that_he_has_to_many_tokens_to_make_an_request(String user) {
        assertTrue(service.doesUserExist(user));
        Token testToken = service.getTokenByUser(user);
        assertNotNull(testToken);
        assertEquals(2, testToken.tokens.size());
    }
    //Scenario 3
    @Given("Customer {string} has an account and {int} tokens")
    public void a_customer_has_an_account_on_dtu_pay_with_token2(String user, int number) {
        service.registerUser(user);
        service.requestTokenMessageQueue(user, number);
        assertTrue(service.doesUserExist(user));
        Token testToken = service.getTokenByUser(user);
        assertNotNull(testToken);
        assertEquals(number, testToken.tokens.size());
    }

    @When("The customer {string} requests to few or {int} tokens")
    public void the_customer_requests_to_few_or_tokens(String user, Integer number) {
        service.registerUser(user);
        service.requestTokenMessageQueue(user, number);
        Token testToken = service.getTokenByUser(user);
        assertNotNull(testToken);
        assertEquals(1, testToken.tokens.size());
    }

    //Scenario 4
    @Given("Customer {string} creates a new account")
    public void Customer_John_Doe_has_an_account_and_1_tokens(String user) {
        service.registerUser(user);
        assertTrue(service.doesUserExist(user));
    }
    @Then("The customer {string} requests {int} new token")
    public void The_customer_John_Doe_requests_1_new_token(String user, int number){
        service.requestTokenMessageQueue(user, number);
        Token testToken = service.getTokenByUser(user);
        assertNotNull(testToken);
        assertEquals(number, testToken.tokens.size());
    }
    @Then("The customer {string} requests the token, uses the token and checks if he gets the same token again")
    public void The_customer_John_Doe_requests_the_token(String user){
        String testToken = service.getSingleToken(user);
        service.validateToken(testToken);
        assertNotEquals(testToken, service.getSingleToken(testToken));

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
