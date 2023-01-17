package entities;

import dtu.ws.fastmoney.AccountInfo;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;

import java.util.List;

public class DTUPayUser {
    private String _name;
    private String _bankID;

    private String _userRole;


    public DTUPayUser(String name, String bankID, String role){
        _name=name;
        _bankID=bankID;
        _userRole=role;
    }

    private String accountID;

    public String getName(){
        return this._name;
    }

    public String getAccountID(){
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getBankID(){
        return this._bankID;
    }

    public String getRole() { return this._userRole; }
    public boolean validAccount(){
        BankService bank = new BankServiceService().getBankServicePort();
        List<AccountInfo> bankAccounts = bank.getAccounts();

        for (AccountInfo account : bankAccounts) {
            if (account.getAccountId().equals(_bankID)) {
                return true;
            }
        }

        return false;
    }

    /*

    private List<Payment> customerPayments = new ArrayList<>();

    public void addCustomerPayment(Payment payment) { this.customerPayments.add(payment); }

    public List<Payment> getCustomerPayments() { return List.copyOf(this.customerPayments);}

    * */
}