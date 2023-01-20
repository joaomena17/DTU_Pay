package accountManagementService;

import dtu.ws.fastmoney.*;
import groovy.xml.Entity;
import services.*;
import Entities.*;
import handlers.*;
import Utils.EventTypes;

// import Utils.CorrelationId;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import messaging.Event;
import messaging.MessageQueue;

import java.math.BigDecimal;

public class ManageAccountServiceSteps {

    private User user = new User();
    private String name = "Joao Silva";
    private String bankId;
    private String role = "customer";
    private BankService bank = new BankServiceService().getBankServicePort();
    private DTUPayUser customer;
    private String expectedId;
    private MessageQueue queue = mock(MessageQueue.class);
    private AccountManagementService customerService = new AccountManagementService(queue);

                            //////////////////////////////////////////////////////////////////////////////////
                            //////////////////////////////////////////////////////////////////////////////////
                            ///////////////////////////////////// REGISTER ACCOUNT///////////////////////////////////
                            //////////////////////////////////////////////////////////////////////////////////

    @Given("a customer {string} that is not registered with DTU Pay")
    public void aCustomerThatIsNotRegisteredWithDTUPay(String name) {
        bankId="111";
        customer= new  DTUPayUser(name,bankId,"customer");
    }

    @When("a register request event is received")
    public void aRegisterRequestEventIsReceived() {
        Event event = new Event(EventTypes.REGISTER_ACCOUNT_REQUEST, new Object[] { customer, customer.getBankID() });

        expectedId=customerService.handleRegisterAccountRequest(event);
        customer.setAccountID(expectedId);
    }
    @Then("a success register event is sent")
    public void aSuccessRegisterEventIsSent() {
        var event = new Event(EventTypes.REGISTER_ACCOUNT_COMPLETED, new Object[] {expectedId, customer.getBankID()});
        verify(queue).publish(event);
    }

    @When("an unregister event for the customer is received")
    public void anUnregisterEventForTheCustomerIsReceived() {
        Event event = new Event(EventTypes.UNREGISTER_ACCOUNT_REQUEST, new Object[] { customer, customer.getBankID() });
        customerService.handleUnregisterAccountRequest(event);
    }

    @And("an unregister success event is sent")
    public void anUnregisterSuccessEventIsSent() {
        var event = new Event(EventTypes.UNREGISTER_ACCOUNT_SUCCESS, new Object[] {true, customer.getBankID()});
        verify(customerService.queue).publish(event);
    }

    @Then("a failure register event is sent")
    public void aFailureRegisterEventIsSent() {
        var event = new Event(EventTypes.REGISTER_ACCOUNT_FAILED, new Object[] {"", customer.getBankID()});
        verify(queue).publish(event);
    }

    @Then("an unregister failure event is sent")
    public void anUnregisterFailureEventIsSent() {
        var event = new Event(EventTypes.UNREGISTER_ACCOUNT_FAILED, new Object[] {false, customer.getBankID()});
        verify(customerService.queue).publish(event);
    }

                        //////////////////////////////////////////////////////////////////////////////////
                        //////////////////////////////////////////////////////////////////////////////////
                        ///////////////////////////////////// REGISTER ACCOUNT////////////////////////////
                        //////////////////////////////////////////////////////////////////////////////////
    @When("a event is received asking for user bank account")
    public void aEventIsReceivedAskingForUserBankAccount() {
        customerService.handleBankAccountIdRequest(new Event(EventTypes.BANK_ACCOUNT_ID_REQUEST, new Object[] { customer.getAccountID(),customer.getAccountID()}));
    }

    @Then("a {string} event is sent for the payment service")
    public void aEventIsSentForThePaymentService(String eventName) {
        var event = new Event(eventName, new Object[] {customer.getBankID(),customer.getAccountID()});
        verify(queue).publish(event);
    }

}
