package customerservice;


import java.util.List;

public class CustomerService implements ICustomerService {

    private CustomersStorage CustomerList = new CustomersStorage();

    @Override
    public String registerCustomer(Customer customer) throws IllegalArgumentException {
        /* if(customer.validAccount() && !CustomerList.bankIDAlreadyExists(customer.getBankID())){ */
            CustomerList.addCustomer(customer);
            customer.setCustomerID(CustomerList.getCustomersCounter());
            return customer.getCustomerID();
        /* }
        else { throw new IllegalArgumentException("Customer needs a valid bank account to register");
    } */


    }

    @Override
    public boolean unregisterCustomer(Customer customer) throws IllegalArgumentException {
        if (CustomerList.deleteCustomer(customer)) return true;
        else throw new IllegalArgumentException("Customer Account was not deleted or does not exist");
    }

    @Override
    public Customer getCustomer(String customerID) throws IllegalArgumentException {
        Customer customer = CustomerList.searchCustomerByID(customerID);
        if(customer != null) return customer;
        else throw new IllegalArgumentException("Customer Account does not exist");
    }

    @Override
    public List<Customer> getCustomerList() throws IllegalArgumentException {
        return CustomerList.getCustomerStorage();
    }
}
