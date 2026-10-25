package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showStudentAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.progress.CompletionStatus;
import seedu.address.model.progress.StudentCourseProgress;

public class CourseProgressOverviewCommandTest {

    private static final CourseCode COURSE = new CourseCode("CS2103T");
    private static final Semester SEMESTER = new Semester("AY26/27-S1");
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_enrolledStudents_displaysCountsInStudentOrder() {
        StudentCourseProgress aliceProgress = model.getStudentCourseProgressList().stream()
                .filter(progress -> progress.getStudentId().equals(ALICE.getId()))
                .findFirst()
                .orElseThrow();
        model.setStudentCourseProgress(aliceProgress,
                aliceProgress.withStatus(new Deliverable("MVP"), CompletionStatus.DONE));
        String expectedMessage = "Progress overview for CS2103T (AY26/27-S1):\n"
                + "Alice Pauline (A1234567Y): 1/2 completed\n"
                + "Benson Meier (A2345678X): 0/2 completed\n"
                + "Carl Kurz (A3456789W): 0/2 completed\n"
                + "Daniel Meier (A4567890V): 0/2 completed";
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(new CourseProgressOverviewCommand(COURSE, SEMESTER),
                model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_preservesCurrentFilter() {
        showStudentAtIndex(model, INDEX_FIRST_PERSON);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showStudentAtIndex(expectedModel, INDEX_FIRST_PERSON);
        String expectedMessage = "Progress overview for CS2103T (AY26/27-S1):\n"
                + "Alice Pauline (A1234567Y): 0/2 completed\n"
                + "Benson Meier (A2345678X): 0/2 completed\n"
                + "Carl Kurz (A3456789W): 0/2 completed\n"
                + "Daniel Meier (A4567890V): 0/2 completed";

        assertCommandSuccess(new CourseProgressOverviewCommand(COURSE, SEMESTER),
                model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_courseWithoutEnrolments_reportsNoStudents() {
        CourseCode emptyCourseCode = new CourseCode("CS2105");
        Course emptyCourse = new Course(emptyCourseCode, SEMESTER,
                List.of(new Deliverable("Assignment")));
        model.addCourse(emptyCourse);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(new CourseProgressOverviewCommand(emptyCourseCode, SEMESTER), model,
                String.format(CourseProgressOverviewCommand.MESSAGE_NO_ENROLMENTS,
                        emptyCourseCode, SEMESTER), expectedModel);
    }

    @Test
    public void execute_uninitialisedCourse_throwsCommandException() {
        CourseCode unknownCourse = new CourseCode("CS2105");

        assertCommandFailure(new CourseProgressOverviewCommand(unknownCourse, SEMESTER), model,
                String.format(CourseProgressOverviewCommand.MESSAGE_COURSE_NOT_FOUND,
                        unknownCourse, SEMESTER));
    }
}
