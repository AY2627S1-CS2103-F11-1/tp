package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
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
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Id;
import seedu.address.model.student.TutorialClass;

/** Tests for {@code EnrolCommand}. */
public class EnrolCommandTest {
    private static final CourseCode COURSE = new CourseCode("CS2103T");
    private static final Semester SEMESTER = new Semester("AY26/27-S1");
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_existingStudent_success() {
        CourseCode newCourseCode = new CourseCode("IS2108");
        Course newCourse = new Course(newCourseCode, SEMESTER, java.util.List.of(new Deliverable("MVP")));
        model.addCourse(newCourse);
        TutorialClass tutorialClass = new TutorialClass("T08");
        EnrolCommand command = new EnrolCommand(BENSON.getId(), newCourseCode, SEMESTER, tutorialClass);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        StudentCourseEnrolment enrolment = new StudentCourseEnrolment(BENSON.getId(), newCourseCode, SEMESTER,
                tutorialClass, new seedu.address.model.enrolment.NotesList());
        expectedModel.addEnrolment(enrolment);

        CommandResult expected = new CommandResult(String.format(
                EnrolCommand.MESSAGE_SUCCESS, BENSON.getId(), newCourseCode, SEMESTER, tutorialClass),
                false, false, BENSON.getId());
        assertCommandSuccess(command, model, expected, expectedModel);
    }

    @Test
    public void execute_unknownStudent_throwsCommandException() {
        EnrolCommand command = new EnrolCommand(new Id("A9999999Z"), COURSE, SEMESTER,
                new TutorialClass("T08"));
        assertCommandFailure(command, model,
                String.format(EnrolCommand.MESSAGE_STUDENT_NOT_FOUND, "A9999999Z"));
    }

    @Test
    public void execute_duplicateEnrolment_throwsCommandException() {
        EnrolCommand command = new EnrolCommand(BENSON.getId(), COURSE, SEMESTER,
                new TutorialClass("T08"));
        assertCommandFailure(command, model,
                String.format(EnrolCommand.MESSAGE_DUPLICATE_ENROLMENT, BENSON.getId(), COURSE, SEMESTER));
    }
}
