import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import dtu.ws.fastmoney.*;
import Entities.*;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import MobileApp.*;
import org.junit.After;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/***
 * Author: Tiago Machado s222963
 */
public class DTUPaySteps {


    BankService bank = new BankServiceService().getBankServicePort();
    String customerBankID,customerId,merchantBankID,merchantId;
    DTUPayUser customerAccount,merchantAccount ;

    List<String> tokens;
    String paidToken;


    CustomerPort customerService= new CustomerPort();
    MerchantPort merchantService= new MerchantPort();

    ManagerPort managerService= new ManagerPort();

    List<PaymentReport> reportManager,reportCustomer,reportMerchant;
    Boolean paymentSuccess;
    private String error;


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////CREATE ACCOUNT ///////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Given("the customer {string} {string} with CPR {string} has a bank account with balance {int}")
    public void theCostumerWithCPRHasABankAccountWithBalance(String firstName, String lastName,String cpr, int balance) throws  BankServiceException_Exception {
        User customerUser = new User();
        customerUser.setFirstName(firstName);
        customerUser.setLastName(lastName);
        customerUser.setCprNumber(cpr);
        customerBankID = bank.createAccountWithBalance(customerUser,new BigDecimal( balance));
        customerAccount= new DTUPayUser(firstName+lastName,customerBankID,"customer");
    }

    @When("the customer registers at DTUPay")
    public void theCustomerRegistersAtDTUPay() throws Exception {
        customerId=customerService.registerCustomer(customerAccount);
    }

    @Then("the customer account is created")
    public void theCustomerAccountIsCreated() {
        assertFalse(customerId.isEmpty());
    }

    @Given("the merchant {string} {string} with CPR {string} has a bank account with balance {int}")
    public void theMerchantWithCPRHasABankAccountWithBalance(String firstName, String lastName, String cpr, int balance) throws  BankServiceException_Exception{
        User merchantUser = new User();
        merchantUser.setFirstName(firstName);
        merchantUser.setLastName(lastName);
        merchantUser.setCprNumber(cpr);
        merchantBankID = bank.createAccountWithBalance(merchantUser,new BigDecimal( balance));
        merchantAccount= new DTUPayUser(firstName+lastName,customerBankID,"merchant");
    }

    @When("the merchant registers at DTUPay")
    public void theMerchantRegistersAtDTUPay() throws Exception{
        merchantId=merchantService.registerMerchant(merchantAccount);
    }

    @Then("the merchant account is created")
    public void theMerchantAccountIsCreated() {
        assertFalse(merchantId.isEmpty());
    }
    @After
    public void afterStep() throws BankServiceException_Exception {
        if(merchantBankID!=null) bank.retireAccount(merchantBankID);
        if(customerBankID!=null) bank.retireAccount(customerBankID);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////TOKEN REQUEST ///////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @When("the customer asks for {int} tokens")
    public void theCustomerAsksForTokens(int numberOfTokens) throws Exception {
        try{
            tokens=customerService.requestTokensCustomer(customerId,numberOfTokens);
        }
        catch(Exception e){
            error=e.getMessage();
        }
    }

    @Then("the customer receives {int} tokens")
    public void theCustomerReceivesTokens(int numberOfTokens) {
        assertEquals(numberOfTokens,tokens.size());
    }

    @And("the merchant has received a token from the customer")
    public void theMerchantHasReceivedATokenFromTheCustomer() {
        paidToken=tokens.get(0);
        tokens.remove(0);
    }
    @Then("the customer receives an exception error with the message {string}")
    public void theCustomerReceivesAnExceptionErrorWithTheMessage(String errorMessage) {
        assertEquals(error,errorMessage);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////PAYMENT ///////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @When("the merchant initiates a payment for {int} kr by the customer")
    public void theMerchantInitiatesAPaymentForKrByTheCustomer(int amount) throws Exception {
        Payment payment= new Payment(merchantId,customerId,"1st payment",new BigDecimal(amount));
        paymentSuccess=merchantService.pay(payment);
    }

    @Then("the payment is successful")
    public void thePaymentIsSuccessful() {
        assertTrue(paymentSuccess);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////REPORT ///////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @When("the manager requests a DTU Pay report")
    public void theManagerRequestsADTUPayReport() throws Exception {
        reportManager=managerService.getDTUPayReport();
    }

    @Then("the manager gets a list with {int} transaction")
    public void theManagerGetsAListWithTransaction(int expectedSize) {
        assertEquals(expectedSize,reportManager.size());
    }

    @When("the customer requests a report")
    public void theCustomerRequestsAReport() throws Exception {
        reportCustomer=customerService.getCustomerReport(customerId);
    }

    @Then("the customer gets a list with {int} transaction")
    public void theCustomerGetsAListWithTransaction(int expectedSize) {
        assertEquals(expectedSize,reportCustomer.size());
    }

    @When("the merchant requests a report")
    public void theMerchantRequestsAReport() throws Exception {
        reportMerchant=merchantService.getMerchantReport(customerId);
    }

    @Then("the merchant gets a list with {int} transaction")
    public void theMerchantGetsAListWithTransaction(int expectedSize) {
        assertEquals(expectedSize,reportMerchant.size());
    }
}
