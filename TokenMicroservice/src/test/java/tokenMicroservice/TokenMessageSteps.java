package tokenMicroservice;

import Utils.CorrelationId;
import dtu.ws.fastmoney.*;
import io.netty.util.concurrent.CompleteFuture;
import messaging.Event;
import messaging.MessageQueue;
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
import org.acme.Utils.EventTypes;
import org.junit.Test;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import org.acme.TokenService.interfaceTokenService;
import org.junit.Test;


/*
 Scenario: Customer requests token while having 2 token
    Given a customer "Jonas Doe" has an account on DTU pay with 2 token
    When the customer "Jonas Doe" request 1 token
    Then customer "Jonas Doe" does not receive more tokens


 */


public class TokenMessageSteps {

    private CompletableFuture<Event> publishedEvent = new CompletableFuture<>();

    private MessageQueue queue = mock(MessageQueue.class);

    private interfaceTokenService interfaceTokenService;
    private tokenService service = new tokenService(queue, interfaceTokenService);
    private TokenRequest tokenRequest = new TokenRequest();
    private Token token = new Token();
    private CreateUser createUser = new CreateUser();
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
            public String getSingleToken(RequestSingleToken t) {
                return null;
            }

            @Override
            public String requestTokenMessageQueue(TokenRequest tokenRequest) {
                return null;
            }
        };
    }


    @Given("A customer is created with the username {string}")
    public void A_customer_is_created_with_the_username(String user) {
        corrId = CorrelationId.randomId();
        service.handleRegisterUserTokenRequest(new Event(EventTypes.REGISTER_TOKEN_USER,new Object[]{user, corrId}));
    }

    @Then("a customer with name {string} then exists")
    public void a_customer_with_name_then_exists(String user) {
        assertEquals(service.doesUserExist(user), true);

    }


}
