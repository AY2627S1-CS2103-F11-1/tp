package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BOB;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.progress.StudentCourseProgress;

public class StudentDeliverablesViewTest {
    private static final Course COURSE = new Course(new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
            List.of(new Deliverable("v1.1"), new Deliverable("MVP")));
    private static final StudentCourseProgress ALICE_PROGRESS = new StudentCourseProgress(ALICE.getId(), COURSE);

    @Test
    public void constructor_progressForDifferentStudent_throwsIllegalArgumentException() {
        StudentCourseProgress bobProgress = new StudentCourseProgress(BOB.getId(), COURSE);

        assertThrows(IllegalArgumentException.class, () ->
                new StudentDeliverablesView(ALICE, List.of(bobProgress)));
    }

    @Test
    public void getCourseProgresses_returnsDefensiveCopy() {
        List<StudentCourseProgress> mutableProgresses = new java.util.ArrayList<>(List.of(ALICE_PROGRESS));
        StudentDeliverablesView view = new StudentDeliverablesView(ALICE, mutableProgresses);

        mutableProgresses.clear();

        assertEquals(List.of(ALICE_PROGRESS), view.getCourseProgresses());
    }

    @Test
    public void equals() {
        StudentDeliverablesView view = new StudentDeliverablesView(ALICE, List.of(ALICE_PROGRESS));

        assertEquals(view, new StudentDeliverablesView(ALICE, List.of(ALICE_PROGRESS)));
        assertNotEquals(view, new StudentDeliverablesView(ALICE, List.of()));
    }
}
