package seedu.address.model.student;

import java.util.ArrayList;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/** Stores a Student's Notes in a List */
public class NotesList {
    private ArrayList<Notes> notesList;

    /**
     * Creates an empty NotesList
     */
    public NotesList() {
        this.notesList = new ArrayList<>();
    }

    /**
     * Creates a NotesList
     * @param notesList
     */
    public NotesList(ArrayList<Notes> notesList) {
        this.notesList = notesList;
    }

    /**
     * Adds a Notes to the NotesList
     * @param notes
     */
    public void add(Notes notes) {
        notesList.add(notes);
    }
}
