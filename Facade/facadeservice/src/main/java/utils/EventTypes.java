package utils;

public class EventTypes {

    // Account Management Events
    public static final String REGISTER_ACCOUNT_REQUEST = "RegisterAccountRequest";
    public static final String REGISTER_ACCOUNT_COMPLETED = "RegisterAccountRequestCompleted";
    public static final String REGISTER_ACCOUNT_FAILED = "RegisterAccountRequestFailed";

    public static final String UNREGISTER_ACCOUNT_REQUEST = "UnregisterAccountRequest";
    public static final String UNREGISTER_ACCOUNT_SUCCESS = "UnregisterAccountRequestSuccess";
    public static final String UNREGISTER_ACCOUNT_NOT_EXIST = "UnregisterAccountRequestNotExist";
    public static final String UNREGISTER_ACCOUNT_FAILED = "UnregisterAccountRequestFailed";

    // Payment Service Events
    public static final String PAYMENT_REQUEST = "MerchantPaymentRequest";
    public static final String PAYMENT_SUCCESS = "MerchantPaymentSuccess";
    public static final String PAYMENT_FAILED = "MerchantPaymentFailed";

    // Token Service Events
    public static final String CUSTOMER_TOKENS_REQUEST = "CustomerTokensRequest";
    public static final String CUSTOMER_TOKENS_SUCCESS = "CustomerTokensSuccess";
    public static final String CUSTOMER_TOKENS_FAIL = "CustomerTokensFailed";


    // Report Service Events
    public static final String MERCHANT_REPORT_RETURN = "MerchantReportReturnEvent";
    public static final String CUSTOMER_REPORT_RETURN = "CustomerReportReturnEvent";
    public static final String MANAGER_REPORT_RETURN = "ManagarReportReturnEvent";
    public static final String REQUEST_MERCHANT_REPORT = "generateMerchantReport";
    public static final String REQUEST_MANAGER_REPORT = "generateManagerReport";
    public static final String REQUEST_CUSTOMER_REPORT = "generateCustomerReport";
}
