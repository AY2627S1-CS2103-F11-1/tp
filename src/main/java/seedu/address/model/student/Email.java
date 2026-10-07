package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Student's email in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidEmail(String)}
 */
public class Email {
    public static final String VALIDATION_REGEX =
            "^[A-Za-z0-9]+([+_.-][A-Za-z0-9]+)*@u\\.nus\\.edu$";

    public static final String MESSAGE_CONSTRAINTS =
            "Student email must follow the valid NUS email format (e.g. abc@u.nus.edu)";

    public final String email;

    /**
     * Constructs an {@code Email}.
     *
     * @param email A valid email address.
     */
    public Email(String email) {
        requireNonNull(email);
        checkArgument(isValidEmail(email), MESSAGE_CONSTRAINTS);
        this.email = email;
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidEmail(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return email;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Email otherEmail)) {
            return false;
        }

        return email.equals(otherEmail.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

}
