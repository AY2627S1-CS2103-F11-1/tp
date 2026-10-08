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
import seedu.address.model.student.Student;
import seedu.address.testutil.StudentBuilder;

/** Tests for {@code DeleteCommand}. */
public class DeleteCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validId_success() {
        DeleteCommand deleteCommand = new DeleteCommand(ALICE.getId());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteStudent(ALICE);

        assertCommandSuccess(deleteCommand, model,
                String.format(DeleteCommand.MESSAGE_DELETE_STUDENT_SUCCESS, ALICE.getName(), ALICE.getId()),
                expectedModel);
    }

    @Test
    public void execute_validNameIgnoringCase_success() {
        DeleteCommand deleteCommand = new DeleteCommand("alice pauline");
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteStudent(ALICE);

        assertCommandSuccess(deleteCommand, model,
                String.format(DeleteCommand.MESSAGE_DELETE_STUDENT_SUCCESS, ALICE.getName(), ALICE.getId()),
                expectedModel);
    }

    @Test
    public void execute_unknownId_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand(new Id("A9999999Z"));

        assertCommandFailure(deleteCommand, model,
                String.format(DeleteCommand.MESSAGE_STUDENT_ID_NOT_FOUND, "A9999999Z"));
    }

    @Test
    public void execute_unknownName_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand("Unknown Student");

        assertCommandFailure(deleteCommand, model,
                String.format(DeleteCommand.MESSAGE_STUDENT_NAME_NOT_FOUND, "Unknown Student"));
    }

    @Test
    public void execute_duplicateName_throwsCommandException() {
        Student anotherAlice = new StudentBuilder(ALICE)
                .withId("A7654321Z")
                .withEmail("alice2@u.nus.edu")
                .build();
        model.addStudent(anotherAlice);
        DeleteCommand deleteCommand = new DeleteCommand(ALICE.getName().fullName);

        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_MULTIPLE_STUDENTS_WITH_NAME);
    }
}
