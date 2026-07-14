package ac.za.mycput.service;

import ac.za.mycput.domain.Customer;

import java.util.List;

public interface ICustomerService extends IService<Customer, Long>{

    List<Customer> getAll();
}
