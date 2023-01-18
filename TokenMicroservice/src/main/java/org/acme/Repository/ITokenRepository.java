package org.acme.Repository;

import org.acme.RequestSingleToken;

import java.util.List;

public interface ITokenRepository {

    void getToken(RequestSingleToken T);

    List<PaymentReport> GetAllPayments();

    List<PaymentReport> GetCustomerPayments(String cid);

    List<PaymentReport> GetMerchantPayments(String mid);


*/
}
