package seedu.address.model.enrolment.exceptions;

/**
 * Signals that the operation will result in duplicate students.
 */
public class DuplicateStudentCourseEnrolmentException extends RuntimeException {
    public DuplicateStudentCourseEnrolmentException() {
        super("Operation would result in duplicate student course enrolment");
    }
}
