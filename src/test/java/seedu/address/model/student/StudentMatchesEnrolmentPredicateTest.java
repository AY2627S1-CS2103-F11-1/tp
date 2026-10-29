package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalStudents.ALICE;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.NotesList;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.progress.CompletionStatus;
import seedu.address.model.progress.StudentCourseProgress;

public class StudentMatchesEnrolmentPredicateTest {

    private static final CourseCode COURSE = new CourseCode("CS2103T");
    private static final Semester SEMESTER = new Semester("AY26/27-S1");
    private static final TutorialClass CLASS = new TutorialClass("T07");
    private static final Deliverable MVP = new Deliverable("MVP");

    private final List<StudentCourseEnrolment> enrolments = List.of(
            new StudentCourseEnrolment(ALICE.getId(), COURSE, SEMESTER, CLASS, new NotesList()));

    @Test
    public void test_matchingCourseAndSemester_returnsTrue() {
        StudentMatchesEnrolmentPredicate predicate = new StudentMatchesEnrolmentPredicate(
                enrolments, COURSE, SEMESTER, Optional.empty());
        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_matchingTutorialClass_returnsTrue() {
        StudentMatchesEnrolmentPredicate predicate = new StudentMatchesEnrolmentPredicate(
                enrolments, COURSE, SEMESTER, Optional.of(CLASS));
        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_nonMatchingContext_returnsFalse() {
        assertFalse(new StudentMatchesEnrolmentPredicate(enrolments,
                new CourseCode("CS2105"), SEMESTER, Optional.empty()).test(ALICE));
        assertFalse(new StudentMatchesEnrolmentPredicate(enrolments,
                COURSE, new Semester("AY25/26-S2"), Optional.empty()).test(ALICE));
        assertFalse(new StudentMatchesEnrolmentPredicate(enrolments,
                COURSE, SEMESTER, Optional.of(new TutorialClass("T08"))).test(ALICE));
    }

    @Test
    public void test_incompleteDeliverable_returnsTrue() {
        StudentCourseProgress progress = new StudentCourseProgress(ALICE.getId(),
                new Course(COURSE, SEMESTER, List.of(MVP)));
        StudentMatchesEnrolmentPredicate predicate = new StudentMatchesEnrolmentPredicate(
                enrolments, List.of(progress), COURSE, SEMESTER, Optional.empty(), Optional.of(MVP));

        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_completedDeliverable_returnsFalse() {
        StudentCourseProgress progress = new StudentCourseProgress(ALICE.getId(),
                new Course(COURSE, SEMESTER, List.of(MVP))).withStatus(MVP, CompletionStatus.DONE);
        StudentMatchesEnrolmentPredicate predicate = new StudentMatchesEnrolmentPredicate(
                enrolments, List.of(progress), COURSE, SEMESTER, Optional.empty(), Optional.of(MVP));

        assertFalse(predicate.test(ALICE));
    }
}
