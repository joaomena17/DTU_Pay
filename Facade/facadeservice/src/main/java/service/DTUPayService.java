package service;

import messaging.MessageQueue;

public class DTUPayService<T> {
    private final AccountService<T> account;
    private final PaymentService<T> payment;
    private final ReportService<T> report;
    private final TokenService<T> token;

    public DTUPayService(MessageQueue q){
        this.account = AccountService<T>(q);
        this.payment = PaymentService<T>(q);
        this.report = ReportService<T>(q);
        this.token = TokenService<T>(q);
    }

    public AccountService<T> getAccountService(){ return account; }

    public PaymentService<T> getPaymentService() {
        return payment;
    }

    public ReportService<T> getReportService() {
        return report;
    }

    public TokenService<T> getTokenService() {
        return token;
    }
}
