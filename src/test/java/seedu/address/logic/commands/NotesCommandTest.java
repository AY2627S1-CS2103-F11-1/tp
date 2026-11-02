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
    private static final CourseCode INVALID_COURSE = new CourseCode("CS2100");
    private static final Semester SEMESTER = new Semester("AY26/27-S1");
    private static final Semester INVALID_SEMESTER = new Semester("AY26/27-S2");
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
    public void execute_caseInsensitiveDeliverable_success() {
        NotesCommand command = new NotesCommand(ALICE.getId(), COURSE, SEMESTER,
                new Notes("Added more unit tests", "mvp"));

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

    @Test
    public void execute_notEnrolled_throwsCommandException() {
        NotesCommand invalidCourseCommand = new NotesCommand(ALICE.getId(), INVALID_COURSE, SEMESTER,
                new Notes("Any note", "MVP"));
        NotesCommand invalidSemesterCommand = new NotesCommand(ALICE.getId(), COURSE, INVALID_SEMESTER,
                new Notes("Any note", "MVP"));

        assertCommandFailure(invalidCourseCommand, model, String.format(
                StudentNotesCommandUtil.MESSAGE_ENROLMENT_NOT_FOUND, ALICE.getId(), INVALID_COURSE, SEMESTER));
        assertCommandFailure(invalidSemesterCommand, model, String.format(
                StudentNotesCommandUtil.MESSAGE_ENROLMENT_NOT_FOUND, ALICE.getId(), COURSE, INVALID_SEMESTER));
    }

    @Test
    public void execute_invalidDeliverable_throwsCommandException() {
        NotesCommand invalidDeliverableCommand = new NotesCommand(ALICE.getId(), COURSE, SEMESTER,
                new Notes("Any note", "Level 1"));

        assertCommandFailure(invalidDeliverableCommand, model,
                String.format(NotesCommand.MESSAGE_DELIVERABLE_NOT_IN_COURSE));
    }
}
