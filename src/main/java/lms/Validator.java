package lms;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {


    public static boolean isValidStudentId(String id) {
        return id != null && id.matches("\\d{8}");
    }

    public static boolean isValidName(String name) {
        return name != null && name.matches("[a-zA-Z]+");
    }

    public static boolean isValidBookID(String id) {
        return id != null && id.matches("^[A-Z]{2}\\d{2}$");
    }

    public static boolean isValidBookTitle(String title) {
        return title != null && title.matches("[a-zA-Z]+");
    }

    public static boolean isValidISBN(String isbn) {

        if (isbn == null || !isbn.matches("\\d{13}")) {
            return false;
        }

        int sum = 0;

        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));

            if (i % 2 == 0) {
                sum += digit;
            } else {
                sum += digit * 3;
            }
        }

        int checkDigit = (10 - (sum % 10)) % 10;
        int actualDigit = Character.getNumericValue(isbn.charAt(12));

        return checkDigit == actualDigit;
    }


    public static boolean isValidDate(String dateStr) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(dateStr, formatter);
            return true;

        } catch (DateTimeParseException e) {
            return false;
        }
    }


    public static boolean isValidCopies(int copies) {
        return copies >= 0 && copies <= 2;
    }


    public static boolean isValidAvailability(int available, int copies) {
        return available >= 0 && available <= copies;
    }

    public static boolean isValidPrice(double price) {
        return price > 0;
    }
}
