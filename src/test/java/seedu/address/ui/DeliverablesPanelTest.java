package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.progress.CompletionStatus;
import seedu.address.model.progress.StudentCourseProgress;
import seedu.address.model.student.Id;

public class DeliverablesPanelTest {
    private static final Deliverable MVP = new Deliverable("MVP");
    private static final Course COURSE = new Course(new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
            List.of(new Deliverable("v1.1"), MVP));

    @Test
    public void formatCourseHeading_noCompletedDeliverables_showsZeroOfTotal() {
        StudentCourseProgress progress = new StudentCourseProgress(new Id("A1234567Z"), COURSE);

        assertEquals("CS2103T · AY26/27-S1 (0/2 complete)", DeliverablesPanel.formatCourseHeading(progress));
    }

    @Test
    public void formatCourseHeading_completedDeliverable_showsCompletedCount() {
        StudentCourseProgress progress = new StudentCourseProgress(new Id("A1234567Z"), COURSE)
                .withStatus(MVP, CompletionStatus.DONE);

        assertEquals("CS2103T · AY26/27-S1 (1/2 complete)", DeliverablesPanel.formatCourseHeading(progress));
    }
}
