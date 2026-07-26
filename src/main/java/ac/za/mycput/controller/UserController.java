package ac.za.mycput.controller;

import ac.za.mycput.domain.User;
import ac.za.mycput.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final IUserService service;

    @Autowired
    public UserController(IUserService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public User create(@RequestBody User user) {
        return service.create(user);
    }

    @GetMapping("/read/{userId}")
    public User read(@PathVariable Long userId) {
        return service.read(userId);
    }

    @PutMapping("/update")
    public User update(@RequestBody User user) {
        return service.update(user);
    }

    @DeleteMapping("/delete/{userId}")
    public boolean delete(@PathVariable Long userId) {
        return service.delete(userId);
    }

    @GetMapping("/getAll")
    public List<User> getAll() {
        return service.getAll();
    }

    @GetMapping("/findByEmail/{email}")
    public User findByEmail(@PathVariable String email) {
        return service.findByEmail(email);
    }
}