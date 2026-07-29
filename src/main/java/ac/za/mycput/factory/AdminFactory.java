package ac.za.mycput.factory;

import ac.za.mycput.domain.Admin;
import ac.za.mycput.util.Helper;

public class AdminFactory {

    public static Admin createAdmin(
                                    String firstName,
                                    String lastName,
                                    String email,
                                    String password,
                                    String employeeNumber) {

        if (Helper.isNullEmpty(firstName) ||
                Helper.isNullEmpty(lastName) ||
                Helper.isNullEmpty(email) ||
                !Helper.isValidEmail(email) ||
                Helper.isNullEmpty(password) ||
                Helper.isNullEmpty(employeeNumber)) {
            return null;
        }

        return new Admin.Builder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setPassword(password)
                .setEmployeeNumber(employeeNumber)
                .build();
    }
}