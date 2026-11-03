package seedu.address.model.enrolment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalStudents.ALICE;

import org.junit.jupiter.api.Test;

import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.student.TutorialClass;

/** Tests the identity and copy operations of {@code StudentCourseEnrolment}. */
public class StudentCourseEnrolmentTest {
    private static final CourseCode COURSE_CODE = new CourseCode("CS2103T");
    private static final Semester SEMESTER = new Semester("AY26/27-S1");
    private static final TutorialClass TUTORIAL_CLASS = new TutorialClass("T07");

    @Test
    public void copyMethods_preserveIdentityAndReplaceRequestedField() {
        NotesList notes = new NotesList(new Notes("Discuss testing", "MVP"));
        StudentCourseEnrolment enrolment = new StudentCourseEnrolment(
                ALICE.getId(), COURSE_CODE, SEMESTER, TUTORIAL_CLASS, notes);

        assertEquals(ALICE.getId(), enrolment.withStudentId(ALICE.getId()).getStudentId());
        assertEquals(new TutorialClass("T08"), enrolment.withTutorialClass(new TutorialClass("T08"))
                .getTutorialClass());
        assertEquals(new NotesList(), enrolment.withNotes(new NotesList()).getNotes());
        assertEquals(enrolment, enrolment.withStudentId(ALICE.getId()));
    }

    @Test
    public void isFor_andIsSameEnrolment_useStudentCourseSemesterIdentity() {
        StudentCourseEnrolment enrolment = new StudentCourseEnrolment(
                ALICE.getId(), COURSE_CODE, SEMESTER, TUTORIAL_CLASS, new NotesList());
        StudentCourseEnrolment differentTutorial = new StudentCourseEnrolment(
                ALICE.getId(), COURSE_CODE, SEMESTER, new TutorialClass("T08"), new NotesList());

        assertTrue(enrolment.isFor(ALICE.getId(), COURSE_CODE, SEMESTER));
        assertTrue(enrolment.isSameEnrolment(differentTutorial));
        assertFalse(enrolment.isSameEnrolment(null));
        assertFalse(enrolment.equals(differentTutorial));
    }
}
