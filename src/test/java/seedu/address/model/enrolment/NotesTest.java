package seedu.address.model.enrolment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.model.course.Deliverable;

public class NotesTest {
    public static final String VALID_NOTES = "Used inheritance";
    public static final String VALID_DELIVERABLE = "Level 1";
    public static final String MATCHED_DELIVERABLE = "LEVEL 1";
    public static final String INVALID_STRING = "Level 1 - Used inheritance";
    public static final String VALID_STRING = "Level 1: Used inheritance";

    @Test
    public void constructor_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Notes(null, VALID_DELIVERABLE));
        assertThrows(NullPointerException.class, () -> new Notes(VALID_NOTES, null));
        assertThrows(NullPointerException.class, () -> new Notes(null, null));
    }

    @Test
    public void constructor_invalidInput_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Notes("", VALID_DELIVERABLE));
        assertThrows(IllegalArgumentException.class, () -> new Notes("  ", VALID_DELIVERABLE));
        assertThrows(IllegalArgumentException.class, () -> new Notes("a".repeat(501), VALID_DELIVERABLE));
        assertThrows(IllegalArgumentException.class, () -> new Notes("a".repeat(1000), VALID_DELIVERABLE));
        assertThrows(IllegalArgumentException.class, () -> new Notes(VALID_NOTES, ""));
        assertThrows(IllegalArgumentException.class, () -> new Notes(INVALID_STRING));
    }

    @Test
    public void constructor_validInput_success() {
        assertDoesNotThrow(() -> new Notes(VALID_NOTES, VALID_DELIVERABLE));
        assertDoesNotThrow(() -> new Notes("Level 1: Used inheritance"));
    }

    @Test
    public void changeToMatchedDeliverable() {
        Notes notes = new Notes(VALID_NOTES, VALID_DELIVERABLE);
        Notes updatedNotes = notes.changeToMatchedDeliverable(new Deliverable(MATCHED_DELIVERABLE));
        assertEquals(new Notes(VALID_NOTES, MATCHED_DELIVERABLE), updatedNotes);
    }

    @Test
    public void isValidNotesAsString() {
        assertTrue(Notes.isNotesAsStringValid(VALID_STRING));
        assertFalse(Notes.isNotesAsStringValid(INVALID_STRING));
    }

    @Test
    public void loadFromToString() {
        Notes notes = new Notes(VALID_NOTES, VALID_DELIVERABLE);
        String notesAsString = notes.toString();
        assertEquals(new Notes(VALID_NOTES, VALID_DELIVERABLE), new Notes(notesAsString));
    }
}
