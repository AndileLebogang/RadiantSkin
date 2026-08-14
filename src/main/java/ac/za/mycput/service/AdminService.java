package ac.za.mycput.service;

import ac.za.mycput.domain.Admin;
import ac.za.mycput.domain.Role;
import ac.za.mycput.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements IAdminService {

    private final AdminRepository repo;

    @Autowired
    public AdminService(AdminRepository repo) {
        this.repo = repo;
    }

    @Override
    public Admin create(Admin admin) {
        if (this.repo.findByEmail(admin.getEmail()) != null) {
            throw new IllegalArgumentException("This email is already registered as an admin");
        }

        admin.setRole(Role.ADMIN);
        return repo.save(admin);
    }

    @Override
    public Admin read(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Admin update(Admin admin) {
        return repo.save(admin);
    }

    @Override
    public boolean delete(Long id) {
        repo.deleteById(id);
        return true;
    }

    @Override
    public List<Admin> getAll() {
        return repo.findAll();
    }

    @Override
    public Admin login(String email, String password) {
        Admin admin = this.repo.findByEmail(email);

        if (admin == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (!admin.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return admin;
    }
}