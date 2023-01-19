import io.cucumber.java.en.Given;
import dtu.ws.fastmoney.*;
import Entities.*;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import MobileApp.*;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class AccountSteps {


    BankService bank = new BankServiceService().getBankServicePort();
    String customerBankID,customerId= new String();
    DTUPayUser customerAccount ;


    CustomerPort customerService= new CustomerPort();
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
}
