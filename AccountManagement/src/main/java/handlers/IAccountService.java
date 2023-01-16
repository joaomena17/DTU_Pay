package handlers;

import java.util.List;
import Entities.DTUPayUser;

public interface IAccountService {
    String registerAccount(DTUPayUser account) throws IllegalArgumentException;

    boolean unregisterAccount(DTUPayUser account) throws IllegalArgumentException;

    DTUPayUser getAccount(String accountID) throws IllegalArgumentException;

    List<DTUPayUser> getAccountList(String role) throws IllegalArgumentException;
}
