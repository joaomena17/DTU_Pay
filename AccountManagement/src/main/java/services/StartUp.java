package services;

import messaging.implementations.RabbitMqQueue;

public class StartUp {

    public static void main(String[] args) {
        System.out.println("START ACCOUNT SERvICE");
        var rabbitMq = new RabbitMqQueue("rabbitMq");
        new AccountManagementService(rabbitMq);
    }
}
