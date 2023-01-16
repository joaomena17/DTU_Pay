package accountservice;

import java.util.List;

public interface IAccountStorage {

    void addAccount(DTUPayUser account);

    boolean deleteAccount(DTUPayUser account);

    List<DTUPayUser> getCustomerStorage();

    List<DTUPayUser> getMerchantStorage();

    String getAccountCounter();

    DTUPayUser searchAccountByID(String accountID);

    boolean bankIDAlreadyExists (String bankID);
}
