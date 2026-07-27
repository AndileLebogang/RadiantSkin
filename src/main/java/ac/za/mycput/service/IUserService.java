package ac.za.mycput.service;

import ac.za.mycput.domain.User;

import java.util.List;

public interface IUserService extends IService<User, Long> {

    List<User> getAll();

    User findByEmail(String email);

}
