package ReportService;

import Repository.PaymentRepository;
import Utils.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;

import java.util.ArrayList;

//TODO: Make this look better refactor etc etc
public class reportService {
    PaymentRepository paymentRepo;
    MessageQueue queue;

    public reportService(MessageQueue mq,PaymentRepository p) {
        queue = mq;
        queue.addHandler("RequestPaymentSuccess", this::handlePaymentRegisterEvent);
        queue.addHandler("generateCustomerReport", this::handleCustomerReport);
        queue.addHandler("generateMerchantReport",this::handleMerchantReport);
        queue.addHandler("generateManagerReport", this::handleManagerReport);
        this.paymentRepo = p;
    }

    public void handlePaymentRegisterEvent(Event event){
        var payment = event.getArgument(0, Payment.class);
        var customerId = event.getArgument(1,String.class);
        PaymentReport pr = new PaymentReport();
        pr = pr.paymentToPaymentReport(payment,customerId);
        paymentRepo.addPayment(pr);
    }
    public void handleCustomerReport(Event event){
        var customerId = event.getArgument(0,String.class);
        var corrId = event.getArgument(1, CorrelationId.class);
        var list = paymentRepo.GetCustomerPayments(customerId);
        Event e = new Event("CustomerReportReturnEvent", new Object[] {list,corrId});
        queue.publish(e);
    }

    public void handleMerchantReport(Event event){
        var merchantId = event.getArgument(0,String.class);
        var corrId = event.getArgument(1, CorrelationId.class);
        var list = paymentRepo.GetMerchantPayments(merchantId);
        //Hack to hide customer id from merchant. Can't just alter the payment since its reference types
        /*var newlist = new ArrayList<PaymentReport>();
        for (PaymentReport p : list) {
            newlist.add(new PaymentReport("",p.mid,p.amount,p.customerToken));
        }*/
        Event e = new Event("MerchantReportReturnEvent", new Object[] {list,corrId});
        queue.publish(e);
    }
    public void handleManagerReport(Event event){
        var list = paymentRepo.GetAllPayments();
        var corrId = event.getArgument(0, CorrelationId.class);
        Event e = new Event("ManagerReportReturnEvent", new Object[] {list,corrId});
        queue.publish(e);
    }
}
