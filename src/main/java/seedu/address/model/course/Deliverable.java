package seedu.address.model.course;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Locale;

/**
 * Represents one project deliverable belonging to a course.
 */
public class Deliverable {
    public static final String MESSAGE_CONSTRAINTS = "Deliverable name must not be blank.";

    public final String name;

    /**
     * Creates a {@code Deliverable}.
     */
    public Deliverable(String name) {
        requireNonNull(name);
        checkArgument(isValidDeliverable(name), MESSAGE_CONSTRAINTS);
        this.name = name.trim();
    }

    /**
     * Returns whether the deliverable name is non-blank.
     */
    public static boolean isValidDeliverable(String test) {
        return !test.isBlank();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Deliverable otherDeliverable)) {
            return false;
        }
        return name.equalsIgnoreCase(otherDeliverable.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase(Locale.ROOT).hashCode();
    }
}
