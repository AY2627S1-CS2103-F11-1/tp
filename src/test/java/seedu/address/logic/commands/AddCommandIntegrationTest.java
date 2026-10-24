package seedu.address.logic.commands;






import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalStudents.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.NotesList;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Student;
import seedu.address.model.student.TutorialClass;
import seedu.address.testutil.StudentBuilder;

/**
 * Contains integration tests (interaction with the Model) for
 * {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newStudent_success() {
        Student validStudent = new StudentBuilder().withName("New Student")
                .withId("A1122334M").withEmail("new.student@u.nus.edu").build();
        StudentCourseEnrolment enrolment = new StudentCourseEnrolment(validStudent.getId(),
                new CourseCode("CS2103T"), new Semester("AY26/27-S1"), new TutorialClass("T08"), new NotesList());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addStudentWithEnrolment(validStudent, enrolment);
        expectedModel.updateFilteredStudentList(Model.PREDICATE_SHOW_ALL_STUDENTS);

        CommandResult expectedResult = new CommandResult(
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validStudent)),
                false, false, validStudent.getId());
        assertCommandSuccess(new AddCommand(validStudent, List.of(enrolment)), model, expectedResult,
                expectedModel);
    }

    @Test
    public void execute_duplicateStudent_throwsCommandException() {
        Student studentInList = model.getAddressBook().getStudentList().get(0);
        assertCommandFailure(new AddCommand(studentInList, List.of(new StudentCourseEnrolment(
                studentInList.getId(), new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
                new TutorialClass("T07"), new NotesList()))), model,
                AddCommand.MESSAGE_DUPLICATE_STUDENT);
    }

}
