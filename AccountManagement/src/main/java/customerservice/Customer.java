package customerservice;

public class Customer extends DTUPayUser{

    private String customerID;

    public Customer(String name, String bankID) {
        super(name, bankID);
    }

    public String getCustomerID(){
        return customerID;
    }

    public void setCustomerID(String merchantID) {
        this.customerID = merchantID;
    }

    /*

    private List<Payment> customerPayments = new ArrayList<>();

    public void addCustomerPayment(Payment payment) { this.customerPayments.add(payment); }

    public List<Payment> getCustomerPayments() { return List.copyOf(customerPayments);}

    * */

}
