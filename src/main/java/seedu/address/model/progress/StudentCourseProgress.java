package seedu.address.model.progress;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.student.Id;

/**
 * Stores one student's completion statuses for the deliverables of a course in a semester.
 * Instances are immutable.
 */
public class StudentCourseProgress {

    public static final String MESSAGE_UNKNOWN_DELIVERABLE = "Deliverable is not part of this course.";

    private final Id studentId;
    private final CourseCode courseCode;
    private final Semester semester;
    private final Map<Deliverable, CompletionStatus> deliverableStatuses;

    /**
     * Creates progress for all deliverables in {@code course}, initially marked as not done.
     */
    public StudentCourseProgress(Id studentId, Course course) {
        requireAllNonNull(studentId, course);

        this.studentId = studentId;
        this.courseCode = course.getCourseCode();
        this.semester = course.getSemester();
        this.deliverableStatuses = createInitialStatuses(course);
    }

    private StudentCourseProgress(Id studentId, CourseCode courseCode, Semester semester,
                                  Map<Deliverable, CompletionStatus> deliverableStatuses) {
        requireAllNonNull(studentId, courseCode, semester, deliverableStatuses);
        deliverableStatuses.forEach((deliverable, status) -> requireAllNonNull(deliverable, status));

        this.studentId = studentId;
        this.courseCode = courseCode;
        this.semester = semester;
        this.deliverableStatuses = Collections.unmodifiableMap(new LinkedHashMap<>(deliverableStatuses));
    }

    /**
     * Restores a progress record from previously stored deliverable statuses.
     */
    public static StudentCourseProgress fromStatuses(Id studentId, CourseCode courseCode, Semester semester,
                                                      Map<Deliverable, CompletionStatus> deliverableStatuses) {
        return new StudentCourseProgress(studentId, courseCode, semester, deliverableStatuses);
    }

    public Id getStudentId() {
        return studentId;
    }

    public CourseCode getCourseCode() {
        return courseCode;
    }

    public Semester getSemester() {
        return semester;
    }

    /** Returns the status of each deliverable in insertion order. */
    public Map<Deliverable, CompletionStatus> getDeliverableStatuses() {
        return deliverableStatuses;
    }

    /** Returns the completion status of {@code deliverable}. */
    public CompletionStatus getStatus(Deliverable deliverable) {
        requireNonNull(deliverable);
        CompletionStatus status = deliverableStatuses.get(deliverable);
        if (status == null) {
            throw new IllegalArgumentException(MESSAGE_UNKNOWN_DELIVERABLE);
        }
        return status;
    }

    /**
     * Returns a copy of this progress record with {@code deliverable} assigned {@code status}.
     */
    public StudentCourseProgress withStatus(Deliverable deliverable, CompletionStatus status) {
        requireAllNonNull(deliverable, status);
        if (!deliverableStatuses.containsKey(deliverable)) {
            throw new IllegalArgumentException(MESSAGE_UNKNOWN_DELIVERABLE);
        }

        Map<Deliverable, CompletionStatus> updatedStatuses = new LinkedHashMap<>(deliverableStatuses);
        updatedStatuses.put(deliverable, status);
        return new StudentCourseProgress(studentId, courseCode, semester, updatedStatuses);
    }

    /** Returns whether this progress record belongs to the specified student and course-semester. */
    public boolean isFor(Id studentId, CourseCode courseCode, Semester semester) {
        requireAllNonNull(studentId, courseCode, semester);
        return this.studentId.equals(studentId)
                && this.courseCode.equals(courseCode)
                && this.semester.equals(semester);
    }

    private static Map<Deliverable, CompletionStatus> createInitialStatuses(Course course) {
        Map<Deliverable, CompletionStatus> statuses = new LinkedHashMap<>();
        for (Deliverable deliverable : course.getDeliverables()) {
            statuses.put(deliverable, CompletionStatus.NOT_DONE);
        }
        return Collections.unmodifiableMap(statuses);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof StudentCourseProgress otherProgress)) {
            return false;
        }
        return studentId.equals(otherProgress.studentId)
                && courseCode.equals(otherProgress.courseCode)
                && semester.equals(otherProgress.semester)
                && deliverableStatuses.equals(otherProgress.deliverableStatuses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseCode, semester, deliverableStatuses);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("studentId", studentId)
                .add("courseCode", courseCode)
                .add("semester", semester)
                .add("deliverableStatuses", deliverableStatuses)
                .toString();
    }
}
