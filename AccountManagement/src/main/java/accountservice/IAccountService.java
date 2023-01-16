package accountservice;

import java.util.List;

public interface IAccountService {
    String registerAccount(DTUPayUser account) throws IllegalArgumentException;

    boolean unregisterAccount(DTUPayUser account) throws IllegalArgumentException;

    DTUPayUser getAccount(String accountID) throws IllegalArgumentException;

    List<DTUPayUser> getAccountList(String role) throws IllegalArgumentException;
}
