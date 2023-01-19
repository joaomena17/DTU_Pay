package paymentservice;

import messaging.implementations.RabbitMqQueue;

public class StartUp {
    public static void main(String[] args) {
        System.out.println("START PAYMENT SERvICE");
        var rabbitMq = new RabbitMqQueue("rabbitMq");
        new PaymentService(rabbitMq);
    }
}
