package accountservice;

import java.util.List;

public class AccountService implements IAccountService {

    private AccountStorage AccountList = new AccountStorage();

    @Override
    public String registerAccount(DTUPayUser account) throws IllegalArgumentException {
        if(account.validAccount() && !AccountList.bankIDAlreadyExists(account.getBankID())){
            AccountList.addAccount(account);
            account.setAccountID(AccountList.getAccountCounter());
            return account.getAccountID();
        }
        else { throw new IllegalArgumentException("Account needs a valid bank account to register");}
    }

    @Override
    public boolean unregisterAccount(DTUPayUser account) throws IllegalArgumentException {
        if(AccountList.deleteAccount(account)) return true;
        else throw new IllegalArgumentException("Account was not deleted or does not exist");
    }

    @Override
    public DTUPayUser getAccount(String accountID, String role) throws IllegalArgumentException {
        DTUPayUser account = AccountList.searchAccountByID(accountID);
        if (account != null) return account;
        else throw new IllegalArgumentException("Account not found");
    }

    @Override
    public List<DTUPayUser> getAccountList(String role) throws IllegalArgumentException {
        if(role.equals("customer")) return AccountList.getCustomerStorage();
        else if (role.equals("merchant")) return AccountList.getMerchantStorage();
        else throw new IllegalArgumentException(String.format("Invalid role acc list - role: %s", role));
    }
}
