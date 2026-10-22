package seedu.address.model.student;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** Stores a Student's Notes in a List */
public class NotesList {
    private final List<Notes> notesList;

    /**
     * Creates an empty NotesList.
     */
    public NotesList() {
        this.notesList = new ArrayList<>();
    }

    /**
     * Creates a NotesList given a Notes.
     * @param notes Notes to be added into NotesList.
     */
    public NotesList(Notes notes) {
        requireNonNull(notes);
        this.notesList = new ArrayList<>();
        this.notesList.add(notes);
    }

    /**
     * Creates a NotesList given a List of Notes.
     * @param notesList List of Notes to be converted into NotesList.
     */
    public NotesList(List<Notes> notesList) {
        requireNonNull(notesList);
        this.notesList = new ArrayList<>(notesList);
    }

    /**
     * Creates a NotesList from the String representation.
     * @param notesListAsString NotesList String to be converted into NotesList.
     */
    public NotesList(String notesListAsString) {
        requireNonNull(notesListAsString);

        String notesAsString = notesListAsString.substring(1, notesListAsString.length() - 1);
        String[] notes = notesAsString.split(", ");
        this.notesList = Arrays.stream(notes)
                .map(Notes::new) // Map from String to Notes
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Adds a Notes to the NotesList.
     * @param notes
     */
    public NotesList add(Notes notes) {
        requireNonNull(notes);
        List<Notes> notesListCopy = new ArrayList<>(notesList);
        notesListCopy.add(notes);
        return new NotesList(notesListCopy);
    }

    /**
     * Returns the number of notes in NotesList.
     */
    public int size() {
        return notesList.size();
    }

    /**
     * Checks if NotesList has no Notes.
     */
    public boolean isEmpty() {
        return notesList.isEmpty();
    }

    @Override
    public String toString() {
        return notesList.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof NotesList otherNotesList)) {
            return false;
        }

        return notesList.equals(otherNotesList.notesList);
    }

    @Override
    public int hashCode() {
        return notesList.hashCode();
    }
}
