package org.acme.TokenService;

import java.util.List;

public interface interfaceTokenService {
    boolean registerUser(String user);

    String validateToken(String t);

    String getSingleToken(String user);

    List<String> requestTokenMessageQueue(String token, int number);
}
