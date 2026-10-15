package seedu.address.model.progress;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.student.Id;

/** Tests for {@code StudentCourseProgress}. */
public class StudentCourseProgressTest {

    private static final Id STUDENT_ID = new Id("A1234567Z");
    private static final Course COURSE = new Course(
            new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
            List.of(new Deliverable("v1.1"), new Deliverable("MVP")));
    private static final Deliverable MVP = new Deliverable("MVP");

    @Test
    public void constructor_initialisesEveryDeliverableAsNotDone() {
        StudentCourseProgress progress = new StudentCourseProgress(STUDENT_ID, COURSE);

        assertEquals(CompletionStatus.NOT_DONE, progress.getStatus(new Deliverable("v1.1")));
        assertEquals(CompletionStatus.NOT_DONE, progress.getStatus(MVP));
        assertTrue(progress.isFor(STUDENT_ID, new CourseCode("CS2103T"), new Semester("AY26/27-S1")));
    }

    @Test
    public void withStatus_updatesRequestedDeliverableWithoutChangingOriginal() {
        StudentCourseProgress progress = new StudentCourseProgress(STUDENT_ID, COURSE);

        StudentCourseProgress updatedProgress = progress.withStatus(MVP, CompletionStatus.DONE);

        assertEquals(CompletionStatus.NOT_DONE, progress.getStatus(MVP));
        assertEquals(CompletionStatus.DONE, updatedProgress.getStatus(MVP));
        assertEquals(CompletionStatus.NOT_DONE, updatedProgress.getStatus(new Deliverable("v1.1")));
    }

    @Test
    public void getStatus_unknownDeliverable_throwsIllegalArgumentException() {
        StudentCourseProgress progress = new StudentCourseProgress(STUDENT_ID, COURSE);

        assertThrows(IllegalArgumentException.class, () -> progress.getStatus(new Deliverable("PE-D")));
    }

    @Test
    public void withStatus_unknownDeliverable_throwsIllegalArgumentException() {
        StudentCourseProgress progress = new StudentCourseProgress(STUDENT_ID, COURSE);

        assertThrows(IllegalArgumentException.class, () ->
                progress.withStatus(new Deliverable("PE-D"), CompletionStatus.DONE));
    }
}
