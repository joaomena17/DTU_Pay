package paymentservice;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@XmlRootElement // Needed for XML serialization and deserialization
//@Data // Automatic getter and setters and equals etc
@NoArgsConstructor // Needed for JSON deserialization and XML serialization and deserialization
@AllArgsConstructor
public class Payment {
    private String customerID, merchantID;
    private BigDecimal amount;

    public String getCustomerID(){ return this.customerID; }

    public void setCustomerID(String customerID){ this.customerID = customerID; }

    public String getMerchantID(){ return this.merchantID; }

    public void setMerchantID(String merchantID){ this.merchantID = merchantID; }

    public BigDecimal getAmount(){ return this.amount; }

    public void setAmount(BigDecimal amount){ this.amount = amount; }
}