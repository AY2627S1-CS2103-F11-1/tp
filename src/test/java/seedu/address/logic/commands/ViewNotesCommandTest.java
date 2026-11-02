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
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.Notes;
import seedu.address.model.enrolment.NotesList;
import seedu.address.model.student.Id;

/** Tests for {@code ViewNotesCommand}. */
public class ViewNotesCommandTest {

    private static final CourseCode COURSE_CODE = new CourseCode("CS2103T");
    private static final CourseCode INVALID_COURSE_CODE = new CourseCode("CS2100");
    private static final Semester SEMESTER = new Semester("AY26/27-S1");
    private static final Semester INVALID_SEMESTER = new Semester("AY26/27-S2");

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_studentWithNotes_success() {
        ViewNotesCommand command = new ViewNotesCommand(ALICE.getId(), COURSE_CODE, SEMESTER);

        CommandResult expectedResult = new CommandResult(String.format(
                ViewNotesCommand.MESSAGE_VIEW_NOTES_SUCCESS, ALICE.getName(), ALICE.getId(), COURSE_CODE),
                new StudentNotesListView(ALICE, new NotesList(new Notes("Needs help with testing", "MVP"))));

        assertCommandSuccess(command, model, expectedResult, model);
    }

    @Test
    public void execute_studentWithoutNotes_success() {
        ViewNotesCommand command = new ViewNotesCommand(BENSON.getId(), COURSE_CODE, SEMESTER);

        CommandResult expectedResult = new CommandResult(String.format(ViewNotesCommand.MESSAGE_NO_NOTES,
                BENSON.getName(), BENSON.getId(), COURSE_CODE));

        assertCommandSuccess(command, model, expectedResult, model);
    }

    @Test
    public void execute_unknownId_throwsCommandException() {
        ViewNotesCommand command = new ViewNotesCommand(new Id("A9999999Z"), COURSE_CODE, SEMESTER);

        assertCommandFailure(command, model,
                String.format(StudentNotesCommandUtil.MESSAGE_STUDENT_ID_NOT_FOUND, "A9999999Z"));
    }

    @Test
    public void execute_notEnrolled_throwsCommandException() {
        ViewNotesCommand invalidCourseCommand = new ViewNotesCommand(ALICE.getId(),
                INVALID_COURSE_CODE, SEMESTER);
        ViewNotesCommand invalidSemesterCommand = new ViewNotesCommand(ALICE.getId(),
                COURSE_CODE, INVALID_SEMESTER);

        assertCommandFailure(invalidCourseCommand, model, String.format(
                StudentNotesCommandUtil.MESSAGE_ENROLMENT_NOT_FOUND, ALICE.getId(), INVALID_COURSE_CODE, SEMESTER));
        assertCommandFailure(invalidSemesterCommand, model, String.format(
                StudentNotesCommandUtil.MESSAGE_ENROLMENT_NOT_FOUND, ALICE.getId(), COURSE_CODE, INVALID_SEMESTER));
    }
}
