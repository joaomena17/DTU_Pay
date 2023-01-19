package Repository;

import ReportService.PaymentReport;

import java.util.List;

public interface IPaymentRepository {

    void addPayment(PaymentReport p);

    List<PaymentReport> GetAllPayments();

    List<PaymentReport> GetCustomerPayments(String cid);

    List<PaymentReport> GetMerchantPayments(String mid);



}
