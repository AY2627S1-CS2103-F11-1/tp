package seedu.address.logic.commands;





import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalStudents.ALICE;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.enrolment.NotesList;

public class CommandResultTest {
    @Test
    public void equals() {
        CommandResult commandResult = new CommandResult("feedback");

        // same values -> returns true
        assertTrue(commandResult.equals(new CommandResult("feedback")));
        assertTrue(commandResult.equals(new CommandResult("feedback", false, false)));

        // same object -> returns true
        assertTrue(commandResult.equals(commandResult));

        // null -> returns false
        assertFalse(commandResult.equals(null));

        // different types -> returns false
        assertFalse(commandResult.equals(0.5f));

        // different feedbackToUser value -> returns false
        assertFalse(commandResult.equals(new CommandResult("different")));

        // different showHelp value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", true, false)));

        // different exit value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", false, true)));
    }

    @Test
    public void hashcode() {
        CommandResult commandResult = new CommandResult("feedback");

        // same values -> returns same hashcode
        assertEquals(commandResult.hashCode(), new CommandResult("feedback").hashCode());

        // different feedbackToUser value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("different").hashCode());

        // different showHelp value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", true, false).hashCode());

        // different exit value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", false, true).hashCode());
    }

    @Test
    public void toStringMethod() {
        CommandResult commandResult = new CommandResult("feedback");
        String expected = CommandResult.class.getCanonicalName() + "{feedbackToUser="
                + commandResult.getFeedbackToUser() + ", showHelp=" + commandResult.isShowHelp()
                + ", exit=" + commandResult.isExit() + ", studentIdToFocus=null"
                + ", studentSplitView=null}";
        assertEquals(expected, commandResult.toString());
    }

    @Test
    public void studentSplitView_present_returnsView() {
        StudentDeliverablesView view = new StudentDeliverablesView(ALICE, List.of());
        CommandResult commandResult = new CommandResult("feedback", view);

        assertTrue(commandResult.containsStudentSplitView());
        assertEquals(view, commandResult.getStudentSplitView());
    }

    @Test
    public void studentViews_areTypedAndExposeStudentId() {
        StudentDeliverablesView deliverablesView = new StudentDeliverablesView(ALICE, List.of());
        CommandResult deliverablesResult = new CommandResult("deliverables", deliverablesView);
        StudentNotesListView notesView = new StudentNotesListView(ALICE, new NotesList());
        CommandResult notesResult = new CommandResult("notes", notesView);

        assertEquals(ALICE.getId(), deliverablesResult.getStudentIdToFocus().orElseThrow());
        assertTrue(deliverablesResult.containsStudentDeliverablesView());
        assertEquals(deliverablesView, deliverablesResult.getStudentDeliverablesView());
        assertTrue(notesResult.containsStudentNotesListView());
        assertEquals(notesView, notesResult.getStudentNotesListView());
        assertFalse(deliverablesResult.equals(notesResult));
    }

    @Test
    public void viewGetters_withoutView_throwAssertionError() {
        CommandResult commandResult = new CommandResult("feedback");

        assertFalse(commandResult.containsStudentSplitView());
        assertFalse(commandResult.containsStudentDeliverablesView());
        assertFalse(commandResult.containsStudentNotesListView());
        assertThrows(AssertionError.class, commandResult::getStudentSplitView);
        assertThrows(AssertionError.class, commandResult::getStudentDeliverablesView);
        assertThrows(AssertionError.class, commandResult::getStudentNotesListView);
    }
}
