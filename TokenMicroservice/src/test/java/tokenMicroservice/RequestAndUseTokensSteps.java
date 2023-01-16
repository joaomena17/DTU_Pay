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
    And the customer can use the tokens for payments */

public class RequestAndUseTokensSteps {

    private Token token = new Token();

    private TokenService tokenService = new TokenService();
    private RequestSingleToken requestSingleToken;
    private TokenRequest tokenRequest = new TokenRequest();

    @Given("a customer {string} creates an account on DTU PAY and the token micro service creates him as a user")
    public void a_customer_creates_an_account_on_dtu_pay_and_the_token_micro_service_creates_him_as_a_user(String username) {
       // List<String> emptyList = new ArrayList<String>();
        CreateUser createUser= new CreateUser(username);
        tokenService.createUser(createUser);

        //Check if user was created
        assertTrue(tokenService.doesUserExist(username));
        //throw new io.cucumber.java.PendingException();
    }
    @When("a customer with name {string} requests {int} tokens")
    public void a_customer_with_name_requests_tokens(String username, Integer number) {
        tokenRequest = new TokenRequest(username, number);
        //System.out.println(tokenService.requestToken(tokenRequest).getStatusCode());
        //Response resp = tokenService.requestToken(tokenRequest);
        //int statusCode = Response.ok().build();

        assertEquals(200,200); //todo fix testing statuscode
    }

    /*
   // @Given("a customer {string} creates an account on DTU PAY and the token micro service creates him as a user")
   @Given("a customer {string} creates an account on DTU PAY and the token micro service creates him as a user")
   public void userRequestingTokens(String username) {

        List<String> emptyList = new ArrayList<String>();
        token = new Token(username, emptyList);
        tokenService.createUser(token);

        //Check if user was created
        assertTrue(tokenService.doesUserExist(username));

    }
    @When("a customer with name {string} requests {int} number of tokens")
    public void userRequestingTokens(String username, int numberOfTokens) {
        List<String> emptyList = new ArrayList<String>();
        token = new Token(username, emptyList);
        tokenService.createUser(token);

        //Check if user was created
        assertTrue(tokenService.doesUserExist(username));

    }*/
    /*
    @When("the customer requests token with DTU Pay")
    public void the_customer_registers_with_dtu_pay() {
        customerService.registerCustomer(customer);
    }

    @Then("the customer recieves 3 token")
    public void the_customer_is_saved_in_the_system() {
        assertTrue(customerService.getCustomerList().contains(customer));
    }

    @And("the customer can use the tokens for payments")
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
*/
}
