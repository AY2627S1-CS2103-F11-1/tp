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
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.progress.CompletionStatus;
import seedu.address.model.progress.StudentCourseProgress;
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

    @Test
    public void execute_incompleteDeliverableFilter_showsOnlyIncompleteStudents() {
        CourseCode courseCode = new CourseCode("CS2103T");
        Semester semester = new Semester("AY26/27-S1");
        Deliverable deliverable = new Deliverable("MVP");
        StudentCourseProgress completedProgress = model.getStudentCourseProgressList().getFirst();
        model.setStudentCourseProgress(completedProgress,
                completedProgress.withStatus(deliverable, CompletionStatus.DONE));
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        StudentMatchesEnrolmentPredicate predicate = new StudentMatchesEnrolmentPredicate(
                expectedModel.getAddressBook().getEnrolmentList(), expectedModel.getStudentCourseProgressList(),
                courseCode, semester, Optional.empty(), Optional.of(deliverable));
        expectedModel.updateFilteredStudentList(predicate);

        String expectedMessage = String.format(ListCommand.MESSAGE_FILTER_SUCCESS,
                3, courseCode, semester, ", incomplete " + deliverable);
        assertCommandSuccess(new ListCommand(courseCode, semester, Optional.empty(), Optional.of(deliverable)),
                model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_incompleteDeliverableAndTutorialFilter_showsMatchingStudents() {
        CourseCode courseCode = new CourseCode("CS2103T");
        Semester semester = new Semester("AY26/27-S1");
        TutorialClass tutorialClass = new TutorialClass("T07");
        Deliverable deliverable = new Deliverable("Final");
        StudentMatchesEnrolmentPredicate predicate = new StudentMatchesEnrolmentPredicate(
                expectedModel.getAddressBook().getEnrolmentList(), expectedModel.getStudentCourseProgressList(),
                courseCode, semester, Optional.of(tutorialClass), Optional.of(deliverable));
        expectedModel.updateFilteredStudentList(predicate);

        String description = ", tutorial " + tutorialClass + ", incomplete " + deliverable;
        String expectedMessage = String.format(ListCommand.MESSAGE_FILTER_SUCCESS,
                1, courseCode, semester, description);
        assertCommandSuccess(new ListCommand(courseCode, semester, Optional.of(tutorialClass),
                Optional.of(deliverable)), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_unknownDeliverable_throwsCommandExceptionWithoutChangingFilter() {
        showStudentAtIndex(model, INDEX_FIRST_PERSON);
        Deliverable deliverable = new Deliverable("Unknown");
        CourseCode courseCode = new CourseCode("CS2103T");
        Semester semester = new Semester("AY26/27-S1");

        assertCommandFailure(new ListCommand(courseCode, semester, Optional.empty(), Optional.of(deliverable)),
                model, String.format(ListCommand.MESSAGE_DELIVERABLE_NOT_FOUND,
                        deliverable, courseCode, semester));
    }

    @Test
    public void execute_uninitialisedCourseWithTask_throwsCommandException() {
        CourseCode courseCode = new CourseCode("CS2105");
        Semester semester = new Semester("AY26/27-S1");

        assertCommandFailure(new ListCommand(courseCode, semester, Optional.empty(),
                        Optional.of(new Deliverable("MVP"))),
                model, String.format(ListCommand.MESSAGE_COURSE_NOT_FOUND, courseCode, semester));
    }
}
