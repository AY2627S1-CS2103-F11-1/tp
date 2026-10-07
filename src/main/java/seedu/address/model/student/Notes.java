package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class Notes {

    public static final int MAX_LENGTH = 500;

    public static final String MESSAGE_CONSTRAINTS = "Student notes cannot be empty or exceed 500 characters.";

    private final String notes;

    public Notes(String notes) {
        requireNonNull(notes);
        checkArgument(isValidNotes(notes), MESSAGE_CONSTRAINTS);
        this.notes = notes;

    }

    public static boolean isValidNotes(String test) {
        return !test.isBlank() && test.length() <= MAX_LENGTH;
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
