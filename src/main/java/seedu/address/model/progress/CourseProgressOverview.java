package seedu.address.model.progress;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

import seedu.address.model.student.Student;

/**
 * An immutable summary of enrolled students' completed deliverables for one course-semester.
 */
public class CourseProgressOverview {

    private final int totalDeliverables;
    private final List<StudentProgressSummary> studentProgressSummaries;

    /** Creates an overview with the selected course's deliverable total and student summaries. */
    public CourseProgressOverview(int totalDeliverables, List<StudentProgressSummary> studentProgressSummaries) {
        if (totalDeliverables < 0) {
            throw new IllegalArgumentException("Total deliverables cannot be negative.");
        }
        requireAllNonNull(studentProgressSummaries);
        this.totalDeliverables = totalDeliverables;
        this.studentProgressSummaries = List.copyOf(studentProgressSummaries);
    }

    /** Returns the number of deliverables tracked by the selected course. */
    public int getTotalDeliverables() {
        return totalDeliverables;
    }

    /** Returns enrolled students and their completed-deliverable counts in display order. */
    public List<StudentProgressSummary> getStudentProgressSummaries() {
        return studentProgressSummaries;
    }

    /** A student's completed-deliverable count within the selected course-semester. */
    public static class StudentProgressSummary {
        private final Student student;
        private final int completedDeliverables;

        /** Creates a summary for one student. */
        public StudentProgressSummary(Student student, int completedDeliverables) {
            if (completedDeliverables < 0) {
                throw new IllegalArgumentException("Completed deliverables cannot be negative.");
            }
            this.student = java.util.Objects.requireNonNull(student);
            this.completedDeliverables = completedDeliverables;
        }

        /** Returns the enrolled student. */
        public Student getStudent() {
            return student;
        }

        /** Returns the number of completed deliverables for the student. */
        public int getCompletedDeliverables() {
            return completedDeliverables;
        }
    }
}
