package resources;

import customerservice.Customer;
import customerservice.CustomerService;
import customerservice.CustomersStorage;
import customerservice.ICustomersStorage;
import customerservice.ICustomerService;
import merchantservice.Merchant;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import java.util.List;

// JUNTAR CUSTOMER RESOURCES E MERCHANT RESOURCES NUM SO FICHEIRO ACCOUNTRESOURCES?

@Path("/customers")
public class CustomerResources {
    private ICustomerService customerService = new CustomerService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Customer> getCustomers(){ return customerService.getCustomerList(); }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomer(@PathParam("id") String id) {
        try{
            Customer customer=customerService.getCustomer(id);
            return Response.ok(customer).build();
        }
        catch(Exception e){
            System.err.println("Got error: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getCause()).build();
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerCustomer(Customer customer ) {
        try {
            String customerID= customerService.registerCustomer(customer);
            return Response.ok(customerID).build();
        }
        catch(Exception e){
            System.err.println("Got error: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getCause()).build();
        }
    }
    @POST
    @Path("/deregister")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteCustomer(Customer customer) {
        try {
            boolean success_delete = customerService.unregisterCustomer(customer);
            return Response.ok(success_delete).build();
        }
        catch(Exception e){
            System.err.println("Got error: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getCause()).build();
        }
    }
}
