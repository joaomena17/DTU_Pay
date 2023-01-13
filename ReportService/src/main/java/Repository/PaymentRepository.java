package Repository;

import ReportService.Payment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentRepository implements IPaymentRepository {
    List<Payment> Payments = new ArrayList<>();
    @Override
    public void addPayment(Payment p) {
        Payments.add(p);
    }

    @Override
    public List<Payment> GetAllPayments() {
        return Payments;
    }

    @Override
    public List<Payment> GetCustomerPayments(String cid) {
        return Payments.stream().filter(payment -> payment.cid.equals(cid)).collect(Collectors.toList());
    }

    @Override
    public List<Payment> GetMerchantPayments(String mid) {
        return Payments.stream().filter(payment -> payment.mid.equals(mid)).collect(Collectors.toList());
    }
}
