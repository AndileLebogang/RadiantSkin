package ac.za.mycput.factory;

// Tebogo Makgato 230116086

import ac.za.mycput.domain.Customer;
import ac.za.mycput.util.Helper;

public class CustomerFactory {

    public static Customer createCustomer(Long userId, String firstName, String lastName,
                                          String email, String password, String phoneNumber) {

        if (!Helper.isValidId(userId) ||
                Helper.isNullEmpty(firstName) ||
                Helper.isNullEmpty(lastName) ||
                Helper.isNullEmpty(email) ||
                !Helper.isValidEmail(email) ||
                Helper.isNullEmpty(password) ||
                !Helper.isValidPhoneNumber(phoneNumber)) {
            return null;
        }

        return new Customer.Builder()
                .setUserId(userId)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setPassword(password)
                .setPhoneNumber(phoneNumber)
                .build();
    }

}