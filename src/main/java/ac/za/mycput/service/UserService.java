package ac.za.mycput.service;

import ac.za.mycput.domain.User;
import ac.za.mycput.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    private final UserRepository repo;

    @Autowired
    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public User create(User user) {
        return this.repo.save(user);
    }

    @Override
    public User read(Long id) {
        return this.repo.findById(id).orElse(null);
    }

    @Override
    public User update(User user) {
        return this.repo.save(user);
    }

    @Override
    public boolean delete(Long id) {
        this.repo.deleteById(id);
        return true;
    }

    @Override
    public List<User> getAll() {
        return this.repo.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return this.repo.findByEmail(email);
    }
}