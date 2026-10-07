package seedu.address.model.course;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Locale;

/**
 * Represents the semester in which a course is taught.
 */
public class Semester {
    public static final String MESSAGE_CONSTRAINTS = "Semester must not be blank.";

    public final String value;

    /**
     * Creates a {@code Semester}.
     */
    public Semester(String value) {
        requireNonNull(value);
        checkArgument(isValidSemester(value), MESSAGE_CONSTRAINTS);
        this.value = value.trim();
    }

    /**
     * Returns whether the semester is non-blank.
     */
    public static boolean isValidSemester(String test) {
        return !test.isBlank();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Semester otherSemester)) {
            return false;
        }
        return value.equalsIgnoreCase(otherSemester.value);
    }

    @Override
    public int hashCode() {
        return value.toLowerCase(Locale.ROOT).hashCode();
    }
}
