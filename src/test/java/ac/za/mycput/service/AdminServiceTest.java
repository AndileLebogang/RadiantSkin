package ac.za.mycput.service;

import ac.za.mycput.domain.Admin;
import ac.za.mycput.repository.AdminRepository;
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
class AdminServiceTest {

    @Mock
    private AdminRepository repo;

    @InjectMocks
    private AdminService service;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin.Builder()
                .setUserId(1L)
                .setEmployeeNumber("EMP001")
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john@example.com")
                .setPassword("12345")
                .build();
    }

    @Test
    void create() {
        when(repo.save(admin)).thenReturn(admin);

        Admin created = service.create(admin);

        assertNotNull(created);
        assertEquals(admin.getUserId(), created.getUserId());
        verify(repo, times(1)).save(admin);
    }

    @Test
    void read() {
        when(repo.findById(1L)).thenReturn(Optional.of(admin));

        Admin found = service.read(1L);

        assertNotNull(found);
        assertEquals(admin.getUserId(), found.getUserId());
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void update() {
        when(repo.save(admin)).thenReturn(admin);

        Admin updated = service.update(admin);

        assertNotNull(updated);
        assertEquals(admin.getEmail(), updated.getEmail());
        verify(repo, times(1)).save(admin);
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
        List<Admin> admins = Arrays.asList(admin);

        when(repo.findAll()).thenReturn(admins);

        List<Admin> result = service.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repo, times(1)).findAll();
    }

    @Test
    void findByEmail() {
        when(repo.findByEmail("john@example.com")).thenReturn(admin);

        Admin found = service.findByEmail("john@example.com");

        assertNotNull(found);
        assertEquals("john@example.com", found.getEmail());
        verify(repo, times(1)).findByEmail("john@example.com");
    }
}