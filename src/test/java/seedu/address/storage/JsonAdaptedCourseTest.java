package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;

public class JsonAdaptedCourseTest {
    private static final Course CS2103 = new Course(new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
            List.of(new Deliverable("v1.2"), new Deliverable("MVP")));

    @Test
    public void toModelType_validCourse_success() throws Exception {
        assertEquals(CS2103, new JsonAdaptedCourse(CS2103).toModelType());
    }

    @Test
    public void toModelType_duplicateDeliverables_throwsIllegalValueException() {
        JsonAdaptedCourse adaptedCourse = new JsonAdaptedCourse("CS2103T", "AY26/27-S1", List.of("v1.2", "V1.2"));
        assertThrows(IllegalValueException.class, "A course must contain at least one unique deliverable.",
                adaptedCourse::toModelType);
    }
}
