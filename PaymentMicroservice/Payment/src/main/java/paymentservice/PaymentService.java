package paymentservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Utils.CorrelationId;
import Utils.EventTypes;
import dtu.ws.fastmoney.*;
import messaging.Event;
import messaging.MessageQueue;

public class PaymentService implements IPaymentService {

    BankService service = (new BankServiceService()).getBankServicePort();
    public Map<CorrelationId, Payment > paymentRequest;

    private List<Payment> paymentList = new ArrayList<>();

    MessageQueue mq;

    public PaymentService(MessageQueue mq){
        this.mq = mq;
        mq.addHandler(EventTypes.REQUEST_PAYMENT, this::makePayment);
        mq.addHandler(EventTypes.VALIDATE_SUCCESS,this::handleTokenSuccessResponse);
        mq.addHandler(EventTypes.VALIDATE_FAILED,this::handleTokenFailResponse);
        mq.addHandler(EventTypes.GET_BANK_ACCOUNT_ID_SUCCESS,this::handleBankAccountIdSuccess);
        mq.addHandler(EventTypes.GET_BANK_ACCOUNT_ID_FAILED,this::handleTokenSuccessResponse);
    }
    @Override
    public void makePayment(Event ev) {
        CorrelationId corrId = CorrelationId.randomId();
        Payment payment = ev.getArgument(0, Payment.class);
        paymentRequest.put(corrId,payment);
        //Validate customer token.
        mq.publish(new Event(EventTypes.VALIDATE_TOKEN,new Object[]{payment.getCustomerToken(),corrId}));
    }

    public void handleTokenSuccessResponse(Event event){
        var customerId = event.getArgument(0,String.class);
        var corrId = event.getArgument(1,CorrelationId.class);
        mq.publish(new Event(EventTypes.GET_BANK_ACCOUNT_ID_REQUEST,new Object[]{customerId,corrId}));
    }

    public void handleTokenFailResponse(Event event){
        var corrId = event.getArgument(0,CorrelationId.class);
        mq.publish(new Event(EventTypes.REQUEST_PAYMENTFAILED,new Object[]{"Invalid token on event" + corrId.toString()}));
    }

    public void handleBankAccountIdSuccess(Event event){
        var corId = event.getArgument(1,CorrelationId.class);
        var customerBankId = event.getArgument(0,String.class);
        Payment p = paymentRequest.get(corId);
        String from = customerBankId;
        String to = p.getMerchantBankID();
        BigDecimal amount = p.getAmount();
        String description = p.getDescription();
        try {
            service.transferMoneyFromTo(from, to, amount, description);
            mq.publish(new Event(EventTypes.REQUEST_PAYMENTSUCESS,new Object[]{p,customerBankId,corId}));

        }
        catch (BankServiceException_Exception e) {
            mq.publish(new Event(EventTypes.REQUEST_PAYMENTFAILED, new Object[]{e.getMessage(),corId}));
        }
    }
}
