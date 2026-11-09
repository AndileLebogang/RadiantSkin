package ac.za.mycput.service;

import ac.za.mycput.domain.Admin;
import ac.za.mycput.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final AdminRepository admins;
    private final AdminService adminService;

    public AdminSeeder(AdminRepository admins, AdminService adminService) {
        this.admins = admins;
        this.adminService = adminService;
    }

    @Override
    public void run(String... args) {
        if (admins.findByEmail("admin@radiantskin.co.za") == null) {
            adminService.create(new Admin.Builder()
                    .setEmployeeNumber("EMP001")
                    .setFirstName("Store")
                    .setLastName("Admin")
                    .setEmail("admin@radiantskin.co.za")
                    .setPassword("Admin@123")
                    .build());
        }
    }
}
