package ReportService;

import Repository.PaymentRepository;
import messaging.implementations.RabbitMqQueue;

public class StartUp{
        public static void main(String[] args) {
            System.out.println("START REPORT SERvICE");
            var rabbitMq = new RabbitMqQueue("rabbitMq");
            var repo = new PaymentRepository();
            new reportService(rabbitMq,repo);
        }
}
