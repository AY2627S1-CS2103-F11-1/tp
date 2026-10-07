package seedu.address.model.course;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a course and the project deliverables tracked for one semester.
 */
public class Course {
    public static final String MESSAGE_DELIVERABLES = "A course must contain at least one unique deliverable.";
    private final CourseCode courseCode;
    private final Semester semester;
    private final List<Deliverable> deliverables;

    /**
     * Creates a {@code Course} with its project deliverables.
     */
    public Course(CourseCode courseCode, Semester semester, List<Deliverable> deliverables) {
        requireAllNonNull(courseCode, semester, deliverables);
        deliverables.forEach(Objects::requireNonNull);
        checkArgument(!deliverables.isEmpty() && hasUniqueDeliverables(deliverables), MESSAGE_DELIVERABLES);
        this.courseCode = courseCode;
        this.semester = semester;
        this.deliverables = List.copyOf(deliverables);
    }

    public CourseCode getCourseCode() {
        return courseCode;
    }

    public Semester getSemester() {
        return semester;
    }

    /**
     * Returns an unmodifiable list of the course's project deliverables.
     */
    public List<Deliverable> getDeliverables() {
        return deliverables;
    }

    /**
     * Returns true when both courses refer to the same course in the same semester.
     */
    public boolean isSameCourse(Course otherCourse) {
        requireNonNull(otherCourse);
        return courseCode.equals(otherCourse.courseCode) && semester.equals(otherCourse.semester);
    }

    /**
     * Returns whether every deliverable in the list has a distinct name.
     */
    public static boolean hasUniqueDeliverables(List<Deliverable> deliverables) {
        return new HashSet<>(deliverables).size() == deliverables.size();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("courseCode", courseCode)
                .add("semester", semester)
                .add("deliverables", deliverables)
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Course otherCourse)) {
            return false;
        }
        return courseCode.equals(otherCourse.courseCode)
                && semester.equals(otherCourse.semester)
                && deliverables.equals(otherCourse.deliverables);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(courseCode, semester, deliverables);
    }
}
