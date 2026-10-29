package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.student.Student;

/**
 * Abstract class to use for split display of Student information in the UI
 */
public abstract class StudentSplitView {
    private final Student student;

    protected StudentSplitView(Student student) {
        this.student = requireNonNull(student);
    }

    public Student getStudent() {
        return student;
    }
}
