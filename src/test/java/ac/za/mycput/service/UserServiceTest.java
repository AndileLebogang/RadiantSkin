package ac.za.mycput.service;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.User;
import ac.za.mycput.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo;

    @InjectMocks
    private UserService service;

    private User user;

    @BeforeEach
    void setUp() {
        user = new Customer.Builder()
                .setUserId(1L)
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john@example.com")
                .setPassword("12345")
                .setPhoneNumber("0812345678")
                .build();
    }

    @Test
    void create() {
        when(repo.save(user)).thenReturn(user);

        User created = service.create(user);

        assertNotNull(created);
        assertEquals(user.getUserId(), created.getUserId());
        verify(repo, times(1)).save(user);
    }

    @Test
    void read() {
        when(repo.findById(1L)).thenReturn(Optional.of(user));

        User found = service.read(1L);

        assertNotNull(found);
        assertEquals(user.getUserId(), found.getUserId());
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void update() {
        when(repo.save(user)).thenReturn(user);

        User updated = service.update(user);

        assertNotNull(updated);
        assertEquals(user.getEmail(), updated.getEmail());
        verify(repo, times(1)).save(user);
    }

    @Test
    void delete() {
        doNothing().when(repo).deleteById(1L);

        boolean deleted = service.delete(1L);

        assertTrue(deleted);
        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void getAll() {
        List<User> users = Arrays.asList(user);

        when(repo.findAll()).thenReturn(users);

        List<User> result = service.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repo, times(1)).findAll();
    }

    @Test
    void findByEmail() {
        when(repo.findByEmail("john@example.com")).thenReturn(user);

        User found = service.findByEmail("john@example.com");

        assertNotNull(found);
        assertEquals("john@example.com", found.getEmail());
        verify(repo, times(1)).findByEmail("john@example.com");
    }
}