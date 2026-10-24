package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BENSON;
import static seedu.address.testutil.TypicalStudents.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Id;

/** Tests for {@code DeleteNotesCommand}. */
public class DeleteNotesCommandTest {
    private static final CourseCode COURSE = new CourseCode("CS2103T");
    private static final Semester SEMESTER = new Semester("AY26/27-S1");
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_studentWithNotes_success() {
        DeleteNotesCommand command = new DeleteNotesCommand(ALICE.getId(), COURSE, SEMESTER);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        StudentCourseEnrolment original = expectedModel.getAddressBook().getEnrolmentList().stream()
                .filter(enrolment -> enrolment.isFor(ALICE.getId(), COURSE, SEMESTER)).findFirst().orElseThrow();
        expectedModel.setEnrolment(original, original.withNotes(new seedu.address.model.enrolment.NotesList()));
        CommandResult expected = new CommandResult(String.format(DeleteNotesCommand.MESSAGE_DELETE_NOTES_SUCCESS,
                ALICE.getName(), ALICE.getId()), false, false, ALICE.getId());
        assertCommandSuccess(command, model, expected, expectedModel);
    }

    @Test
    public void execute_studentWithoutNotes_throwsCommandException() {
        DeleteNotesCommand command = new DeleteNotesCommand(BENSON.getId(), COURSE, SEMESTER);
        assertCommandFailure(command, model, DeleteNotesCommand.MESSAGE_NO_NOTES);
    }

    @Test
    public void execute_unknownId_throwsCommandException() {
        DeleteNotesCommand command = new DeleteNotesCommand(new Id("A9999999Z"), COURSE, SEMESTER);
        assertCommandFailure(command, model,
                String.format(StudentNotesCommandUtil.MESSAGE_STUDENT_ID_NOT_FOUND, "A9999999Z"));
    }
}
