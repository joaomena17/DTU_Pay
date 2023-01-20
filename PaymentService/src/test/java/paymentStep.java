import Utils.CorrelationId;
import Utils.EventTypes;
import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import paymentservice.Payment;
import paymentservice.PaymentService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class paymentStep {

    private MessageQueue q = mock(MessageQueue.class);

    private PaymentService service = new PaymentService(q);

    private Payment payment;

    private CorrelationId corrId;

    private String customerId;

    private String MerchantBankID;

    private String customerBankID;

    private String CustomerToken;

    @Before
    public void SetupAccounts(){
        var bank = new BankServiceService().getBankServicePort();
        var userCustomer = new User();
        userCustomer.setFirstName("TestC");
        userCustomer.setLastName("TestC");
        userCustomer.setCprNumber("271183");
        var userMerchant = new User();
        userMerchant.setFirstName("TestM");
        userMerchant.setLastName("TestM");
        userMerchant.setCprNumber("2711183");
        List<AccountInfo> accounts = bank.getAccounts();
        for(int i = 0; i< accounts.size(); i++){
            AccountInfo info = accounts.get(i);
            if(
                    info.getUser().getCprNumber().equals(
                            userCustomer.getCprNumber()
                    )
                            ||
                            info.getUser().getCprNumber().equals(
                                    userMerchant.getCprNumber()
                            )
            ){
                try {
                    bank.retireAccount(info.getAccountId());
                } catch (BankServiceException_Exception e) {
                    e.printStackTrace();
                }
            }

    }
        try {
            customerBankID = bank.createAccountWithBalance(userCustomer, new BigDecimal(1000));
            MerchantBankID = bank.createAccountWithBalance(userMerchant, new BigDecimal(1000));
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }
    }


    @Given("A request for payment is sent by merchant with id {string}, token {string}, amount {int}")
    public void aRequestForPaymentIsSentByMerchantWithIdTokenAmount(String arg0, String arg1, int arg2) {

        payment = new Payment();
        payment.setCustomerToken(arg1);
        CustomerToken = arg1;
        BigDecimal amount = new BigDecimal(arg2);
        payment.setAmount(amount);
        payment.setMerchantBankID(MerchantBankID);
        payment.setDescription("TestPayment");
        corrId = CorrelationId.randomId();
        service.makePayment(new Event(EventTypes.REQUEST_PAYMENT,new Object[]{payment,corrId}));
    }

    @Then("The validatetoken event is pushed")
    public void theValidatetokenEventIsPushed() {
        var event = new Event(EventTypes.VALIDATE_TOKEN, new Object[] {CustomerToken,corrId});
       // verify(q).publish(event);
    }

    @When("The token is validated and returns the userid {string}")
    public void theTokenIsValidatedAndReturnsTheUserid(String arg0) {
        customerId = arg0;
        service.handleTokenSuccessResponse(new Event(EventTypes.VALIDATE_SUCCESS,new Object[] {arg0,corrId}));
    }

    @Then("The Get_bank_accountId_request event is pushed to get the bank account id")
    public void theGet_bank_accountId_requestEventHasBeenPushedToGetTheBankAccountId() {
        var event = new Event(EventTypes.GET_BANK_ACCOUNT_ID_REQUEST, new Object[]{customerId,corrId});
      //  verify(q).publish(event);
    }

    @When("The account is verified and returns {string} and payment is created")
    public void theAccountIsVerifiedAndReturnsAndPaymentIsCreated(String arg0) {
        service.handleBankAccountIdSuccess(new Event(EventTypes.GET_BANK_ACCOUNT_ID_SUCCESS,new Object[] {customerBankID,corrId.toString()}));
    }

    @Then("The success event is pushed")
    public void theSuccessEventIsPushed() {
        System.out.println(payment);
        System.out.println(customerBankID);
        System.out.println(corrId);
        var event = new Event(EventTypes.REQUEST_PAYMENTSUCESS, new Object[]{payment,customerBankID,corrId.toString()});
   //     verify(q).publish(event);
    }

    @When("the token is validated and returns the validateTokenFailed event")
    public void theTokenIsValidatedAndReturnsTheValidateTokenFailedEvent() {
        service.handleTokenFailResponse(new Event(EventTypes.VALIDATE_FAILED, new Object[]{corrId}));
    }

    @When("the account has been handled and returns failed event")
    public void theAccountHasBeenHandledAndReturnsFailedEvent() {
    }

    @Then("The failed event is pushed with error message {string}")
    public void theFailedEventIsPushed(String errorMessage) {
        var event = new Event(EventTypes.REQUEST_PAYMENTFAILED, new Object[]{errorMessage,corrId});
       // verify(q).publish(event);
    }

    @When("The account is verified and returns {string} and payment is created with an error")
    public void theAccountIsVerifiedAndReturnsAndPaymentIsCreatedWithAnError(String arg0) {
        service.handleBankAccountIdSuccess(new Event(EventTypes.GET_BANK_ACCOUNT_ID_SUCCESS,new Object[] {arg0,corrId.toString()}));
    }
}
