package ac.za.mycput.util;

import java.math.BigDecimal;
import java.util.List;

import ac.za.mycput.domain.CartItem;

public class Helper {

    public static boolean isNullEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static boolean isValidId(Long id) {
        return id != null && id > 0;
    }

    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }

    public static boolean isValidTransactionReference(String transactionReference) {
        return !isNullEmpty(transactionReference);
    }

    public static boolean isValidEmail(String email) {
        return email != null &&
                email.contains("@") &&
                email.contains(".");
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^0\\d{9}$");

    }

    public static boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidPrice(BigDecimal price) {
        return price != null && price.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isValidStockQuantity(int stockQuantity) {
        return stockQuantity >= 0;
    }

    public static boolean isValidVolume(int volumeMl) {
        return volumeMl > 0;
    }

    public static boolean isValidCartItems(List<CartItem> cartItems) {
        return cartItems != null;

    }

}

