package merchantservice;
import java.util.List;
public interface IMerchantsStorage {

    void addMerchant(Merchant merchant);

    boolean deleteMerchant(Merchant merchant);
    List<Merchant> getMerchantStorage();

    String getMerchantsCounter();

    Merchant searchMerchantByID(String merchantID);

    boolean bankIDAlreadyExists (String bankID);
}
