package org.acme.TokenService;
import messaging.implementations.RabbitMqQueue;
import org.acme.RequestSingleToken;
import org.acme.TokenRequest;

//Copied from studentregistration example, perhaps change
public class TokenFactory {
    static tokenService service = null;

    public synchronized tokenService getService() {
        // The singleton pattern.
        // Ensure that there is at most
        // one instance of a PaymentService

        if (service != null) {
            return service;
        }

        // Hookup the classes to send and receive
        // messages via RabbitMq, i.e. RabbitMqSender and
        // RabbitMqListener.
        // This should be done in the factory to avoid
        // the PaymentService knowing about them. This
        // is called dependency injection.
        // At the end, we can use the PaymentService in tests
        // without sending actual messages to RabbitMq.
        var mq = new RabbitMqQueue("rabbitMq");
        var repo = new interfaceTokenService() {
            @Override
            public boolean registerUser(String user) {
                return false;
            }

            @Override
            public String validateToken(String t) {
                return null;
            }

            @Override
            public String getSingleToken(String t) {
                return null;
            }

            @Override
            public String requestTokenMessageQueue(String user, int number) {
                return null;
            }
        };
        service = new tokenService(mq,repo);
//		new StudentRegistrationServiceAdapter(service, mq);
        return service;
    }
}
