package customerservice;

public class DTUPayUser {
    private String _name;
    private String _bankID;

    public DTUPayUser(String name, String bankID){
        _name=name;
        _bankID=bankID;
    }
    public String getName(customerservice.DTUPayUser user){
        return user._name;
    }
    public String geBankID(customerservice.DTUPayUser user){
        return user._bankID;
    }
    public boolean validAccount(){
        return (_bankID.length()>0 && _bankID!=null);
    }
}
