package Entities;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement // Needed for XML serialization and deserialization
@Data // Automatic getter and setters and equals etc
@NoArgsConstructor // Needed for JSON deserialization and XML serialization and deserialization
@AllArgsConstructor
public class TokenRequest {
    private String accountId;
    private int tokenAmount;


    public String getAccountId() {
        return this.accountId;
    }

    public int getTokenAmount() {
        return this.tokenAmount;
    }
}