package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.student.Id;
import seedu.address.model.student.Notes;
import seedu.address.testutil.StudentBuilder;

/** Tests for {@code NoteCommand}. */
public class NotesCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validId_success() {
        Notes notes = new Notes("Asked for help with inheritance");
        NotesCommand command = new NotesCommand(ALICE.getId(), notes);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setStudent(ALICE, new StudentBuilder(ALICE).withNotes(notes.toString()).build());

        assertCommandSuccess(command, model, new CommandResult(String.format(
                NotesCommand.MESSAGE_UPDATE_NOTES_SUCCESS, ALICE.getName(), ALICE.getId()),
                false, false, ALICE.getId()),
                expectedModel);
    }

    @Test
    public void execute_unknownId_throwsCommandException() {
        NotesCommand command = new NotesCommand(new Id("A9999999Z"), new Notes("Any note"));

        assertCommandFailure(command, model,
                String.format(StudentNotesCommandUtil.MESSAGE_STUDENT_ID_NOT_FOUND, "A9999999Z"));
    }
}
