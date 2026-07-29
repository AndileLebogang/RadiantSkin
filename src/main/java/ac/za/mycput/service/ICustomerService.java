package ac.za.mycput.service;

import ac.za.mycput.domain.Customer;

import java.util.List;

public interface ICustomerService extends IService<Customer, Long> {

    Customer findByEmail(String email);

    List<Customer> findByPhoneNumber(String phoneNumber);

}
