package ac.za.mycput.service;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ac.za.mycput.domain.Role;

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
        customer.setRole(Role.CUSTOMER);
        return this.repo.save(customer);
    }

    @Override
    public Customer register(Customer customer) {
        if (this.repo.findByEmail(customer.getEmail()) != null) {
            throw new IllegalArgumentException("This email is already registered");
        }

        customer.setRole(Role.CUSTOMER);
        return this.repo.save(customer);
    }

    @Override
    public Customer login(String email, String password) {
        Customer customer = this.repo.findByEmail(email);

        if (customer == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (!customer.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return customer;
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