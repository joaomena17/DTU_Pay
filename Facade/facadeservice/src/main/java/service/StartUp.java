package service;

import messaging.implementations.RabbitMqQueue;

public class StartUp {
    public static void main(String[] args) {
        System.out.println("START FACADE SERvICE");
        var rabbitMq = new RabbitMqQueue("rabbitMq");
        new DTUPayService(rabbitMq);
    }
}
