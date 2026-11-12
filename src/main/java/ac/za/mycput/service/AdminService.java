package ac.za.mycput.service;

import ac.za.mycput.domain.Admin;
import ac.za.mycput.domain.Role;
import ac.za.mycput.repository.AdminRepository;
import ac.za.mycput.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Service
public class AdminService implements IAdminService {

    private final AdminRepository repo;
    private final UserRepository users;
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(12);

    @Autowired
    public AdminService(AdminRepository repo, UserRepository users) {
        this.repo = repo;
        this.users = users;
    }

    @Override
    public Admin create(Admin admin) {
        if (admin == null || blank(admin.getFirstName()) || blank(admin.getLastName())
                || blank(admin.getEmployeeNumber()) || blank(admin.getEmail())
                || admin.getPassword() == null || admin.getPassword().length() < 8
                || admin.getPassword().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException("Complete all fields and use a password of at least 8 characters (up to 72 bytes).");
        }
        admin.setFirstName(admin.getFirstName().trim());
        admin.setLastName(admin.getLastName().trim());
        admin.setEmail(admin.getEmail().trim().toLowerCase(Locale.ROOT));
        if (!admin.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
                || admin.getFirstName().length() > 100 || admin.getLastName().length() > 100
                || admin.getEmployeeNumber().length() > 100 || admin.getEmail().length() > 254) {
            throw new IllegalArgumentException("Check your name, employee number and email address.");
        }
        if (users.findByEmail(admin.getEmail()) != null) {
            throw new IllegalArgumentException("This email is already registered. Use a different email address.");
        }
        admin.setUserId(null);
        admin.setRole(Role.ADMIN);
        admin.setPassword(PASSWORD_ENCODER.encode(admin.getPassword()));
        return repo.save(admin);
    }

    @Override
    public Admin read(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Admin update(Admin admin) {
        if (admin.getUserId() == null) throw new IllegalArgumentException("Choose an existing admin account.");
        Admin existing = repo.findById(admin.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Admin account not found."));
        if (admin.getPassword() == null || admin.getPassword().isBlank()) {
            admin.setPassword(existing.getPassword());
        } else if (!isHash(admin.getPassword())) {
            if (admin.getPassword().length() < 8
                    || admin.getPassword().getBytes(StandardCharsets.UTF_8).length > 72) {
                throw new IllegalArgumentException("Use a password of at least 8 characters (up to 72 bytes).");
            }
            admin.setPassword(PASSWORD_ENCODER.encode(admin.getPassword()));
        }
        admin.setRole(Role.ADMIN);
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
        Admin admin = email == null ? null : this.repo.findByEmail(email.trim().toLowerCase(Locale.ROOT));

        if (admin == null || password == null || admin.getPassword() == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (isHash(admin.getPassword())) {
            if (!PASSWORD_ENCODER.matches(password, admin.getPassword())) {
                throw new IllegalArgumentException("Invalid email or password");
            }
        } else {
            if (!admin.getPassword().equals(password)) {
                throw new IllegalArgumentException("Invalid email or password");
            }
            admin.setPassword(PASSWORD_ENCODER.encode(password));
            repo.save(admin);
        }

        return admin;
    }

    private boolean blank(String value) {
        return value == null || value.isBlank();
    }

    private boolean isHash(String value) {
        return value.matches("^\\$2[aby]\\$\\d{2}\\$.*");
    }
}
