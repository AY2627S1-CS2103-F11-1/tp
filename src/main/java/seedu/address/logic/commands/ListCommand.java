package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import java.util.Objects;
import java.util.Optional;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.student.StudentMatchesEnrolmentPredicate;
import seedu.address.model.student.TutorialClass;

/**
 * Lists all students in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all students or filters them by enrolment.\n"
            + "Parameters: [" + PREFIX_COURSE + " COURSE " + PREFIX_SEMESTER + " SEMESTER "
            + "[" + PREFIX_CLASS + " CLASS]]\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + "\n"
            + "  " + COMMAND_WORD + " " + PREFIX_COURSE + " CS2103T "
            + PREFIX_SEMESTER + " AY26/27-S1\n"
            + "  " + COMMAND_WORD + " " + PREFIX_COURSE + " CS2103T "
            + PREFIX_SEMESTER + " AY26/27-S1 " + PREFIX_CLASS + " T07";

    public static final String MESSAGE_SUCCESS = "Displayed %1$d student(s).";

    public static final String MESSAGE_FILTER_SUCCESS = "Displayed %1$d student(s) enrolled in %2$s (%3$s)%4$s.";

    public static final String MESSAGE_NO_MATCHES = "No students found for %1$s (%2$s)%3$s.";

    public static final String MESSAGE_NO_STUDENTS = "No student records found. Add a student to get started.";

    private final Optional<CourseCode> courseCode;
    private final Optional<Semester> semester;
    private final Optional<TutorialClass> tutorialClass;

    /** Creates a command that lists every student. */
    public ListCommand() {
        courseCode = Optional.empty();
        semester = Optional.empty();
        tutorialClass = Optional.empty();
    }

    /** Creates a command that lists students matching the selected enrolment. */
    public ListCommand(CourseCode courseCode, Semester semester, Optional<TutorialClass> tutorialClass) {
        this.courseCode = Optional.of(requireNonNull(courseCode));
        this.semester = Optional.of(requireNonNull(semester));
        this.tutorialClass = requireNonNull(tutorialClass);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        int studentCount = model.getFilteredStudentList().size();
        if (studentCount == 0) {
            throw new CommandException(MESSAGE_NO_STUDENTS);
        }
        if (courseCode.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_SUCCESS, studentCount));
        }

        StudentMatchesEnrolmentPredicate predicate = new StudentMatchesEnrolmentPredicate(
                model.getAddressBook().getEnrolmentList(), courseCode.orElseThrow(),
                semester.orElseThrow(), tutorialClass);
        model.updateFilteredStudentList(predicate);

        String tutorialDescription = tutorialClass
                .map(value -> ", tutorial " + value)
                .orElse("");
        int matchingStudentCount = model.getFilteredStudentList().size();
        if (matchingStudentCount == 0) {
            return new CommandResult(String.format(MESSAGE_NO_MATCHES,
                    courseCode.orElseThrow(), semester.orElseThrow(), tutorialDescription));
        }
        return new CommandResult(String.format(MESSAGE_FILTER_SUCCESS, matchingStudentCount,
                courseCode.orElseThrow(), semester.orElseThrow(), tutorialDescription));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ListCommand otherCommand
                && courseCode.equals(otherCommand.courseCode)
                && semester.equals(otherCommand.semester)
                && tutorialClass.equals(otherCommand.tutorialClass));
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode, semester, tutorialClass);
    }
}
