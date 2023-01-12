package services;

public class Merchant extends DTUPayUser {
    
    private String merchantID;

    public Merchant(String name, String bankID) {
        super(name, bankID);
    }

    //list payments
    public String getMerchantID(){
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }
}
