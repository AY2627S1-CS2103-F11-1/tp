package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;

/** Utility methods shared by commands that operate on a student's notes. */
final class StudentNotesCommandUtil {

    static final String MESSAGE_STUDENT_ID_NOT_FOUND =
            "No student found with ID '%s'. Please check the student ID and try again.";

    private StudentNotesCommandUtil() {
    }

    static Student findStudentById(Model model, Id studentId) throws CommandException {
        requireNonNull(model);
        requireNonNull(studentId);

        return model.getAddressBook().getStudentList().stream()
                .filter(student -> student.getId().equals(studentId))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_STUDENT_ID_NOT_FOUND, studentId)));
    }

    static StudentCourseEnrolment findEnrolmentByStudentId(Model model, Id studentId)
            throws CommandException {
        return model.getAddressBook().getEnrolmentList().stream()
                .filter(enrolment -> enrolment.getStudentId().equals(studentId))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(
                        MESSAGE_STUDENT_ID_NOT_FOUND, studentId)));
    }
}
