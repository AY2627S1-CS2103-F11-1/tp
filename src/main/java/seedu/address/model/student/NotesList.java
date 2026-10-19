package seedu.address.model.student;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;

/** Stores a Student's Notes in a List */
public class NotesList {
    private ArrayList<Notes> notesList;

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
        ArrayList<Notes> notesList = new ArrayList<Notes>();
        notesList.add(notes);
        this.notesList = notesList;
    }

    /**
     * Adds a Notes to the NotesList.
     * @param notes
     */
    public NotesList add(Notes notes) {
        notesList.add(notes);
        return this;
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
