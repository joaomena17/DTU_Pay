package entities;

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
        if(!this._userRole.equals("customer") && !this._userRole.equals("merchant")) return false;
        if(this._name.equals("")) return false;
        return true;
    }
    public void set_name(String name){ this._name=name;}


    

}