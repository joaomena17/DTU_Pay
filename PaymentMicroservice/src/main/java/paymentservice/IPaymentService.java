package paymentservice;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public interface IPaymentService {
    public List<Payment> getPaymentList();

    public boolean addPayment(Payment payment);

    public Response getBalance(String id);

    public Response transferMoney(Payment payment);
}
