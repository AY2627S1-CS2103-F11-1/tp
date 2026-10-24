package seedu.address.model.course;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

public class CourseTest {

    @Test
    public void constructor_moreThanTwentyDeliverables_throwsIllegalArgumentException() {
        List<Deliverable> deliverables = IntStream.rangeClosed(1, Course.MAX_DELIVERABLES + 1)
                .mapToObj(number -> new Deliverable("task" + number))
                .toList();

        assertThrows(IllegalArgumentException.class, () -> new Course(
                new CourseCode("CS2103T"), new Semester("AY26/27-S1"), deliverables));
    }
}
