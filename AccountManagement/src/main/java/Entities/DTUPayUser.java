package Entities;

import dtu.ws.fastmoney.AccountInfo;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;

import java.util.List;

public class DTUPayUser {

    private String _name;
    private String _bankID;
    private String _userRole;
    private String accountID;

    public DTUPayUser(String name, String bankID, String role){
        _name=name;
        _bankID=bankID;
        _userRole=role;
    }

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
        if(!this._userRole.equals("customer") && !this._userRole.equals("merchant")) return false;
        if(this._name.equals("")) return false;
        for (AccountInfo account : bankAccounts) {
            if (account.getAccountId().equals(_bankID)) {
                return true;
            }
        }
        return false;
    }
    public void set_name(String name){ this._name=name;}
    /*

    private List<Payment> customerPayments = new ArrayList<>();

    public void addCustomerPayment(Payment payment) { this.customerPayments.add(payment); }

    public List<Payment> getCustomerPayments() { return List.copyOf(this.customerPayments);}

    * */
}
