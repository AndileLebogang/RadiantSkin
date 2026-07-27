package ac.za.mycput.factory;

import ac.za.mycput.domain.User;
import ac.za.mycput.util.Helper;

public class UserFactory {

    public static boolean isValidUser(String firstName,
                                      String lastName,
                                      String email,
                                      String password) {

        return Helper.isValidString(firstName)
                && Helper.isValidString(lastName)
                && Helper.isValidEmail(email)
                && Helper.isValidString(password);
    }

    public static User createUser(Long userId,
                                  String firstName,
                                  String lastName,
                                  String email,
                                  String password) {

        if (!isValidUser(firstName, lastName, email, password)) {
            return null;
        }

        return new User.Builder()
                .setUserId(userId)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setPassword(password)
                .build();
    }
}