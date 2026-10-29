package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.Notes;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Id;

/** Tests for {@code NotesCommand}. */
public class NotesCommandTest {
    private static final CourseCode COURSE = new CourseCode("CS2103T");
    private static final Semester SEMESTER = new Semester("AY26/27-S1");
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validId_success() {
        NotesCommand command = new NotesCommand(ALICE.getId(), COURSE, SEMESTER,
                new Notes("Added more unit tests", "MVP"));
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        StudentCourseEnrolment original = expectedModel.getAddressBook().getEnrolmentList().stream()
                .filter(enrolment -> enrolment.isFor(ALICE.getId(), COURSE, SEMESTER)).findFirst().orElseThrow();
        expectedModel.setEnrolment(original, original.withNotes(original.getNotes().add(
                new Notes("Added more unit tests", "MVP"))));
        CommandResult expected = new CommandResult(String.format(NotesCommand.MESSAGE_UPDATE_NOTES_SUCCESS,
                ALICE.getName(), ALICE.getId()), false, false, ALICE.getId());
        assertCommandSuccess(command, model, expected, expectedModel);
    }

    @Test
    public void execute_unknownId_throwsCommandException() {
        NotesCommand command = new NotesCommand(new Id("A9999999Z"), COURSE, SEMESTER,
                new Notes("Any note", "MVP"));
        assertCommandFailure(command, model,
                String.format(StudentNotesCommandUtil.MESSAGE_STUDENT_ID_NOT_FOUND, "A9999999Z"));
    }
}
