package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.student.Student;

public abstract class StudentIterableView {
    private final Student student;

    protected StudentIterableView(Student student) {
        this.student = requireNonNull(student);
    }

    public Student getStudent() {
        return student;
    }
}
