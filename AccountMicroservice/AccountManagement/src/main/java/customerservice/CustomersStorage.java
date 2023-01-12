package customerservice;

import merchantservice.Merchant;

import java.util.ArrayList;
import java.util.List;

public class CustomersStorage implements ICustomersStorage {

    private List<Customer> CustomerList = new ArrayList<>();

    private int CustomersCounter = 0;

    @Override
    public void addCustomer(Customer customer) {
        CustomerList.add(customer);
        CustomersCounter++;
    }

    @Override
    public boolean deleteCustomer(Customer customer) { return CustomerList.remove(customer); }

    @Override
    public List<Customer> getCustomerStorage() { return List.copyOf(CustomerList); }

    @Override
    public String getCustomersCounter() { return String.valueOf(CustomersCounter); }

    @Override
    public Customer searchCustomerByID(String customerID) {
        for (Customer customer : CustomerList){ // consider use of .stream() .filter() .collect()
            if (customer.getCustomerID() == customerID){ return customer; }
        }
        return null;
    }

    @Override
    public boolean bankIDAlreadyExists(String bankID) {
        for (Customer customer : CustomerList){ // consider use of .stream() .filter() .collect()
            if(customer.getBankID().equals(bankID)) return true;
        }
        return false;
    }
}
