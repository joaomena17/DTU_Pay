package Repository;

import ReportService.Payment;

import java.util.List;

public class PaymentRepository implements IPaymentRepository {


    @Override
    public void addPayment() {
        
    }

    @Override
    public List<Payment> GetAllPayments() {
        return null;
    }

    @Override
    public List<Payment> GetCustomerPayments(String cid) {
        return null;
    }

    @Override
    public List<Payment> GetMerchantPayments(String mid) {
        return null;
    }
}
