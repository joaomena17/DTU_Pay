package resources;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import services.*;
import dtu.ws.fastmoney.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class PaymentServiceSteps {

    PaymentResource payService = new PaymentResource();

    BankService bank = new BankServiceService().getBankServicePort();
    User BankCostumer= new User();
    User BankMerchant = new User();
    Transfer transfer= new Transfer();
    String merchantId = new String();
    String costumerId = new String();
        @Given("a customer with a bank account with balance {int}")
        public void aCustomerWithABankAccountWithBalance(Integer int1) throws BankServiceException_Exception {
            // Write code here that turns the phrase above into concrete actions
            BankCostumer.setCprNumber("1004017591");
            BankCostumer.setFirstName("Tiago");
            BankCostumer.setLastName("Silverio");
            costumerId=bank.createAccountWithBalance(BankCostumer,new BigDecimal(int1));
        }
        @Given("a merchant with a bank account with balance {int}")
        public void a_merchant_with_a_bank_account_with_balance(Integer int1) throws BankServiceException_Exception {
            // Write code here that turns the phrase above into concrete actions
            BankMerchant.setCprNumber("1004017591");
            BankMerchant.setFirstName("Tiago");
            BankMerchant.setLastName("Silverio");
            merchantId=bank.createAccountWithBalance(BankMerchant,new BigDecimal(int1));
        }


        @When("the merchant initiates a payment for {int} kr by the customer")
        public void the_merchant_initiates_a_payment_for_kr_by_the_customer(Integer int1) {
            // Write code here that turns the phrase above into concrete actions
            Payment payment= new Payment();
            payment.setCustomerBankID(costumerId);
            payment.setMerchantBankID(merchantId);
            payment.setAmount(new BigDecimal(int1));
            payService.transfer(payment);
        }

        @Then("the balance of the customer at the bank is {int} kr")
        public void the_balance_of_the_customer_at_the_bank_is_kr(Integer int1) {
            // Write code here that turns the phrase above into concrete actions
            System.out.println(costumerId);
            assertEquals(payService.getBalance(costumerId), new BigDecimal(900));
        }

        @Then("the balance of the merchant at the bank is {int} kr")
        public void the_balance_of_the_merchant_at_the_bank_is_kr(Integer int1) {
            // Write code here that turns the phrase above into concrete actions
            assertEquals(payService.getBalance(merchantId), new BigDecimal(2100));
        }
        @After
        public void afterStep() throws BankServiceException_Exception {
           bank.retireAccount(merchantId);
           bank.retireAccount(costumerId);
        }


    }
