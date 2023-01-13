package services;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@XmlRootElement // Needed for XML serialization and deserialization
//@Data // Automatic getter and setters and equals etc
@NoArgsConstructor // Needed for JSON deserialization and XML serialization and deserialization
@AllArgsConstructor
public class Payment {
    private String customerBankID, merchantBankID;
    private int amount;

    public String getCustomerBankID(){ return this.customerBankID; }

    public void setCustomerBankID(String customerBankID){ this.customerBankID = customerBankID; }

    public String getMerchantBankID(){ return this.merchantBankID; }

    public void setMerchantBankID(String merchantBankID){ this.merchantBankID = merchantBankID; }

    public int getAmount(){ return this.amount; }

    public void setAmount(int amount){ this.amount = amount; }
}