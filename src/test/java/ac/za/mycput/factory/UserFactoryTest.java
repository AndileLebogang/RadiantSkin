package ac.za.mycput.factory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserFactoryTest {

    @Test
    void validUser() {

        assertTrue(UserFactory.isValidUser(
                "Lerato",
                "Mabena",
                "lerato.mabena@gmail.com",
                "Password123"
        ));
    }

    @Test
    void invalidFirstName() {

        assertFalse(UserFactory.isValidUser(
                "",
                "Zulu",
                "siyabonga.zulu@gmail.com",
                "Password123"
        ));
    }

    @Test
    void invalidLastName() {

        assertFalse(UserFactory.isValidUser(
                "Nompumelelo",
                "",
                "nompumelelo.nkosi@gmail.com",
                "Password123"
        ));
    }

    @Test
    void invalidEmail() {

        assertFalse(UserFactory.isValidUser(
                "Sipho",
                "Mthembu",
                "siphomthembugmail.com",
                "Password123"
        ));
    }

    @Test
    void invalidPassword() {

        assertFalse(UserFactory.isValidUser(
                "Zanele",
                "Nkosi",
                "zanele.nkosi@gmail.com",
                ""
        ));
    }
}