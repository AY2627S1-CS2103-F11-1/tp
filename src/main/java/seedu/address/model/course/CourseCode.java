package seedu.address.model.course;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Locale;

/**
 * Represents a course code in TAssist.
 */
public class CourseCode {
    public static final String MESSAGE_CONSTRAINTS = "Course code must not be blank.";

    public final String value;

    /**
     * Creates a {@code CourseCode}.
     */
    public CourseCode(String value) {
        requireNonNull(value);
        checkArgument(isValidCourseCode(value), MESSAGE_CONSTRAINTS);
        this.value = value.trim();
    }

    /**
     * Returns whether the course code is non-blank.
     */
    public static boolean isValidCourseCode(String test) {
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
        if (!(other instanceof CourseCode otherCourseCode)) {
            return false;
        }
        return value.equalsIgnoreCase(otherCourseCode.value);
    }

    @Override
    public int hashCode() {
        return value.toLowerCase(Locale.ROOT).hashCode();
    }
}
