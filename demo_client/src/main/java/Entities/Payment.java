package Entities;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@XmlRootElement // Needed for XML serialization and deserialization
@Data // Automatic getter and setters and equals etc
@NoArgsConstructor // Needed for JSON deserialization and XML serialization and deserialization
@AllArgsConstructor
public class Payment {
    private String merchantBankID, customerToken,description;
    private BigDecimal amount;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerToken() {
        return customerToken;
    }

    public void setCustomerToken(String customerToken) {
        this.customerToken = customerToken;
    }
    public String getMerchantBankID(){ return this.merchantBankID; }

    public void setMerchantBankID(String merchantBankID){ this.merchantBankID = merchantBankID; }

    public BigDecimal getAmount(){ return this.amount; }

    public void setAmount(BigDecimal amount){ this.amount = amount; }
}