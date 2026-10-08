package seedu.address.model.student.exceptions;

/**
 * Signals that the operation will result in duplicate students.
 */
public class DuplicateStudentException extends RuntimeException {
    public DuplicateStudentException() {
        super("Operation would result in duplicate students");
    }
}
