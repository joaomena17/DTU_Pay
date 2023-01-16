package paymentservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;

import dtu.ws.fastmoney.*;
import messaging.Event;
import messaging.MessageQueue;

public class PaymentService implements IPaymentService {

    BankService service = (new BankServiceService()).getBankServicePort();
    public Map<String, Payment > paymentRequest;

    private List<Payment> paymentList = new ArrayList<>();

    MessageQueue mq;

    public PaymentService(MessageQueue mq){
        this.mq = mq;
        mq.addHandler(EventTypes.REQUEST_PAYMENT, this::makePayment);
        mq.addHandler(EventTypes.VALIDATE_SUCCESS,this::handleTokenSuccessResponse);
        mq.addHandler(EventTypes.VALIDATE_FAILED,this::handleTokenSuccessResponse);
        mq.addHandler(EventTypes.GET_BANK_ACCOUNT_ID_SUCCESS,this::handleBankAccountIdSuccess);
        mq.addHandler(EventTypes.GET_BANK_ACCOUNT_ID_FAILED,this::handleTokenSuccessResponse);
    }
    @Override
    public void makePayment(Event ev) {
        Payment payment = ev.getArgument(0, Payment.class);
        paymentRequest.put("corrID",payment);
        //Validate customer token.
        mq.publish(new Event(EventTypes.VALIDATE_TOKEN,new Object[]{payment.getCustomerToken()}));
    }

    public void handleTokenSuccessResponse(Event event){
        var customerId = event.getArgument(0,String.class);
        mq.publish(new Event(EventTypes.GET_BANK_ACCOUNT_ID_REQUEST,new Object[]{customerId,"corrid"}));
    }

    public void handleBankAccountIdSuccess(Event event){
        var corId = event.getArgument(1,String.class);
        var customerBankId = event.getArgument(0,String.class);
        Payment p = paymentRequest.get(corId);
        String from = customerBankId;
        String to = p.getMerchantBankID();
        BigDecimal amount = p.getAmount();
        String description = p.getDescription();
        try {
            service.transferMoneyFromTo(from, to, amount, description);
            mq.publish(new Event(EventTypes.REQUEST_PAYMENTSUCESS,new Object[]{p,customerBankId}));

        }
        catch (BankServiceException_Exception e) {
            mq.publish(new Event(EventTypes.REQUEST_PAYMENTFAILED, new Object[]{e.getMessage(),corId}));
        }
    }
}
