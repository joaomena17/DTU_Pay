package org.acme.Utils;

public final class EventTypes {
        public static final String REGISTER_TOKEN_USER = "RegisterUserTokenRequest";
        public static final String REGISTER_TOKEN_USER_SUCCESS = "RegisterUserTokenRequestSuccess";
        public static final String REGISTER_TOKEN_USER_FAILED = "RegisterUserTokenRequestFailed";

        public static final String VALIDATE_TOKEN = "ValidateTokenRequest";
        public static final String VALIDATE_FAILED = "ValidateTokenRequestFailed";
        public static final String VALIDATE_SUCCESS = "ValidateTokenRequestSuccess";

        public static final String GET_TOKEN = "GetToken";
        public static final String GET_TOKEN_SUCCESS = "GetTokenSuccess";

        public static final String GET_TOKEN_FAILED = "GetTokenFailed";

        public static final String REQUEST_TOKEN = "RequestToken";
        public static final String REQUEST_TOKEN_SUCCESS = "RequestTokenSuccess";
        public static final String REQUEST_TOKEN_FAILED = "RequestTokenFailed";


        public static final String CUSTOMER_TOKENS_REQUEST = "CustomerTokensRequest";
        public static final String CUSTOMER_TOKENS_SUCCESS = "CustomerTokensSuccess";
        public static final String CUSTOMER_TOKENS_FAILED = "CustomerTokensFailed";











    private EventTypes(){
            //this prevents even the native class from
            //calling this ctor as well :
            throw new AssertionError();
        }

    }
