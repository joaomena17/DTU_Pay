package merchantservice;

import java.util.ArrayList;
import java.util.List;

public class MerchantsStorage implements IMerchantsStorage{

    private List<Merchant> MerchantList= new ArrayList<>();
    private int MerchantsCounter=0;

    @Override
    public void addMerchant(Merchant merchant){
        MerchantList.add(merchant);
        MerchantsCounter++;
    }
    @Override
    public boolean deleteMerchant(Merchant merchant){
        return MerchantList.remove(merchant);

    }
    @Override
    public List<Merchant> getMerchantStorage(){
        return List.copyOf(MerchantList);
    }
    @Override
    public String getMerchantsCounter(){
        return String.valueOf(MerchantsCounter);
    }

    @Override
    public Merchant searchMerchantByID(String merchantID){
        for (Merchant merchant : MerchantList){ // consider use of .stream() .filter() .collect()
            if(merchant.getMerchantID().equals(merchantID)){
                return merchant;
            }
        }
        return null;
    }

    @Override
    public boolean bankIDAlreadyExists (String bankID){
        for (Merchant merchant : MerchantList){ // consider use of .stream() .filter() .collect()
            if(merchant.getBankID().equals(bankID)) return true;
        }
        return false;
    }
}
