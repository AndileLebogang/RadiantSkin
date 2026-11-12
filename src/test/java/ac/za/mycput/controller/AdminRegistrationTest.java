package ac.za.mycput.controller;

import ac.za.mycput.domain.Admin;
import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Role;
import ac.za.mycput.repository.AdminRepository;
import ac.za.mycput.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.jpa.show-sql=false")
@AutoConfigureMockMvc
@Transactional
class AdminRegistrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired AdminRepository admins;
    @Autowired CustomerService customers;

    Map<String, Object> input(String email) {
        return Map.of("firstName", " Sam ", "lastName", "Mokoena", "email", email,
                "employeeNumber", "EMP-17", "password", "SchoolStaff123", "userId", 999999,
                "role", "CUSTOMER");
    }

    @Test
    void startupDoesNotCreateAFixedDefaultAdminAccount() {
        assertNull(admins.findByEmail("admin@radiantskin.co.za"));
    }

    @Test
    void publicRegistrationSavesHashedAdminAndCanLoginToThePortal() throws Exception {
        String email = "staff-" + UUID.randomUUID() + "@example.test";
        mvc.perform(post("/admin/register").contentType("application/json")
                        .content(json.writeValueAsString(input(email.toUpperCase()))))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.firstName").value("Sam"))
                .andExpect(jsonPath("$.password").doesNotExist());
        Admin saved = admins.findByEmail(email);
        assertNotNull(saved);
        assertNotEquals(999999L, saved.getUserId());
        assertEquals(Role.ADMIN, saved.getRole());
        assertNotEquals("SchoolStaff123", saved.getPassword());
        assertTrue(new BCryptPasswordEncoder().matches("SchoolStaff123", saved.getPassword()));
        assertFalse(saved.toString().contains(saved.getPassword()));

        var result = mvc.perform(post("/auth/login").contentType("application/json")
                        .content(json.writeValueAsString(Map.of("email", email.toUpperCase(), "password", "SchoolStaff123"))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.password").doesNotExist()).andReturn();
        var session = (MockHttpSession) result.getRequest().getSession(false);
        assertNotNull(session);
        mvc.perform(get("/auth/session").session(session)).andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ADMIN"));
        mvc.perform(get("/admin-portal/orders").session(session)).andExpect(status().isOk());
        mvc.perform(post("/auth/logout").session(session)).andExpect(status().isNoContent());
        mvc.perform(get("/admin-portal/orders")).andExpect(status().isUnauthorized());
    }

    @Test
    void invalidAndDuplicateRegistrationsDoNotCreateAccounts() throws Exception {
        long before = admins.count();
        mvc.perform(post("/admin/register").contentType("application/json").content("{}"))
                .andExpect(status().isBadRequest());
        var invalid = new java.util.HashMap<>(input("valid@example.test"));
        for (String field : java.util.List.of("employeeNumber", "firstName", "lastName")) {
            invalid.put(field, " ");
            mvc.perform(post("/admin/register").contentType("application/json")
                    .content(json.writeValueAsString(invalid))).andExpect(status().isBadRequest());
            invalid.putAll(input("valid@example.test"));
        }
        invalid.put("password", "short");
        mvc.perform(post("/admin/register").contentType("application/json")
                .content(json.writeValueAsString(invalid))).andExpect(status().isBadRequest());
        assertEquals(before, admins.count());

        String email = "duplicate-" + UUID.randomUUID() + "@example.test";
        String body = json.writeValueAsString(input(email));
        mvc.perform(post("/admin/register").contentType("application/json").content(body))
                .andExpect(status().isCreated());
        mvc.perform(post("/admin/register").contentType("application/json").content(body))
                .andExpect(status().isBadRequest());
        assertEquals(before + 1, admins.count());
    }

    @Test
    void customerEmailCannotBeUsedForAdminRegistration() throws Exception {
        Customer customer = new Customer();
        customer.setFirstName("Sam"); customer.setLastName("Mokoena");
        customer.setEmail("customer-" + UUID.randomUUID() + "@example.test");
        customer.setPassword("SchoolCustomer123"); customer.setPhoneNumber("0712345678");
        customers.register(customer);
        mvc.perform(post("/admin/register").contentType("application/json")
                        .content(json.writeValueAsString(input(customer.getEmail()))))
                .andExpect(status().isBadRequest());
        assertNull(admins.findByEmail(customer.getEmail()));
    }

    @Test
    void wrongPasswordIsRejectedAndLegacyPasswordUpgradesAfterSuccessfulLogin() throws Exception {
        String email = "legacy-" + UUID.randomUUID() + "@example.test";
        Admin admin = admins.saveAndFlush(new Admin.Builder().setFirstName("Sam").setLastName("Mokoena")
                .setEmployeeNumber("EMP-legacy").setEmail(email).setPassword("LegacyStaff123").build());
        admin.setRole(Role.ADMIN);
        mvc.perform(post("/auth/login").contentType("application/json")
                        .content(json.writeValueAsString(Map.of("email", email, "password", "wrong"))))
                .andExpect(status().isUnauthorized());
        assertEquals("LegacyStaff123", admin.getPassword());
        mvc.perform(post("/auth/login").contentType("application/json")
                        .content(json.writeValueAsString(Map.of("email", email, "password", "LegacyStaff123"))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.role").value("ADMIN"));
        assertTrue(new BCryptPasswordEncoder().matches("LegacyStaff123", admin.getPassword()));
    }
}
