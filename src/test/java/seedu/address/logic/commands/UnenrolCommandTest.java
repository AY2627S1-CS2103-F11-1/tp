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
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.NotesList;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Id;

/** Tests for {@code UnenrolCommand}. */
public class UnenrolCommandTest {
    private static final CourseCode COURSE = new CourseCode("CS2103T");
    private static final Semester SEMESTER = new Semester("AY26/27-S1");
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_existingEnrolment_success() {
        UnenrolCommand command = new UnenrolCommand(ALICE.getId(), COURSE, SEMESTER);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.removeEnrolment(ALICE.getId(), COURSE, SEMESTER);

        CommandResult expected = new CommandResult(
                String.format(UnenrolCommand.MESSAGE_SUCCESS, ALICE.getId(), COURSE, SEMESTER),
                false, false, ALICE.getId());
        assertCommandSuccess(command, model, expected, expectedModel);
    }

    @Test
    public void execute_nonFinalEnrolment_success() {
        CourseCode secondCourseCode = new CourseCode("IS2108");
        Course secondCourse = new Course(secondCourseCode, SEMESTER,
                java.util.List.of(new Deliverable("MVP")));
        model.addCourse(secondCourse);
        StudentCourseEnrolment secondEnrolment = new StudentCourseEnrolment(
                ALICE.getId(), secondCourseCode, SEMESTER,
                new seedu.address.model.student.TutorialClass("T08"), new NotesList());
        model.addEnrolment(secondEnrolment);

        UnenrolCommand command = new UnenrolCommand(ALICE.getId(), COURSE, SEMESTER);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.removeEnrolment(ALICE.getId(), COURSE, SEMESTER);

        CommandResult expected = new CommandResult(
                String.format(UnenrolCommand.MESSAGE_SUCCESS, ALICE.getId(), COURSE, SEMESTER),
                false, false, ALICE.getId());
        assertCommandSuccess(command, model, expected, expectedModel);
    }

    @Test
    public void execute_unknownStudent_throwsCommandException() {
        UnenrolCommand command = new UnenrolCommand(new Id("A9999999Z"), COURSE, SEMESTER);
        assertCommandFailure(command, model,
                String.format(UnenrolCommand.MESSAGE_STUDENT_NOT_FOUND, "A9999999Z"));
    }

    @Test
    public void execute_missingEnrolment_throwsCommandException() {
        UnenrolCommand command = new UnenrolCommand(BENSON.getId(),
                new CourseCode("IS2108"), SEMESTER);
        assertCommandFailure(command, model, String.format(
                UnenrolCommand.MESSAGE_ENROLMENT_NOT_FOUND, BENSON.getId(), "IS2108", SEMESTER));
    }
}
