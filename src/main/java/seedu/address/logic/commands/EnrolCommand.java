
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
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
import seedu.address.model.student.Notes;
import seedu.address.model.student.TutorialClass;

/**
 * Enrols an existing student in a specified course-semester.
 */
public class EnrolCommand extends Command {

    public static final String COMMAND_WORD = "enrol";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Enrols an existing student in a course-semester.\n"
            + "Parameters: "
            + PREFIX_ID + " ID "
            + PREFIX_COURSE + " COURSE "
            + PREFIX_SEMESTER + " SEMESTER "
            + PREFIX_CLASS + " CLASS\n"
            + "Example: " + COMMAND_WORD
            + " " + PREFIX_ID + " A1234567Z"
            + " " + PREFIX_COURSE + " CS2103T"
            + " " + PREFIX_SEMESTER + " AY26/27-S1"
            + " " + PREFIX_CLASS + " T07";

    public static final String MESSAGE_SUCCESS = "Student %s enrolled in %s (%s), "
            + "tutorial class %s.";

    public static final String MESSAGE_STUDENT_NOT_FOUND = "Student with ID %s does not exist.";

    public static final String MESSAGE_COURSE_NOT_FOUND = "Course %s for semester %s does not exist. "
            + "Initialise it first.";

    public static final String MESSAGE_DUPLICATE_ENROLMENT = "Student %s is already enrolled in %s (%s).";

    private final Id studentId;
    private final CourseCode courseCode;
    private final Semester semester;
    private final TutorialClass tutorialClass;

    /**
     * Creates a command to enrol the specified student in a course-semester.
     */
    public EnrolCommand(Id studentId, CourseCode courseCode,
            Semester semester, TutorialClass tutorialClass) {
        requireAllNonNull(studentId, courseCode, semester, tutorialClass);

        this.studentId = studentId;
        this.courseCode = courseCode;
        this.semester = semester;
        this.tutorialClass = tutorialClass;
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

        if (model.getCourse(courseCode, semester).isEmpty()) {
            throw new CommandException(
                    String.format(MESSAGE_COURSE_NOT_FOUND, courseCode, semester));
        }

        StudentCourseEnrolment enrolment = new StudentCourseEnrolment(
                studentId, courseCode, semester, tutorialClass, new Notes());

        if (model.hasEnrolment(enrolment)) {
            throw new CommandException(String.format(
                    MESSAGE_DUPLICATE_ENROLMENT, studentId, courseCode, semester));
        }

        model.addEnrolment(enrolment);

        return new CommandResult(String.format(
                MESSAGE_SUCCESS, studentId, courseCode, semester, tutorialClass));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EnrolCommand otherCommand)) {
            return false;
        }

        return studentId.equals(otherCommand.studentId)
                && courseCode.equals(otherCommand.courseCode)
                && semester.equals(otherCommand.semester)
                && tutorialClass.equals(otherCommand.tutorialClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseCode, semester, tutorialClass);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("studentId", studentId)
                .add("courseCode", courseCode)
                .add("semester", semester)
                .add("tutorialClass", tutorialClass)
                .toString();
    }
}
