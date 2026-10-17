package seedu.address.model.student;

import java.util.ArrayList;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/** Stores a Student's Notes in a List */
public class NotesList {
    private final ArrayList<Notes> notesList;

    public NotesList() {
        this.notesList = new ArrayList<>();
    }

    public NotesList(ArrayList<Notes> notesList) {
        this.notesList = notesList;
    }


}
