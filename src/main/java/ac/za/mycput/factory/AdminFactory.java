package ac.za.mycput.factory;

import ac.za.mycput.domain.Admin;
import ac.za.mycput.util.Helper;

public class AdminFactory {

    public static Admin createAdmin(Long userId, String firstName, String lastName,
                                    String email, String password, String employeeNumber) {

        if (!UserFactory.isValidUser(firstName, lastName, email, password)
                || !Helper.isValidString(employeeNumber)) {
            return null;
        }

        return new Admin.Builder()
                .setUserId(userId)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setPassword(password)
                .setEmployeeNumber(employeeNumber)
                .build();
    }
}