package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.enrolment.Notes;
import seedu.address.model.enrolment.NotesList;

public class NotesListTest {

    @Test
    public void loadFromToString() {
        NotesList notesList = new NotesList(List.of(new Notes("Works, but unreadable", "Level 1"),
                new Notes("Passes test cases, but did not use inheritance", "Level 2")));
        String notesListAsString = notesList.toString();
        NotesList loadedNotesList = new NotesList(notesListAsString);
        assertTrue(notesList.equals(loadedNotesList));
    }
}
