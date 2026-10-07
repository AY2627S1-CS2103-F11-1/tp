package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class EmailTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Email(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidEmail = "";
        assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
    }

    @Test
    public void isValidEmail() {
        // null email
        assertThrows(NullPointerException.class, () -> Email.isValidEmail(null));

        // blank email
        assertFalse(Email.isValidEmail("")); // empty string
        assertFalse(Email.isValidEmail(" ")); // spaces only

        // missing parts or wrong domain
        assertFalse(Email.isValidEmail("@u.nus.edu")); // missing local part
        assertFalse(Email.isValidEmail("e1234567u.nus.edu")); // missing '@' symbol
        assertFalse(Email.isValidEmail("e1234567@")); // missing domain name
        assertFalse(Email.isValidEmail("e1234567@gmail.com")); // wrong domain (not @u.nus.edu)
        assertFalse(Email.isValidEmail("e1234567@nus.edu")); // missing 'u.' sub-domain
        assertFalse(Email.isValidEmail(" e1234567@u.nus.edu")); // leading space
        assertFalse(Email.isValidEmail("e1234567@u.nus.edu ")); // trailing space

        // valid NUS student emails
        assertTrue(Email.isValidEmail("e1234567@u.nus.edu")); // standard student ID email format
        assertTrue(Email.isValidEmail("e0000001@u.nus.edu")); // minimum digit format
        assertTrue(Email.isValidEmail("john_doe@u.nus.edu")); // name format with underscore
        assertTrue(Email.isValidEmail("student.1@u.nus.edu")); // period in local part
        assertTrue(Email.isValidEmail("a+test@u.nus.edu")); // plus sign in local part
    }

    @Test
    public void equals() {
        Email email = new Email("e1234567@u.nus.edu");

        // same values -> returns true
        assertTrue(email.equals(new Email("e1234567@u.nus.edu")));

        // same object -> returns true
        assertTrue(email.equals(email));

        // null -> returns false
        assertFalse(email.equals(null));

        // different types -> returns false
        assertFalse(email.equals(5.0f));

        // different values -> returns false
        assertFalse(email.equals(new Email("e7654321@u.nus.edu")));
    }
}
