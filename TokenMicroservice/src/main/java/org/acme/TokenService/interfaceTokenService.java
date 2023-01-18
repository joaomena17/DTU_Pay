package org.acme.TokenService;

import org.acme.RequestSingleToken;
import org.acme.TokenRequest;

public interface interfaceTokenService {
    boolean registerUser(String user);

    String validateToken(String t);

    String getSingleToken(RequestSingleToken t);

    String requestTokenMessageQueue(TokenRequest tokenRequest);
}
