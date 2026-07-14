package ac.za.mycput.service;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements ICustomerService{

    private CustomerRepository repo;
    @Autowired
    public CustomerService(CustomerRepository repo){
        this.repo = repo;
    }
    @Override
    public Customer create(Customer cust){
        return this.repo.save(cust);
    }
    @Override
    public Customer read(Long id) {
        return this.repo.findById(id).orElse(null);
    }
    @Override
    public Customer update(Customer cust){
        return this.repo.save(cust);
    }
    @Override
    public boolean delete(Long id) {
        this.repo.deleteById(id);
        return true;
    }
    @Override
    public List<Customer> getAll(){
        return this.repo.findAll();
    }
}
