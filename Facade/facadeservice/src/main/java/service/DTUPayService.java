package service;

import service.AccountService;
import service.PaymentService;
import service.TokenService;
import service.ReportService;
import messaging.MessageQueue;
import java.util.concurrent.CompletableFuture;

public class DTUPayService {

    private final AccountService account;
    //private final PaymentService payment;
    //private final ReportService report;
    //private final TokenService token;

    public DTUPayService(MessageQueue q){
        this.account = new AccountService(q);
        //this.payment = new PaymentService(q);
        //this.report = new ReportService(q);
        //this.token = new TokenService(q);
    }

    public AccountService getAccountService(){ return account; }

    /*
    public PaymentService<T> getPaymentService() {
        return payment;
    }

    public ReportService<T> getReportService() {
        return report;
    }

    public TokenService<T> getTokenService() {
        return token;
    } */
}
