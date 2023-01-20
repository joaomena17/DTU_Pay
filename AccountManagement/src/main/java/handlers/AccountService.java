package handlers;

import java.util.ArrayList;
import java.util.List;
import Entities.DTUPayUser;

public class AccountService implements IAccountService {

    private AccountStorage AccountList = new AccountStorage();

    @Override
    public String registerAccount(DTUPayUser account) throws IllegalArgumentException {
        System.out.println("REGISTERACCOUNT ACCOUNT MANAGEMENT");
        if(account.validAccount() && !AccountList.bankIDAlreadyExists(account.getBankID())){
            AccountList.addAccount(account);
            account.setAccountID(AccountList.getAccountCounter());
            return account.getAccountID();
        }
        else return "";
    }

    @Override
    public boolean unregisterAccount(DTUPayUser account) throws IllegalArgumentException {
        try{
            DTUPayUser accountToDelete = AccountList.searchAccountByID(account.getAccountID());
            return AccountList.deleteAccount(accountToDelete);
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public DTUPayUser getAccount(String accountID) throws IllegalArgumentException {
        return AccountList.searchAccountByID(accountID);
    }

    @Override
    public List<DTUPayUser> getAccountList(String role) throws IllegalArgumentException {
        if(role.equals("customer")) return AccountList.getCustomerStorage();
        else if (role.equals("merchant")) return AccountList.getMerchantStorage();
        else if (role.equals("all")) {
            List<DTUPayUser> MergedList = new ArrayList<>();
            MergedList.addAll(AccountList.getCustomerStorage());
            MergedList.addAll(AccountList.getMerchantStorage());
            return MergedList;
        }
        else throw new IllegalArgumentException(String.format("Invalid role acc list - role: %s", role));
    }
}
