package giovannid94;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import giovannid94.data.dao.CustomerDao;
import giovannid94.data.dao.ServiceDao;
import giovannid94.data.dao.SimpleProductDao;
import giovannid94.data.entity.Customer;
import giovannid94.data.entity.Service;

public class App {
    public static void main(String[] args) {
        ServiceDao serviceDao = new ServiceDao();
        List<Service> services = serviceDao.getAll();
        System.out.println("**** SERVICES ****");
        System.out.println("\n*** GET_ALL ***\n");
        services.forEach(System.out::println);
        Optional<Service> service = serviceDao.getOne(services.get(0).getServiceId());
        System.out.println("\n*** GET_ONE ***\n" + service.get());
        Service newService = new Service();
        newService.setName("FooBarBaz" + System.currentTimeMillis());
        newService.setPrice(new BigDecimal(4.35));
        serviceDao.create(newService);
        System.out.println("\n*** CREATE ***\n" + newService);
        newService.setPrice(new BigDecimal(13.45));
        newService = serviceDao.update(newService);
        System.out.println("\n*** UPDATE ***\n" + newService);
        serviceDao.delete(newService.getServiceId());
        System.out.println("\n*** DELETE ***\n" + newService);

        System.out.println("\n\n******* CUSTOMERS *******");
        CustomerDao customerDao = new CustomerDao();
        List<Customer> customers = customerDao.getAll();        
        System.out.println("*** GET ALL ***");
        customers.forEach(System.out::println);
        Optional<Customer> customer = customerDao.getOne(customers.get(0).getCustomerId());
        System.out.println("\n*** GET ONE ***\n" + customer.get());
        Customer newCustomer = new Customer();
        newCustomer.setFirstName("Ron");
        newCustomer.setLastName("Swanson");
        newCustomer.setEmail("ronswanson@example.com");
        newCustomer.setPhone("515.555.1235");
        newCustomer.setAddress("1234 Main St Anytown, KS 66400");
        newCustomer = customerDao.create(newCustomer);
        System.out.println("\n*** CREATE ***\n" + newCustomer);
        newCustomer.setEmail("rswanson@freedom.com");
        newCustomer = customerDao.update(newCustomer);
        System.out.println("\n*** UPDATE ***\n" + newCustomer);
        customerDao.delete(newCustomer.getCustomerId());
        System.out.println("\n*** DELETE ***\n");

        System.out.println("\n\n*** SIMPLE PRODUCT ***");
        SimpleProductDao spdao = new SimpleProductDao();
        UUID productId = spdao.createProduct("foobarbaz" + System.currentTimeMillis(), new BigDecimal(45.67), "Jaloo");
        System.out.println(productId);
    }
}
