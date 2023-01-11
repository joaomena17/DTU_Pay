package customerservice;

import java.util.List;

public interface ICustomersStorage {

    void addCustomer(Customer customer);

    boolean deleteCustomer(Customer customer);

    List<Customer> getCustomerStorage();

    String getCustomersCounter();

    Customer searchCustomerByID(String customerID);

    boolean bankIDAlreadyExists (String bankID);


}
