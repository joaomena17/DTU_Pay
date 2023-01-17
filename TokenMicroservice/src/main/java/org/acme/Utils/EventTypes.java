package org.acme.Utils;

public final class EventTypes {
        public static final String REGISTER_TOKEN_USER = "RegisterUserTokenRequest";
        public static final String REGISTER_TOKEN_USER_SUCCESS = "RegisterUserTokenRequestSuccess";
        public static final String REGISTER_TOKEN_USER_FAILED = "RegisterUserTokenRequestFailed";

        public static final String VALIDATE_TOKEN = "ValidateTokenRequest";
        public static final String VALIDATE_FAILED = "ValidateTokenRequestFailed";
        public static final String VALIDATE_SUCCESS = "ValidateTokenRequestSuccess";

        public static final String REQUEST_PAYMENT = "RequestPayment";
        public static final String REQUEST_PAYMENTFAILED = "RequestPaymentCompleted";
        public static final String REQUEST_PAYMENTSUCESS = "RequestPaymentFailed";





    private EventTypes(){
            //this prevents even the native class from
            //calling this ctor as well :
            throw new AssertionError();
        }

    }
