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
        if(!this._userRole.equals("customer") && !this._userRole.equals("merchant")) return false;
        if(this._name.equals("")) return false;
        return true;
    }
    public void set_name(String name){ this._name=name;}

    
}