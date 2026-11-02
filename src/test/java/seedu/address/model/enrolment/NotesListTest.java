package seedu.address.model.enrolment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

public class NotesListTest {
    @Test
    public void constructor_noArguments_isEmpty() {
        assertTrue(new NotesList().isEmpty());
    }

    @Test
    public void notesConstructor_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new NotesList((Notes) null));
    }

    @Test
    public void listConstructor_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new NotesList((List<Notes>) null));
    }

    @Test
    public void stringConstructor_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new NotesList((String) null));
    }

    @Test
    public void constructor_list_containsAllNotes() {
        NotesList notesList = new NotesList(List.of(new Notes("Used 1 class for all Tasks", "Level 1"),
                new Notes("Used inheritance for different Task classes", "Level 1")));
        assertEquals(2, notesList.size());
        assertEquals(new NotesList().add(new Notes("Used 1 class for all Tasks", "Level 1"))
                .add(new Notes("Used inheritance for different Task classes", "Level 1")), notesList);
    }

    @Test
    public void getNotes_unmodifiable() {
        NotesList notesList = new NotesList(List.of(new Notes("Used 1 class for all Tasks", "Level 1"),
                new Notes("Used inheritance for different Task classes", "Level 1")));
        List<Notes> unmodifiableList = notesList.getNotes();
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.add(
                new Notes("No testing of new Task classes", "Level 1")));
    }

    @Test
    public void add_returnsNewNotesListAndDoesNotModifyOldList() {
        NotesList notesList = new NotesList();
        NotesList newNotesList = notesList.add(new Notes("Used inheritance", "Level 1"));
        assertTrue(notesList.isEmpty());
        assertEquals(new NotesList(new Notes("Used inheritance", "Level 1")), newNotesList);
    }

    @Test
    public void loadFromToString() {
        NotesList notesList = new NotesList(List.of(new Notes("Works, but unreadable", "Level 1"),
                new Notes("Passes test cases, but did not use inheritance", "Level 2")));
        String notesListAsString = notesList.toString();
        NotesList loadedNotesList = new NotesList(notesListAsString);
        assertTrue(notesList.equals(loadedNotesList));
    }
}
