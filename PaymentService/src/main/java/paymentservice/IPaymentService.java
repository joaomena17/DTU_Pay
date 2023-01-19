package paymentservice;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import messaging.Event;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public interface IPaymentService {
    void makePayment(Event ev);
}
