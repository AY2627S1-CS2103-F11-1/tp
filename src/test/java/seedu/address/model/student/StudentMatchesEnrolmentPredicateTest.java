package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalStudents.ALICE;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.NotesList;
import seedu.address.model.enrolment.StudentCourseEnrolment;

public class StudentMatchesEnrolmentPredicateTest {

    private static final CourseCode COURSE = new CourseCode("CS2103T");
    private static final Semester SEMESTER = new Semester("AY26/27-S1");
    private static final TutorialClass CLASS = new TutorialClass("T07");

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
}
