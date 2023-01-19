
// Author: Tiago Machado s222963 //

import entities.DTUPayUser;
import io.cucumber.java.en.Given;
import dtu.ws.fastmoney.*;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import service.AccountService;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.HashMap;
import java.util.function.Consumer;

import messaging.Event;
import messaging.MessageQueue;
import utils.CorrelationID;
import utils.EventTypes;

import static org.junit.jupiter.api.Assertions.*;

public class AccountSteps {
    private Map<String, CompletableFuture<Event>> publishedEvents = new HashMap<>();
    private Map<DTUPayUser, CorrelationID> correlationIds = new HashMap<>();
    private CompletableFuture<String> accountCompletableFuture = new CompletableFuture<>();


    private MessageQueue queue = new MessageQueue() {

        @Override
        public void publish(Event event) {
            var account = event.getArgument(0, DTUPayUser.class);
            publishedEvents.get(account.getName()).complete(event);
        }

        @Override
        public void addHandler(String eventType, Consumer<Event> handler) {
        }
    };

    DTUPayUser userAccount,accountRegistered;

    AccountService accountService= new AccountService(queue);


    /////////////////////////// /////////////////////////// /////////////////////////// /////////////////////////// //////////////////////////////////////////////////////
    ///////////////////////////CREATE ACCOUNT////////////////////////////
    /////////////////////////// /////////////////////////// /////////////////////////// /////////////////////////// /////////////////////////// ///////////////////////////


    @Given("the {string} {string} {string} has a bank account")
    public void theHasABankAccount(String role, String firstName, String lastName)  throws BankServiceException_Exception{
        String userBankID="123";
        userAccount = new DTUPayUser(firstName + lastName, userBankID, role);
        publishedEvents.put(userAccount.getName(), new CompletableFuture<>());
    }
    @When("the user registers at DTUPay")
    public void theUserRegistersAtDTUPay() {
        new Thread(() -> {
            try {
                var UserId = accountService.requestAccountRegister(userAccount);
                accountCompletableFuture.complete(UserId); //wait
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Then("the event {string} is published")
    public void theEventIsPublished(String eventName) {
        Event event = publishedEvents.get(userAccount.getName()).join();

        assertEquals(eventName, event.getType());
        accountRegistered = event.getArgument(0, DTUPayUser.class);
        System.out.println(accountRegistered.getName());
        var correlationId = event.getArgument(1, CorrelationID.class);
        correlationIds.put(accountRegistered, correlationId);
    }


    @When("the account is received from account management")
    public void theAccountIsReceivedFromAccountManagement() {
        accountRegistered.setAccountID("1");
        accountService.handleAccountRegistrationCompleted(
                new Event(EventTypes.REGISTER_ACCOUNT_COMPLETED,
                        new Object[] {accountRegistered.getAccountID(),correlationIds.get(accountRegistered)}));
    }

    @When("the account is not received from account management")
    public void theAccountIsNotReceivedFromAccountManagement() {
        accountService.handleAccountRegistrationFailed(
                new Event(EventTypes.REGISTER_ACCOUNT_COMPLETED,
                        new Object[] {"",correlationIds.get(accountRegistered)}));
    }

    @Then("the account is created")
    public void theAccountIsCreated() {
        assertNotNull(accountCompletableFuture.join());
    }

    @Then("the account is not created")
    public void theAccountIsNotCreated() {
        assertEquals("",accountCompletableFuture.join());
    }

}
