
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_COURSE;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_ID;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_COURSE;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_ID;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Id;

/**
 * Removes a student's enrolment from a specified course-semester.
 */
public class UnenrolCommand extends Command {

    public static final String COMMAND_WORD = "unenrol";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes a student's enrolment from a course-semester.\n"
            + "Parameters: "
            + PREFIX_ID + " " + ARGUMENT_ID + " "
            + PREFIX_COURSE + " " + ARGUMENT_COURSE + " "
            + PREFIX_SEMESTER + " " + ARGUMENT_SEMESTER + "\n"
            + "Example: " + COMMAND_WORD
            + " " + PREFIX_ID + " " + EXAMPLE_ID
            + " " + PREFIX_COURSE + " " + EXAMPLE_COURSE
            + " " + PREFIX_SEMESTER + " " + EXAMPLE_SEMESTER;

    public static final String MESSAGE_SUCCESS =
            "Student %s has been unenrolled from %s (%s).";

    public static final String MESSAGE_STUDENT_NOT_FOUND =
            "Student with ID %s does not exist.";

    public static final String MESSAGE_ENROLMENT_NOT_FOUND =
            "Student %s is not enrolled in %s (%s).";

    private final Id studentId;
    private final CourseCode courseCode;
    private final Semester semester;

    /**
     * Creates a command to remove the specified enrolment.
     */
    public UnenrolCommand(Id studentId, CourseCode courseCode, Semester semester) {
        requireAllNonNull(studentId, courseCode, semester);

        this.studentId = studentId;
        this.courseCode = courseCode;
        this.semester = semester;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        boolean studentExists = model.getAddressBook().getStudentList().stream()
                .anyMatch(student -> student.getId().equals(studentId));

        if (!studentExists) {
            throw new CommandException(
                    String.format(MESSAGE_STUDENT_NOT_FOUND, studentId));
        }

        // Locate the existing enrolment using its identity.
        StudentCourseEnrolment enrolment = model.getAddressBook().getEnrolmentList().stream()
                .filter(candidate -> candidate.isFor(studentId, courseCode, semester))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(
                        MESSAGE_ENROLMENT_NOT_FOUND, studentId, courseCode, semester)));

        model.removeEnrolment(
                enrolment.getStudentId(),
                enrolment.getCourseCode(),
                enrolment.getSemester());

        return new CommandResult(
                String.format(MESSAGE_SUCCESS, studentId, courseCode, semester),
                false, false, studentId);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnenrolCommand otherCommand)) {
            return false;
        }

        return studentId.equals(otherCommand.studentId)
                && courseCode.equals(otherCommand.courseCode)
                && semester.equals(otherCommand.semester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseCode, semester);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("studentId", studentId)
                .add("courseCode", courseCode)
                .add("semester", semester)
                .toString();
    }
}
