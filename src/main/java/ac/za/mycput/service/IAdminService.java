package ac.za.mycput.service;

import ac.za.mycput.domain.Admin;

public interface IAdminService extends IService<Admin, Long> {

    Admin login(String email, String password);
}