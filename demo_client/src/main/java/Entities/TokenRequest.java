package Entities;

public class TokenRequest {
    private String accountId;
    private int tokenAmount;

    public TokenRequest(String customerId, int amount) {
        this.accountId=customerId;
        this.tokenAmount=amount;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public int getTokenAmount() {
        return this.tokenAmount;
    }
}