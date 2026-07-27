package ac.za.mycput.service;

import ac.za.mycput.domain.Admin;

import java.util.List;

public interface IAdminService extends IService<Admin, Long> {

    List<Admin> getAll();

    Admin findByEmail(String email);

}
