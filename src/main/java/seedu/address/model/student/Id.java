package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/** Represents a student's unique identifier. */
public class Id {

    public static final String MESSAGE_CONSTRAINTS =
            "Student ID must start with an A, followed by 7 digits and an uppercase letter";

    public static final String VALIDATION_REGEX = "A\\d{7}[A-Z]";

    public final String id;

    /** Constructs an ID from a valid string. */
    public Id(String id) {
        requireNonNull(id);
        checkArgument(isValidId(id), MESSAGE_CONSTRAINTS);
        this.id = id;
    }

    public static boolean isValidId(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Id otherId)) {
            return false;
        }

        return id.equals(otherId.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
