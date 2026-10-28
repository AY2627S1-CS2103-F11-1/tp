package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;

/** Utility methods shared by commands that operate on a student's notes. */
final class StudentNotesCommandUtil {

    private static final String MESSAGE_ENROLMENT_NOT_FOUND = "Student %s is not enrolled in %s (%s).";

    static final String MESSAGE_STUDENT_ID_NOT_FOUND = "No student found with ID '%s'. "
            + "Please check the student ID and try again.";

    private StudentNotesCommandUtil() {
    }

    static Student findStudentById(Model model, Id studentId) throws CommandException {
        requireNonNull(model);
        requireNonNull(studentId);

        return model.getAddressBook().getStudentList().stream()
                .filter(student -> student.getId().equals(studentId))
                .findFirst()
                .orElseThrow(() -> new CommandException(
                        String.format(MESSAGE_STUDENT_ID_NOT_FOUND, studentId)));
    }

    /**
     * Finds the enrolment matching the specified student and course-semester.
     *
     * @throws CommandException if no matching enrolment exists.
     */
    static StudentCourseEnrolment findEnrolment(
            Model model, Id studentId, CourseCode courseCode,
            Semester semester) throws CommandException {

        requireAllNonNull(model, studentId, courseCode, semester);

        return model.getAddressBook().getEnrolmentList().stream()
                .filter(enrolment -> enrolment.isFor(studentId, courseCode, semester))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(
                        MESSAGE_ENROLMENT_NOT_FOUND, studentId, courseCode, semester)));
    }
}
