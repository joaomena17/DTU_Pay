package Utils;

public final class EventTypes {
        public static final String GET_BANK_ACCOUNT_ID_REQUEST = "BankAccountIdRequest";
        public static final String GET_BANK_ACCOUNT_ID_SUCCESS = "BankAccountIdRequestCompleted";
        public static final String GET_BANK_ACCOUNT_ID_FAILED = "BankAccountIdRequestCompleted";

        public static final String VALIDATE_TOKEN = "ValidateTokenRequest";
        public static final String VALIDATE_FAILED = "ValidateTokenRequestFailed";
        public static final String VALIDATE_SUCCESS = "ValidateTokenRequestSuccess";

        public static final String REQUEST_PAYMENT = "RequestPayment";
        public static final String REQUEST_PAYMENTFAILED = "RequestPaymentFailed";
        public static final String REQUEST_PAYMENTSUCESS = "RequestPaymentSuccess";





    private EventTypes(){
            //this prevents even the native class from
            //calling this ctor as well :
            throw new AssertionError();
        }

    }
