package paymentservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

import dtu.ws.fastmoney.*;

public class PaymentService implements IPaymentService {

    BankService service = (new BankServiceService()).getBankServicePort();

    private List<Payment> paymentList = new ArrayList<>();

    @Override
    public List<Payment> getPaymentList() {
        return List.copyOf(paymentList);
    }

    @Override
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
    @Override
    public Response makePayment(Payment payment) {
        String from = payment.getCustomerBankID();
        String to = payment.getMerchantBankID();
        BigDecimal amount = payment.getAmount();
        String description = String.format("Transfer of %d from %s to %s", amount, from, to);
        try {
            service.transferMoneyFromTo(from, to, amount, description);
            paymentList.add(payment);
            return Response.ok().build();
        } catch (BankServiceException_Exception e) {
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getCause()).build();
        }
    }
}
