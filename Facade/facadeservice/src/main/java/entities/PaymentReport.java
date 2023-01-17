package entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@NoArgsConstructor // Needed for JSON deserialization and XML serialization and deserialization
@AllArgsConstructor
public class PaymentReport {
    public String cid;
    public String mid;
    public BigDecimal amount;

    public String customerToken;

}
