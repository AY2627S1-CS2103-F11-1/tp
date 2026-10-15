package seedu.address.storage;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.progress.CompletionStatus;
import seedu.address.model.progress.StudentCourseProgress;
import seedu.address.model.student.Id;

/**
 * Jackson-friendly version of {@link StudentCourseProgress}.
 */
class JsonAdaptedStudentCourseProgress {

    private final String studentId;
    private final String courseCode;
    private final String semester;
    private final Map<String, CompletionStatus> deliverableStatuses = new LinkedHashMap<>();

    /** Constructs a progress record from JSON properties. */
    @JsonCreator
    public JsonAdaptedStudentCourseProgress(@JsonProperty("studentId") String studentId,
                                             @JsonProperty("courseCode") String courseCode,
                                             @JsonProperty("semester") String semester,
                                             @JsonProperty("deliverableStatuses")
                                             Map<String, CompletionStatus> deliverableStatuses) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.semester = semester;
        if (deliverableStatuses != null) {
            this.deliverableStatuses.putAll(deliverableStatuses);
        }
    }

    /** Converts a model progress record into a Jackson-friendly record. */
    public JsonAdaptedStudentCourseProgress(StudentCourseProgress source) {
        studentId = source.getStudentId().id;
        courseCode = source.getCourseCode().value;
        semester = source.getSemester().value;
        source.getDeliverableStatuses().forEach((deliverable, status) ->
                deliverableStatuses.put(deliverable.name, status));
    }

    /** Converts this JSON record to a model progress record. */
    public StudentCourseProgress toModelType() throws IllegalValueException {
        if (studentId == null || !Id.isValidId(studentId)) {
            throw new IllegalValueException(Id.MESSAGE_CONSTRAINTS);
        }
        if (courseCode == null || !CourseCode.isValidCourseCode(courseCode)) {
            throw new IllegalValueException(CourseCode.MESSAGE_CONSTRAINTS);
        }
        if (semester == null || !Semester.isValidSemester(semester)) {
            throw new IllegalValueException(Semester.MESSAGE_CONSTRAINTS);
        }
        if (deliverableStatuses.isEmpty() || deliverableStatuses.entrySet().stream().anyMatch(entry ->
                entry.getKey() == null || !Deliverable.isValidDeliverable(entry.getKey())
                        || entry.getValue() == null)) {
            throw new IllegalValueException("Progress must contain valid deliverable statuses.");
        }

        Map<Deliverable, CompletionStatus> modelStatuses = new LinkedHashMap<>();
        for (Map.Entry<String, CompletionStatus> entry : deliverableStatuses.entrySet()) {
            Deliverable deliverable = new Deliverable(entry.getKey());
            if (modelStatuses.put(deliverable, entry.getValue()) != null) {
                throw new IllegalValueException("Progress contains duplicate deliverables.");
            }
        }
        return StudentCourseProgress.fromStatuses(new Id(studentId), new CourseCode(courseCode),
                new Semester(semester), modelStatuses);
    }
}
