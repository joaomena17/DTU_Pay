package services;

import dtu.ws.fastmoney.AccountInfo;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;

import java.util.List;

public class DTUPayUser {
    private String _name;
    private String _bankID;


    public DTUPayUser(String name, String bankID){
        _name=name;
        _bankID=bankID;
    }
    public String getName(){
        return this._name;
    }
    public String getBankID(){
        return this._bankID;
    }
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
}
