package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BENSON;
import static seedu.address.testutil.TypicalStudents.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.student.Id;
import seedu.address.testutil.StudentBuilder;

/** Tests for {@code DeleteNotesCommand}. */
public class DeleteNotesCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_studentWithNotes_success() {
        DeleteNotesCommand command = new DeleteNotesCommand(ALICE.getId());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setStudent(ALICE, new StudentBuilder(ALICE).withNotes().build());

        CommandResult expectedResult = new CommandResult(String.format(
                DeleteNotesCommand.MESSAGE_DELETE_NOTES_SUCCESS, ALICE.getName(), ALICE.getId()),
                false, false, ALICE.getId());

        assertCommandSuccess(command, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_studentWithoutNotes_throwsCommandException() {
        DeleteNotesCommand command = new DeleteNotesCommand(BENSON.getId());

        assertCommandFailure(command, model, DeleteNotesCommand.MESSAGE_NO_NOTES);
    }

    @Test
    public void execute_unknownId_throwsCommandException() {
        DeleteNotesCommand command = new DeleteNotesCommand(new Id("A9999999Z"));

        assertCommandFailure(command, model,
                String.format(StudentNotesCommandUtil.MESSAGE_STUDENT_ID_NOT_FOUND, "A9999999Z"));
    }
}
