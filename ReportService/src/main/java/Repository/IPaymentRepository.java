package Repository;

import ReportService.Payment;

import java.util.List;

public interface IPaymentRepository {

    void addPayment(Payment p);

    List<Payment> GetAllPayments();

    List<Payment> GetCustomerPayments(String cid);

    List<Payment> GetMerchantPayments(String mid);



}
