package org.acme.TokenService;

public interface interfaceTokenService {
    boolean registerUser(String user);

    String validateToken(String t);

    String getSingleToken(String user);

    String requestTokenMessageQueue(String token, int number);
}
