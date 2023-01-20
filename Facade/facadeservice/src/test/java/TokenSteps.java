import entities.DTUPayUser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import service.TokenService;
import utils.CorrelationID;
import utils.EventTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

<<<<<<< Updated upstream
import static org.junit.jupiter.api.Assertions.*;
=======
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
>>>>>>> Stashed changes

public class TokenSteps {
    private Map<String, CompletableFuture<Event>> publishedEvents = new HashMap<>();
    private Map<String, CorrelationID> correlationIds = new HashMap<>();
    private CompletableFuture<List<String>> tokensCompletableFuture = new CompletableFuture<>();
    private MessageQueue queue = new MessageQueue() {

        @Override
        public void publish(Event event) {
            var accountId = event.getArgument(0, String.class);
            publishedEvents.get(accountId).complete(event);
        }

        @Override
        public void addHandler(String eventType, Consumer<Event> handler) {
        }
    };
    TokenService tokenService= new TokenService(queue);
    String customerId,expectedCustomer;
    List<String> tokens;

    @Given("the customer {string} creates account")
    public void theCustomerCreatesAccount(String cid) {
        customerId=cid;
        publishedEvents.put(customerId,new CompletableFuture<>());
    }

    @When("the customer asks for {int} tokens")
    public void theCustomerAsksForTokens(int amountOfTokens) {
        new Thread(() -> {
            try {
                var tokens = tokenService.customerTokensRequest(customerId,amountOfTokens);
                tokensCompletableFuture.complete(tokens); //wait
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Then("the event {string} is published asking for tokens")
    public void theEventIsPublishedAskingForTokens(String eventName) {
        Event event = publishedEvents.get(customerId).join();
        assertEquals(eventName, event.getType());
        expectedCustomer = event.getArgument(0,String.class);
        var correlationId = event.getArgument(1, CorrelationID.class);
        correlationIds.put(expectedCustomer, correlationId);
    }

    @When("the tokens are received from the account management")
    public void theTokensAreReceivedFromTheAccountManagement() {
        List<String> tokensDelivered= new ArrayList<>();
        tokensDelivered.add("token 1");
        tokenService.handleRequestTokensSuccess(
                new Event(EventTypes.REQUEST_TOKEN_SUCCESS,
                        new Object[] {tokensDelivered,correlationIds.get(customerId)}));
    }

    @Then("the tokens are received")
    public void theTokensAreReceived() {
        tokens=tokensCompletableFuture.join();
        assertNotNull(tokens);
    }
<<<<<<< Updated upstream

    @When("the tokens are not received from the account management")
    public void theTokensAreNotReceivedFromTheAccountManagement() {
        List<String> tokensDelivered= new ArrayList<>();
        tokenService.handleRequestTokensSuccess(
                new Event(EventTypes.REQUEST_TOKEN_FAILED,
                        new Object[] {tokensDelivered,correlationIds.get(customerId)}));
    }

    @Then("the tokens are not received")
    public void theTokensAreNotReceived() {
        tokens=tokensCompletableFuture.join();
        assertTrue(tokens.size()==0);
    }
=======
>>>>>>> Stashed changes
}
