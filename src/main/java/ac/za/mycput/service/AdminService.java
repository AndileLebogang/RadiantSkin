package ac.za.mycput.service;

import ac.za.mycput.domain.Admin;
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
        return this.repo.save(admin);
    }

    @Override
    public Admin read(Long id) {
        return this.repo.findById(id).orElse(null);
    }

    @Override
    public Admin update(Admin admin) {
        return this.repo.save(admin);
    }

    @Override
    public boolean delete(Long id) {
        this.repo.deleteById(id);
        return true;
    }

    @Override
    public List<Admin> getAll() {
        return this.repo.findAll();
    }

    @Override
    public Admin findByEmail(String email) {
        return this.repo.findByEmail(email);
    }
}