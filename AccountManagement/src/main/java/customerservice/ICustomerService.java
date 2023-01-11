package customerservice;

import customerservice.Customer;

public interface ICustomerService {

    String registerCustomer(Customer customer) throws IllegalArgumentException;

    boolean unregisterCustomer(Customer customer) throws IllegalArgumentException;

    Customer getCustomer(String customerID) throws IllegalArgumentException;
}
