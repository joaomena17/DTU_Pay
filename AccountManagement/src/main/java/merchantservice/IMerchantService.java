package merchantservice;
import java.util.List;
public interface IMerchantService {

    String registerMerchant(Merchant merchant) throws IllegalArgumentException;

    boolean unregisterMerchant(Merchant merchant) throws IllegalArgumentException;

    Merchant getMerchant(String merchantID) throws IllegalArgumentException;

    List<Merchant> getMerchantList() ;
}
