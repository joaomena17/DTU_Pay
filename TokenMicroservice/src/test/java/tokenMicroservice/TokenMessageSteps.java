package tokenMicroservice;

import Utils.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.acme.Token;
import org.acme.TokenService.tokenService;
import org.acme.Utils.EventTypes;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.mock;
import org.acme.TokenService.interfaceTokenService;

/* author:Þorfinnur s212953 and Kári s212434 */

public class TokenMessageSteps {

    private CompletableFuture<Event> publishedEvent = new CompletableFuture<>();

    private MessageQueue queue = mock(MessageQueue.class);

    private interfaceTokenService interfaceTokenService;
    private tokenService service = new tokenService(queue, interfaceTokenService);
    private Token token = new Token();
    CorrelationId corrId;



    public TokenMessageSteps() {
        interfaceTokenService = new interfaceTokenService() {
            @Override
            public boolean registerUser(String user) {
                return false;
            }

            @Override
            public String validateToken(String t) {
                return null;
            }

            @Override
            public String getSingleToken(String user) {
                return null;
            }

            @Override
            public List<String> requestTokenMessageQueue(String user, int number) {
                return null;
            }
        };
    }
//Scenario 1
    @Given("A customer is created with the username {string}")
    public void A_customer_is_created_with_the_username(String user) {
        corrId = CorrelationId.randomId();
        service.handleRegisterUserTokenRequest(new Event(EventTypes.REGISTER_TOKEN_USER,new Object[]{user, corrId}));
    }

    @Then("a customer with name {string} then exists")
    public void a_customer_with_name_then_exists(String user) {
        assertEquals(service.doesUserExist(user), true);

    }

/// Scenario 2
    @Given("A customer {string} has an account on DTU pay")
    public void A_customer_has_account_and_tokens(String user) {
        corrId = CorrelationId.randomId();
        service.handleRegisterUserTokenRequest(new Event(EventTypes.REGISTER_TOKEN_USER,new Object[]{user, corrId}));
    }

    @When("The customer {string} requests {int} tokens")
    public void The_customer_request_token(String user, int number) {
        corrId = CorrelationId.randomId();
        service.handleRequestToken(new Event(EventTypes.REQUEST_TOKEN,new Object[]{user,corrId, number}));
    }

    @Then("Customer {string} he should have {int} tokens")
    public void Customer_does_not_receive_more_tokens(String user, int number) {
        assertEquals(number,service.getAllTokensByUser(user).size());
    }

    @When("The customer {string} requests again {int} tokens")
    public void The_customer_request_token_again(String user, int number) {
        corrId = CorrelationId.randomId();
        //service.handleRequestToken(new Event(EventTypes.REQUEST_TOKEN,new Object[]{service.getTokenByUser(user).tokens.get(0),corrId}));

        service.handleRequestToken(new Event(EventTypes.REQUEST_TOKEN,new Object[]{user,corrId, number}));
    }

    @Then("Customer {string} he should still have {int} tokens")
    public void Customer_does_not_receive_more_tokens_still(String user, int number) {
        assertEquals(number,service.getAllTokensByUser(user).size());
    }
/// Scenario 3
    @Given("A customer {string} has an account on DTU pay with {int} token")
    public void A_customer_has_account_and_tokens2(String user, int number) {
        corrId = CorrelationId.randomId();
        service.handleRegisterUserTokenRequest(new Event(EventTypes.REGISTER_TOKEN_USER,new Object[]{user, corrId}));
        service.handleRequestToken(new Event(EventTypes.REQUEST_TOKEN, new Object[]{user,corrId, number}));

    }

    @When("The customer {string} validates a tokens")
    public void The_customer_validates_a_token(String user) {
        corrId = CorrelationId.randomId();

        service.handleValidateToken(new Event(EventTypes.VALIDATE_TOKEN,new Object[]{service.getTokenByUser(user).tokens.get(0),corrId}));
    }

    @Then("Customer {string} he should have {int} unused tokens")
    public void check_unused_tokens(String user, int number) {
        assertEquals(number,service.getAllTokensByUser(user).size());
    }


}
