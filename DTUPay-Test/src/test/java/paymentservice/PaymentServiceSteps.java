package paymentservice;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class PaymentServiceSteps {
    Payment pay = new Payment();
    PaymentService payService = new PaymentService();
    List<Payment> paymentList = new ArrayList<>();
    boolean successful;
    boolean paymentInList;
        User BankCostumer= new User();
        User BankMerchant = new User();
        CreateUser createCostumer= new CreateUser();
        CreateUser createMerchant = new CreateUser();
        Transfer transfer= new Transfer();
        String merchantId = new String();
        String costumerId = new String();
        @Given("a customer with a bank account with balance {int}")
        public void aCustomerWithABankAccountWithBalance(Integer int1) {
            // Write code here that turns the phrase above into concrete actions
            BankCostumer.setCprNumber("2821319");
            BankCostumer.setFirstName("s32138545");
            BankCostumer.setLastName("Ts312382428");
            createCostumer.user=BankCostumer;
            createCostumer.balance= new BigDecimal(int1);
        }

        @Given("that the customer is registered with DTU Pay")
        public void ThatTheCustomerIsRegisteredWithDTUPay() {
            // Write code here that turns the phrase above into concrete actions
            costumerId= payService.registerUser(createCostumer);
        }

        @Given("a merchant with a bank account with balance {int}")
        public void a_merchant_with_a_bank_account_with_balance(Integer int1) {
            // Write code here that turns the phrase above into concrete actions
            BankMerchant.setCprNumber("2821379");
            BankMerchant.setFirstName("123F7");
            BankMerchant.setLastName("S123et8");
            createMerchant.user=BankMerchant;
            createMerchant.balance= new BigDecimal(int1);
        }

        @Given("that the merchant is registered with DTU Pay")
        public void that_the_merchant_is_registered_with_dtu_pay() {
            // Write code here that turns the phrase above into concrete actions
            merchantId= payService.registerUser(createMerchant);

        }

        @When("the merchant initiates a payment for {int} kr by the customer")
        public void the_merchant_initiates_a_payment_for_kr_by_the_customer(Integer int1) {
            // Write code here that turns the phrase above into concrete actions
            transfer.amount=new BigDecimal(int1);
            transfer.from=costumerId;
            transfer.to=merchantId;
            transfer.description = "hi";
            System.out.println("Hej");
            payService.transfer(transfer);

            Payment pay2= new Payment();
            pay2.amount=int1;
            pay2.cid=costumerId;
            pay2.mid=merchantId;

            successful = payService.addPay(pay2);
        }

        @Then("the payment is successful")
        public void the_payment_is_successful() {
            assertTrue(successful);
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
        public void afterStep() {
            payService.deleteAccount(merchantId);
            payService.deleteAccount(costumerId);
        }




    }
