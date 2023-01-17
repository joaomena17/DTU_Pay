package Repository;

import TokenService.*;

import java.util.List;

public interface ITokenRepository {

    void addPayment(PaymentReport p);

    List<PaymentReport> GetAllPayments();

    List<PaymentReport> GetCustomerPayments(String cid);

    List<PaymentReport> GetMerchantPayments(String mid);



}
