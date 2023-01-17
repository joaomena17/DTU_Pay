package accountManagementService;

import Entities.DTUPayUser;
import Utils.CorrelationId;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import services.AccountManagementFactory;
import services.AccountManagementService;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

public class BankAccountRequestSteps {
    private BankService bank = new BankServiceService().getBankServicePort();

    private AccountManagementService customerService = new AccountManagementFactory().getService();

    private MessageQueue queue =  customerService.queue;
    private DTUPayUser customer;
    private String bankId,accountId;
    private User user = new User();
    private CorrelationId correlationId;
    private CorrelationId correlationId_unsuccess;

    @Given("a user called {string} is registered at DTU Pay")
    public void aUserCalledIsRegisteredAtDTUPay(String name) {

        user.setCprNumber("289-1234");
        user.setFirstName(name);
        user.setLastName(name);

        try {
            bankId = bank.createAccountWithBalance(user,  new BigDecimal(1000));

        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }
        // make full name from first and last name
        customer = new DTUPayUser(name, bankId, "customer");
        CorrelationId correlationId= CorrelationId.randomId();

        //event is received
        accountId=customerService.handleRegisterAccountRequest(new Event("RegisterAccountRequest", new Object[] { customer ,correlationId}));

        customerService.handleRegisterUserTokenSuccess(new Event("RegisterUserTokenSuccess", new Object[] { customer,customerService.tokenCorrelationId}));
    }

    @When("a successful {string} event is received asking for bank account")
    public void aSuccessfulEventIsReceivedAskingForBankAccount(String eventName) {
        correlationId=CorrelationId.randomId();
        customerService.handleBankAccountIdRequest(new Event(eventName, new Object[] { accountId,correlationId}));
    }

    @Then("a success {string} event is sent for the payment service")
    public void aSuccessEventIsSentForThePaymentService(String eventName) {
        var event = new Event(eventName, new Object[] {bankId,correlationId});
        verify(queue).publish(event);
    }

    /*Scenario failing*/

    @When("a unsuccessful {string} event is received asking for a not existing bank account")
    public void aUnsuccessfulEventIsReceivedAskingForANotExistingBankAccount(String eventName) {
        correlationId_unsuccess=CorrelationId.randomId();
        accountId="not exist";
        customerService.handleBankAccountIdRequest(new Event(eventName, new Object[] { accountId,correlationId_unsuccess}));
    }

    @Then("a success {string} event is sent for the payment service failing")
    public void aSuccessEventIsSentForThePaymentServiceFailing(String eventName) {
        var event = new Event(eventName, new Object[] {"Account not found",correlationId_unsuccess});
        verify(queue).publish(event);
    }
}
