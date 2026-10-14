package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/** Represents notes recorded about a student. */
public class Notes {

    public static final int MAX_LENGTH = 500;

    public static final String MESSAGE_CONSTRAINTS = "Student notes cannot be only whitespaces"
            + " and exceed 500 characters.";

    public final String notes;

    /** Constructs notes from a non-blank string of at most 500 characters. */
    public Notes(String notes) {
        requireNonNull(notes);
        checkArgument(isValidNotes(notes), MESSAGE_CONSTRAINTS);
        this.notes = notes;

    }

    public Notes() {
        this.notes = "";
    }

    /**
     * Checks if Notes is valid.
     * Accepts empty String as notes. Returns false if only contains whitespaces or exceeds 500 characters.
     * @param test
     */
    public static boolean isValidNotes(String test) {
        return (test.isEmpty() || !test.isBlank()) && test.length() <= MAX_LENGTH;
    }

    /**
     * Checks if Notes is empty.
     */
    public boolean isEmpty() {
        return notes.isEmpty();
    }

    @Override
    public String toString() {
        return notes;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Notes otherNotes)) {
            return false;
        }

        return notes.equals(otherNotes.notes);
    }

    @Override
    public int hashCode() {
        return notes.hashCode();
    }
}
