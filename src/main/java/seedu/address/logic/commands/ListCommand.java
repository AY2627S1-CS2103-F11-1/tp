package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import java.util.Objects;
import java.util.Optional;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
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
            + "[" + PREFIX_CLASS + " CLASS] [" + PREFIX_TASK + " DELIVERABLE]]\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + "\n"
            + "  " + COMMAND_WORD + " " + PREFIX_COURSE + " CS2103T "
            + PREFIX_SEMESTER + " AY26/27-S1\n"
            + "  " + COMMAND_WORD + " " + PREFIX_COURSE + " CS2103T "
            + PREFIX_SEMESTER + " AY26/27-S1 " + PREFIX_CLASS + " T07\n"
            + "  " + COMMAND_WORD + " " + PREFIX_COURSE + " CS2103T "
            + PREFIX_SEMESTER + " AY26/27-S1 " + PREFIX_TASK + " v1.3";

    public static final String MESSAGE_SUCCESS = "Displayed %1$d student(s).";

    public static final String MESSAGE_FILTER_SUCCESS = "Displayed %1$d student(s) enrolled in %2$s (%3$s)%4$s.";

    public static final String MESSAGE_NO_MATCHES = "No students found for %1$s (%2$s)%3$s.";

    public static final String MESSAGE_NO_STUDENTS = "No student records found. Add a student to get started.";

    public static final String MESSAGE_COURSE_NOT_FOUND = "Course %s for semester %s has not been initialised.";

    public static final String MESSAGE_DELIVERABLE_NOT_FOUND =
            "Deliverable %s is not tracked for %s (%s).";

    private final Optional<CourseCode> courseCode;
    private final Optional<Semester> semester;
    private final Optional<TutorialClass> tutorialClass;
    private final Optional<Deliverable> incompleteDeliverable;

    /** Creates a command that lists every student. */
    public ListCommand() {
        courseCode = Optional.empty();
        semester = Optional.empty();
        tutorialClass = Optional.empty();
        incompleteDeliverable = Optional.empty();
    }

    /** Creates a command that lists students matching the selected enrolment. */
    public ListCommand(CourseCode courseCode, Semester semester, Optional<TutorialClass> tutorialClass) {
        this(courseCode, semester, tutorialClass, Optional.empty());
    }

    /** Creates a command that may additionally filter by an incomplete deliverable. */
    public ListCommand(CourseCode courseCode, Semester semester, Optional<TutorialClass> tutorialClass,
            Optional<Deliverable> incompleteDeliverable) {
        this.courseCode = Optional.of(requireNonNull(courseCode));
        this.semester = Optional.of(requireNonNull(semester));
        this.tutorialClass = requireNonNull(tutorialClass);
        this.incompleteDeliverable = requireNonNull(incompleteDeliverable);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        int studentCount = model.getAddressBook().getStudentList().size();
        if (studentCount == 0) {
            throw new CommandException(MESSAGE_NO_STUDENTS);
        }
        if (courseCode.isEmpty()) {
            model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
            return new CommandResult(String.format(MESSAGE_SUCCESS, studentCount));
        }

        CourseCode selectedCourseCode = courseCode.orElseThrow();
        Semester selectedSemester = semester.orElseThrow();
        TutorialClass selectedTutorialClass = tutorialClass.orElse(null);
        Deliverable selectedDeliverable = incompleteDeliverable.orElse(null);

        var course = model.getCourse(selectedCourseCode, selectedSemester)
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_COURSE_NOT_FOUND,
                        selectedCourseCode, selectedSemester)));
        if (incompleteDeliverable.isPresent()) {
            if (!course.getDeliverables().contains(selectedDeliverable)) {
                throw new CommandException(String.format(MESSAGE_DELIVERABLE_NOT_FOUND,
                        selectedDeliverable, selectedCourseCode, selectedSemester));
            }
        }

        StudentMatchesEnrolmentPredicate predicate = new StudentMatchesEnrolmentPredicate(
                model.getAddressBook().getEnrolmentList(), model.getStudentCourseProgressList(),
                selectedCourseCode, selectedSemester, Optional.ofNullable(selectedTutorialClass),
                Optional.ofNullable(selectedDeliverable));
        model.updateFilteredStudentList(predicate);

        String tutorialDescription = tutorialClass
                .map(value -> ", tutorial " + value)
                .orElse("");
        String deliverableDescription = incompleteDeliverable
                .map(value -> ", incomplete " + value)
                .orElse("");
        int matchingStudentCount = model.getFilteredStudentList().size();
        if (matchingStudentCount == 0) {
            return new CommandResult(String.format(MESSAGE_NO_MATCHES,
                    selectedCourseCode, selectedSemester,
                    tutorialDescription + deliverableDescription));
        }
        return new CommandResult(String.format(MESSAGE_FILTER_SUCCESS, matchingStudentCount,
                selectedCourseCode, selectedSemester,
                tutorialDescription + deliverableDescription));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ListCommand otherCommand
                && courseCode.equals(otherCommand.courseCode)
                && semester.equals(otherCommand.semester)
                && tutorialClass.equals(otherCommand.tutorialClass)
                && incompleteDeliverable.equals(otherCommand.incompleteDeliverable));
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode, semester, tutorialClass, incompleteDeliverable);
    }
}
