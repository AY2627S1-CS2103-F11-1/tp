package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.progress.StudentCourseProgress;
import seedu.address.model.student.Student;

/**
 * Immutable data required to display one student's deliverables in the UI.
 */
public class StudentDeliverablesView {
    private final Student student;
    private final List<StudentCourseProgress> courseProgresses;

    /**
     * Creates a UI view for {@code student} using the supplied course-progress records.
     */
    public StudentDeliverablesView(Student student, List<StudentCourseProgress> courseProgresses) {
        requireAllNonNull(student, courseProgresses);
        courseProgresses.forEach(progress -> {
            requireNonNull(progress);
            if (!progress.getStudentId().equals(student.getId())) {
                throw new IllegalArgumentException("All progress records must belong to the selected student.");
            }
        });

        this.student = student;
        this.courseProgresses = List.copyOf(courseProgresses);
    }

    public Student getStudent() {
        return student;
    }

    public List<StudentCourseProgress> getCourseProgresses() {
        return courseProgresses;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof StudentDeliverablesView otherView
                && student.equals(otherView.student)
                && courseProgresses.equals(otherView.courseProgresses));
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, courseProgresses);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("student", student)
                .add("courseProgresses", courseProgresses)
                .toString();
    }
}
