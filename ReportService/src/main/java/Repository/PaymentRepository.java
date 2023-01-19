package Repository;

import ReportService.PaymentReport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentRepository implements IPaymentRepository {
    List<PaymentReport> paymentReports = new ArrayList<>();
    @Override
    public void addPayment(PaymentReport p) {
        paymentReports.add(p);
    }

    @Override
    public List<PaymentReport> GetAllPayments() {
        return paymentReports;
    }

    @Override
    public List<PaymentReport> GetCustomerPayments(String cid) {
        return paymentReports.stream().filter(paymentReport -> paymentReport.cid.equals(cid)).collect(Collectors.toList());
    }

    @Override
    public List<PaymentReport> GetMerchantPayments(String mid) {
        return paymentReports.stream().filter(paymentReport -> paymentReport.mid.equals(mid)).collect(Collectors.toList());
    }
}
