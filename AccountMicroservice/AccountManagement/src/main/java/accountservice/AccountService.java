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
        switch (role){
            case "customer":
                return AccountList.getCustomerStorage();
            case "merchant":
                return AccountList.getMerchantStorage();
            default:
                throw new IllegalArgumentException("Invalid role");
        }
    }
}
