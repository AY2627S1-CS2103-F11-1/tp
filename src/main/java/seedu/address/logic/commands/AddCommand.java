package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_CLASS;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_COURSE;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_EMAIL;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_ID;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_NAME;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_EMAIL;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_ID;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Student;

/**
 * Adds a student to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a student with one or more course enrolments.\n"
            + "Parameters: "
            + PREFIX_NAME + " " + ARGUMENT_NAME + " "
            + PREFIX_ID + " " + ARGUMENT_ID + " "
            + PREFIX_EMAIL + " " + ARGUMENT_EMAIL + " "
            + PREFIX_COURSE + " " + ARGUMENT_COURSE + "[, " + ARGUMENT_COURSE + "]... "
            + PREFIX_SEMESTER + " " + ARGUMENT_SEMESTER + "[, " + ARGUMENT_SEMESTER + "]... "
            + PREFIX_CLASS + " " + ARGUMENT_CLASS + "[, " + ARGUMENT_CLASS + "]...\n"
            + "Note: Each course, semester and tutorial class must correspond"
            + " by position. At least one enrolment is required.\n"
            + "Example: " + COMMAND_WORD
            + " " + PREFIX_NAME + " " + EXAMPLE_NAME
            + " " + PREFIX_ID + " " + EXAMPLE_ID
            + " " + PREFIX_EMAIL + " " + EXAMPLE_EMAIL
            + " " + PREFIX_COURSE + " CS2103T, CS1231"
            + " " + PREFIX_SEMESTER + " AY26/27-S1, AY26/27-S1"
            + " " + PREFIX_CLASS + " T07, T02";

    public static final String MESSAGE_SUCCESS = "New student added: %1$s";

    public static final String MESSAGE_DUPLICATE_STUDENT = "This student already exists in TAssist.";

    public static final String MESSAGE_NO_ENROLMENTS = "A student must have at least one enrolment.";

    public static final String MESSAGE_COURSE_NOT_FOUND = "Course %s for semester %s does not exist. "
            + "Initialise it first.";

    private final Student student;
    private final List<StudentCourseEnrolment> initialEnrolments;

    /**
     * Creates a command to add a student with their initial course enrolments.
     */
    public AddCommand(Student student, List<StudentCourseEnrolment> enrolments) {
        requireAllNonNull(student, enrolments);
        enrolments.forEach(Objects::requireNonNull);

        if (enrolments.isEmpty()) {
            throw new IllegalArgumentException(MESSAGE_NO_ENROLMENTS);
        }

        this.student = student;
        this.initialEnrolments = List.copyOf(enrolments);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasStudent(student)) {
            throw new CommandException(MESSAGE_DUPLICATE_STUDENT);
        }

        for (StudentCourseEnrolment enrolment : initialEnrolments) {
            if (model.getCourse(
                    enrolment.getCourseCode(),
                    enrolment.getSemester()).isEmpty()) {
                throw new CommandException(String.format(
                        MESSAGE_COURSE_NOT_FOUND,
                        enrolment.getCourseCode(),
                        enrolment.getSemester()));
            }
        }

        model.addStudentWithEnrolments(student, initialEnrolments);
        model.updateFilteredStudentList(Model.PREDICATE_SHOW_ALL_STUDENTS);

        return new CommandResult(
                String.format(MESSAGE_SUCCESS, Messages.format(student)),
                false, false, student.getId());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddCommand otherAddCommand)) {
            return false;
        }

        return student.equals(otherAddCommand.student)
                && initialEnrolments.equals(otherAddCommand.initialEnrolments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, initialEnrolments);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("student", student)
                .add("initialEnrolments", initialEnrolments)
                .toString();
    }
}
