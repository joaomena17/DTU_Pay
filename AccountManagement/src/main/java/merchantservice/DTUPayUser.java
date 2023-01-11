package merchantservice;

import dtu.ws.fastmoney.AccountInfo;
import dtu.ws.fastmoney.GetAccountsResponse;

import java.util.List;

public class DTUPayUser {
    private String _name;
    private String _bankID;

    public DTUPayUser(String name, String bankID){
        _name=name;
        _bankID=bankID;
    }
    public String getName(){
        return _name;
    }
    public String getBankID(){
        return _bankID;
    }
    public boolean validAccount(){
        List<AccountInfo> bankAccounts = new GetAccountsResponse().getReturn();
        for (AccountInfo account : bankAccounts) {
            if (account.getAccountId() == _bankID) {
                return true;
            }
        }
        return false;
    }
}
