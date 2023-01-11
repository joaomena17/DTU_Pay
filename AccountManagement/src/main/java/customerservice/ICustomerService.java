package customerservice;

import customerservice.Customer;

import java.util.List;

public interface ICustomerService {

    String registerCustomer(Customer customer) throws IllegalArgumentException;

    boolean unregisterCustomer(Customer customer) throws IllegalArgumentException;

    Customer getCustomer(String customerID) throws IllegalArgumentException;

    public List<Customer> getCustomerList() throws IllegalArgumentException;
}
