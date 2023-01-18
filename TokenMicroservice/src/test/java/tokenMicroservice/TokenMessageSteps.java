package tokenMicroservice;

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
import org.acme.Repository.TokenRepository;
import org.acme.RequestSingleToken;
import org.acme.Token;
import org.acme.TokenRequest;
import org.acme.TokenService.tokenService;
import org.junit.Test;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

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

    private TokenRepository repo = new TokenRepository();
    private tokenService service = new tokenService(queue, repo);

    private Token
    public TokenMessageSteps() {}


    @Given("A customer {string} requests token while having {int} token")
    public void customer_requests_token_while_having_tokens(String string, Integer int1) {

    }

}
