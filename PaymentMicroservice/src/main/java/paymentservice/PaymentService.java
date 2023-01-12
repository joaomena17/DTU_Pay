package paymentservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

import dtu.ws.fastmoney.*;

public class PaymentService {

    BankService service = (new BankServiceService()).getBankServicePort();

    private List<Payment> paymentList = new ArrayList<>();
    private List<String> idList= new ArrayList<>();

    private Payment payment= new Payment("cid1","mid1",new BigDecimal(100));

    public PaymentService() {
        paymentList.add(payment);
    }
    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public boolean addPayment(Payment p) {
        return paymentList.add(p);
    }

    public Response getBalance(String id) {
        try {
            var user = service.getAccount(id);
            if (user == null) {
                return Response.status(Response.Status.PRECONDITION_FAILED).entity("User does not exist").build();
            }
            return Response.ok(user.getBalance()).build();
        } catch (BankServiceException_Exception e) {
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getCause()).build();
        }
    }
    public Response transferMoney(Transfer data) {
        try {
            System.out.println("TRANSFERING MONEY");
            service.transferMoneyFromTo(data.from, data.to, data.amount, data.description);
            System.out.println("TRANSFERING MONEY");
            return Response.ok().build();
        } catch (BankServiceException_Exception e) {
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getCause()).build();
        }
    }
}
