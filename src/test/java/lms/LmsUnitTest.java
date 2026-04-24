package lms;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LmsUnitTest {

    @Test
    void testValidStudentId() {
        assertTrue(Validator.isValidStudentId("12345678"));
    }

    @Test
    void testInvalidStudentId_Short() {
        assertFalse(Validator.isValidStudentId("1234567"));
    }

    @Test
    void testInvalidStudentId_Letters() {
        assertFalse(Validator.isValidStudentId("1234567A"));
    }

    @Test
    void testInvalidStudentId_Null() {
        assertFalse(Validator.isValidStudentId(null));
    }

    @Test
    void testValidName() {
        assertTrue(Validator.isValidName("Binath"));
    }

    @Test
    void testInvalidName_WithNumbers() {
        assertFalse(Validator.isValidName("Binath1"));
    }

    @Test
    void testInvalidName_WithSpace() {
        assertFalse(Validator.isValidName("Binath B"));
    }

    @Test
    void testInvalidName_Null() {
        assertFalse(Validator.isValidName(null));
    }

    @Test
    void testValidBookId() {
        assertTrue(Validator.isValidBookID("AB12"));
    }

    @Test
    void testInvalidBookId_LowercaseLetters() {
        assertFalse(Validator.isValidBookID("ab12"));
    }

    @Test
    void testInvalidBookId_TooLong() {
        assertFalse(Validator.isValidBookID("AB123"));
    }

    @Test
    void testInvalidBookId_Null() {
        assertFalse(Validator.isValidBookID(null));
    }

    @Test
    void testValidBookTitle() {
        assertTrue(Validator.isValidBookTitle("Clean Code"));
    }

    @Test
    void testInvalidBookTitle_StartsWithNumber() {
        assertFalse(Validator.isValidBookTitle("1984"));
    }

    @Test
    void testInvalidBookTitle_Blank() {
        assertFalse(Validator.isValidBookTitle("   "));
    }

    @Test
    void testInvalidBookTitle_Null() {
        assertFalse(Validator.isValidBookTitle(null));
    }

    @Test
    void testValidISBN() {
        assertTrue(Validator.isValidISBN("9780306406157"));
    }

    @Test
    void testInvalidISBN_WrongCheckDigit() {
        assertFalse(Validator.isValidISBN("9780306406158"));
    }

    @Test
    void testInvalidISBN_TooShort() {
        assertFalse(Validator.isValidISBN("978030640615"));
    }

    @Test
    void testInvalidISBN_Letters() {
        assertFalse(Validator.isValidISBN("978030640615X"));
    }

    @Test
    void testInvalidISBN_Null() {
        assertFalse(Validator.isValidISBN(null));
    }

    @Test
    void testValidDate() {
        assertTrue(Validator.isValidDate("15/06/2024"));
    }

    @Test
    void testInvalidDate_WrongFormat() {
        assertFalse(Validator.isValidDate("2024-06-15"));
    }

    @Test
    void testInvalidDate_InvalidDay() {
        assertFalse(Validator.isValidDate("32/06/2024"));
    }

    @Test
    void testInvalidDate_Null() {
        assertFalse(Validator.isValidDate(null));
    }

    @Test
    void testValidCopies_Zero() {
        assertTrue(Validator.isValidCopies(0));
    }

    @Test
    void testValidCopies_Max() {
        assertTrue(Validator.isValidCopies(2));
    }

    @Test
    void testInvalidCopies_ExceedsMax() {
        assertFalse(Validator.isValidCopies(3));
    }

    @Test
    void testInvalidCopies_Negative() {
        assertFalse(Validator.isValidCopies(-1));
    }

    @Test
    void testValidAvailability() {
        assertTrue(Validator.isValidAvailability(1, 2));
    }

    @Test
    void testValidAvailability_EqualsCopies() {
        assertTrue(Validator.isValidAvailability(2, 2));
    }

    @Test
    void testInvalidAvailability_ExceedsCopies() {
        assertFalse(Validator.isValidAvailability(3, 2));
    }

    @Test
    void testInvalidAvailability_Negative() {
        assertFalse(Validator.isValidAvailability(-1, 2));
    }

    @Test
    void testValidPrice() {
        assertTrue(Validator.isValidPrice(9.99));
    }

    @Test
    void testInvalidPrice_Zero() {
        assertFalse(Validator.isValidPrice(0.0));
    }

    @Test
    void testInvalidPrice_Negative() {
        assertFalse(Validator.isValidPrice(-5.0));
    }
}
