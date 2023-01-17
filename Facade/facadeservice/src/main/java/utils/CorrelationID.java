package utils;

import java.util.UUID;
import lombok.Value;

@Value
public class CorrelationID {
    private UUID id;

    public static CorrelationID randomID() {
        return new CorrelationID(UUID.randomUUID());
    }
}
