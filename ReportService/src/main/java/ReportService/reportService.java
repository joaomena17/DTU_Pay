package ReportService;

import Repository.PaymentRepository;
import messaging.Event;
import messaging.MessageQueue;

//TODO: Make this look better refactor etc etc
public class reportService {
    PaymentRepository paymentRepo;
    MessageQueue queue;
    public reportService(MessageQueue mq,PaymentRepository p) {
        queue = mq;
        queue.addHandler("RequestPaymentCompleted", this::handlePaymentRegisterEvent);
        queue.addHandler("generateCustomerReport", this::handleCustomerReport);
        queue.addHandler("generateMerchantReport",this::handleMerchantReport);
        queue.addHandler("generateManagerReport", this::handleManagerReport);
        this.paymentRepo = p;
    }
    public void handlePaymentRegisterEvent(Event event){
        var payment = event.getArgument(0, Payment.class);
        var customerId = event.getArgument(1,String.class);
        PaymentReport pr = new PaymentReport();
        pr.paymentToPaymentReport(payment,customerId);
        paymentRepo.addPayment(pr);
    }
    public void handleCustomerReport(Event event){
        var customerId = event.getArgument(0,String.class);
        var list = paymentRepo.GetCustomerPayments(customerId);
        Event e = new Event("CustomerReportReturnEvent", new Object[] {list});
        queue.publish(e);
    }
    public void handleMerchantReport(Event event){
        var merchantId = event.getArgument(0,String.class);
        var list = paymentRepo.GetMerchantPayments(merchantId);
        Event e = new Event("MerchantReportReturnEvent", new Object[] {list});
        queue.publish(e);
    }
    public void handleManagerReport(Event event){
        var list = paymentRepo.GetAllPayments();
        Event e = new Event("ManagerReportReturnEvent", new Object[] {list});
        queue.publish(e);
    }
}
