package Utils;

public final class EventTypes {

    public static final String GET_ACCOUNT_REQUEST = "GetAccountRequest";
    public static final String GET_ACCOUNT_COMPLETED = "GetAccountRequestCompleted";
    public static final String GET_ACCOUNT_FAILED = "GetAccountRequestFailed";

    public static final String REGISTER_ACCOUNT_REQUEST = "RegisterAccountRequest";
    public static final String REGISTER_ACCOUNT_COMPLETED = "RegisterAccountRequestCompleted";
    public static final String REGISTER_ACCOUNT_FAILED = "RegisterAccountRequestFailed";

    public static final String UNREGISTER_ACCOUNT_REQUEST = "UnregisterAccountRequest";
    public static final String UNREGISTER_ACCOUNT_SUCCESS = "UnregisterAccountRequestSuccess";
    public static final String UNREGISTER_ACCOUNT_FAILED = "UnregisterAccountRequestFailed";
    public static final String UNREGISTER_ACCOUNT_NOT_EXIST = "UnregisterAccountRequestNotExist";

    public static final String BANK_ACCOUNT_ID_REQUEST = "BankAccountIdRequest";
    public static final String BANK_ACCOUNT_ID_SUCCESS = "BankAccountIdRequestCompleted";
    public static final String BANK_ACCOUNT_ID_FAILED = "BankAccountIdRequestFailed";

    public static final String GET_LIST_ACCOUNTS_REQUEST = "GetListAccountsRequest";
    public static final String GET_LIST_ACCOUNTS_COMPLETED = "GetListAccountsRequestCompleted";
    public static final String GET_LIST_ACCOUNTS_FAILED = "GetListAccountsRequestFailed";

    public static final String REGISTER_USER_TOKEN_REQUEST = "RegisterUserTokenRequest";
    public static final String REGISTER_USER_TOKEN_SUCCESS = "RegisterUserTokenSuccess";
    public static final String REGISTER_USER_TOKEN_FAILED = "RegisterUserTokenFailed";

    private EventTypes(){
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }

}