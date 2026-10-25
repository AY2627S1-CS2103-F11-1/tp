package seedu.address.logic.commands;






import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showStudentAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalStudents.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.student.StudentMatchesEnrolmentPredicate;
import seedu.address.model.student.TutorialClass;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS,
                expectedModel.getFilteredStudentList().size());
        assertCommandSuccess(new ListCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showStudentAtIndex(model, INDEX_FIRST_PERSON);
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS,
                expectedModel.getFilteredStudentList().size());
        assertCommandSuccess(new ListCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_emptyAddressBook_throwsCommandException() {
        Model emptyModel = new ModelManager();
        assertCommandFailure(new ListCommand(), emptyModel, ListCommand.MESSAGE_NO_STUDENTS);
    }

    @Test
    public void execute_courseFilter_showsEnrolledStudents() {
        CourseCode courseCode = new CourseCode("CS2103T");
        Semester semester = new Semester("AY26/27-S1");
        StudentMatchesEnrolmentPredicate predicate = new StudentMatchesEnrolmentPredicate(
                expectedModel.getAddressBook().getEnrolmentList(), courseCode, semester, Optional.empty());
        expectedModel.updateFilteredStudentList(predicate);

        String expectedMessage = String.format(ListCommand.MESSAGE_FILTER_SUCCESS,
                expectedModel.getFilteredStudentList().size(), courseCode, semester, "");
        assertCommandSuccess(new ListCommand(courseCode, semester, Optional.empty()),
                model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_tutorialFilter_showsOnlyMatchingStudents() {
        CourseCode courseCode = new CourseCode("CS2103T");
        Semester semester = new Semester("AY26/27-S1");
        TutorialClass tutorialClass = new TutorialClass("T07");
        StudentMatchesEnrolmentPredicate predicate = new StudentMatchesEnrolmentPredicate(
                expectedModel.getAddressBook().getEnrolmentList(), courseCode, semester,
                Optional.of(tutorialClass));
        expectedModel.updateFilteredStudentList(predicate);

        String expectedMessage = String.format(ListCommand.MESSAGE_FILTER_SUCCESS,
                1, courseCode, semester, ", tutorial " + tutorialClass);
        assertCommandSuccess(new ListCommand(courseCode, semester, Optional.of(tutorialClass)),
                model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validFilterWithNoMatches_showsEmptyResult() {
        CourseCode courseCode = new CourseCode("CS2105");
        Semester semester = new Semester("AY26/27-S1");
        StudentMatchesEnrolmentPredicate predicate = new StudentMatchesEnrolmentPredicate(
                expectedModel.getAddressBook().getEnrolmentList(), courseCode, semester, Optional.empty());
        expectedModel.updateFilteredStudentList(predicate);

        String expectedMessage = String.format(ListCommand.MESSAGE_NO_MATCHES, courseCode, semester, "");
        assertCommandSuccess(new ListCommand(courseCode, semester, Optional.empty()),
                model, expectedMessage, expectedModel);
    }
}
