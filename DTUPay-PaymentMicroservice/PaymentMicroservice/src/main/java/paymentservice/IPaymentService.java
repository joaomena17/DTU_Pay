package paymentservice;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public interface IPaymentService {
    List<Payment> getPaymentList();

    Response getBalance(String id);

    Response makePayment(Payment payment);
}
