package ac.za.mycput.factory;

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

}