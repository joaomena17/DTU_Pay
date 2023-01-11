package merchantservice;

import java.util.List;

public class MerchantService implements IMerchantService{

    private MerchantsStorage MerchantList = new MerchantsStorage();
    @Override
    public String registerMerchant(Merchant merchant) throws IllegalArgumentException{
        if (merchant.validAccount() && !MerchantList.bankIDAlreadyExists(merchant.getBankID())){
            MerchantList.addMerchant(merchant);
            merchant.setMerchantID(MerchantList.getMerchantsCounter());
            return merchant.getMerchantID();
        }
        else { throw new IllegalArgumentException("Merchant needs to have a valid bank account to register at DTU Pay");}
    }
    @Override
    public boolean unregisterMerchant(Merchant merchant) throws IllegalArgumentException{
        if(MerchantList.deleteMerchant(merchant)) return true;
        else throw new IllegalArgumentException("Merchant Account was not deleted or doesn't exist");
    }
    @Override
    public Merchant getMerchant(String merchantID) throws IllegalArgumentException{
        Merchant merchant=MerchantList.searchMerchantByID(merchantID);
        if(merchant!=null) return merchant;
        else throw new IllegalArgumentException("Merchant Account doesn't exist");
    }

    @Override
    public List<Merchant> getMerchantList(){
        return MerchantList.getMerchantStorage();
    }

}
