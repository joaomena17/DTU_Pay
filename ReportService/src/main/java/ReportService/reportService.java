package ReportService;

import Repository.PaymentRepository;
import messaging.Event;
import messaging.MessageQueue;

//TODO: Make this look better refactor etc etc
public class reportService {
    PaymentRepository p;
    MessageQueue queue;
    public reportService(MessageQueue mq,PaymentRepository p) {
        queue = mq;
        queue.addHandler("RequestPaymentCompleted", this::handlePaymentRegisterEvent);
        queue.addHandler("generateCustomerReport", this::handleCustomerReport);
        queue.addHandler("generateMerchantReport",this::handleMerchantReport);
        queue.addHandler("generateManagerReport", this::handleManagerReport);
        this.p = p;
    }
    public void handlePaymentRegisterEvent(Event event){
        var payment = event.getArgument(0, PaymentReport.class);
        p.addPayment(payment);
    }
    public void handleCustomerReport(Event event){
        var customerId = event.getArgument(0,String.class);
        var list = p.GetCustomerPayments(customerId);
        Event e = new Event("CustomerReportReturnEvent", new Object[] {list});
        queue.publish(e);
    }
    public void handleMerchantReport(Event event){
        var merchantId = event.getArgument(0,String.class);
        var list = p.GetMerchantPayments(merchantId);
        Event e = new Event("MerchantReportReturnEvent", new Object[] {list});
        queue.publish(e);
    }
    public void handleManagerReport(Event event){
        var list = p.GetAllPayments();
        Event e = new Event("ManagerReportReturnEvent", new Object[] {list});
        queue.publish(e);
    }
}
