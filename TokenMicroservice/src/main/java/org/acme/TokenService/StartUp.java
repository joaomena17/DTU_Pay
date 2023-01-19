package org.acme.TokenService;

import messaging.implementations.RabbitMqQueue;

public class StartUp {
    public static void main(String[] args) {
        System.out.println("START TOKEN SERvICE");
        var rabbitMq = new RabbitMqQueue("rabbitMq");
        var Tokenfactory = new TokenFactory();
        Tokenfactory.getService();
    }
}
