package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;

/**
 * Jackson-friendly version of {@link Course}.
 */
class JsonAdaptedCourse {
    private final String courseCode;
    private final String semester;
    private final List<String> deliverables = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedCourse} with the supplied JSON properties.
     */
    @JsonCreator
    public JsonAdaptedCourse(@JsonProperty("courseCode") String courseCode,
                             @JsonProperty("semester") String semester,
                             @JsonProperty("deliverables") List<String> deliverables) {
        this.courseCode = courseCode;
        this.semester = semester;
        if (deliverables != null) {
            this.deliverables.addAll(deliverables);
        }
    }

    /**
     * Converts a given {@code Course} into this class for Jackson use.
     */
    public JsonAdaptedCourse(Course source) {
        courseCode = source.getCourseCode().value;
        semester = source.getSemester().value;
        deliverables.addAll(source.getDeliverables().stream().map(deliverable -> deliverable.name).toList());
    }

    /**
     * Converts this Jackson-friendly adapted course object into the model's {@code Course} object.
     */
    public Course toModelType() throws IllegalValueException {
        if (courseCode == null || !CourseCode.isValidCourseCode(courseCode)) {
            throw new IllegalValueException(CourseCode.MESSAGE_CONSTRAINTS);
        }
        if (semester == null || !Semester.isValidSemester(semester)) {
            throw new IllegalValueException(Semester.MESSAGE_CONSTRAINTS);
        }
        if (deliverables.isEmpty() || deliverables.stream().anyMatch(
                deliverable -> deliverable == null || !Deliverable.isValidDeliverable(deliverable))) {
            throw new IllegalValueException(Deliverable.MESSAGE_CONSTRAINTS);
        }
        List<Deliverable> modelDeliverables = deliverables.stream().map(Deliverable::new).toList();
        if (!Course.hasUniqueDeliverables(modelDeliverables)) {
            throw new IllegalValueException(Course.MESSAGE_DELIVERABLES);
        }
        return new Course(new CourseCode(courseCode), new Semester(semester), modelDeliverables);
    }
}
