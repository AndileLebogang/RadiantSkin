package ac.za.mycput.service;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements ICustomerService {

    private final CustomerRepository repo;

    @Autowired
    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    @Override
    public Customer create(Customer customer) {
        return this.repo.save(customer);
    }

    @Override
    public Customer read(Long id) {
        return this.repo.findById(id).orElse(null);
    }

    @Override
    public Customer update(Customer customer) {
        return this.repo.save(customer);
    }

    @Override
    public boolean delete(Long id) {
        this.repo.deleteById(id);
        return true;
    }

    @Override
    public List<Customer> getAll() {
        return this.repo.findAll();
    }

    @Override
    public Customer findByEmail(String email) {
        return this.repo.findByEmail(email);
    }

    @Override
    public List<Customer> findByPhoneNumber(String phoneNumber) {
        return this.repo.findByPhoneNumber(phoneNumber);
    }
}