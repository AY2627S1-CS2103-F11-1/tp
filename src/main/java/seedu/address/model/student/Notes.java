package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.model.course.Deliverable;

/** Represents notes recorded about a student. */
public class Notes {

    public static final int MAX_LENGTH = 500;

    public static final String MESSAGE_CONSTRAINTS = "Student notes cannot be empty string, only whitespaces"
            + " or exceed 500 characters.";
    public static final String CORRUPTED_NOTES_ERROR_MESSAGE =
            "Student notes is corrupted and does not contain a task.";

    public final String notes;
    public final Deliverable deliverable;

    /** Creates Notes from a non-blank string of at most 500 characters. */
    public Notes(String notes, String deliverable) {
        requireAllNonNull(notes, deliverable);
        checkArgument(isValidNotes(notes), MESSAGE_CONSTRAINTS);
        this.notes = notes;
        this.deliverable = new Deliverable(deliverable);
    }

    /**
     * Creates Notes from the String representation.
     * Used when loading from JSON file of loading Notes.
     * @param notesAsString Notes as String representation
     */
    public Notes(String notesAsString) {
        requireNonNull(notesAsString);
        String[] parts = notesAsString.split(":\\s*", 2);

        this.deliverable = new Deliverable(parts[0]);
        this.notes = parts[1];
    }

    /**
     * Checks if Notes is valid.
     * Returns false if only contains whitespaces or exceeds 500 characters.
     * @param notes Notes to be validated.
     */
    public static boolean isValidNotes(String notes) {
        return (!notes.isBlank()) && notes.length() <= MAX_LENGTH;
    }

    /**
     * Checks if Notes is empty.
     */
    public boolean isEmpty() {
        return notes.isEmpty();
    }

    @Override
    public String toString() {
        return deliverable + ": " + notes;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Notes otherNotes)) {
            return false;
        }

        return notes.equals(otherNotes.notes)
                && deliverable.equals(otherNotes.deliverable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notes, deliverable);
    }
}
