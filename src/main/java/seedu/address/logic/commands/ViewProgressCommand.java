package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_ID;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_NAME;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_ID;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.progress.CompletionStatus;
import seedu.address.model.progress.StudentCourseProgress;
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;

/**
 * Displays a student's deliverable completion status for every tracked
 * course-semester.
 */
public class ViewProgressCommand extends Command {

    public static final String COMMAND_WORD = "view";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Displays all deliverable progress for a student.\n"
            + "Parameters: " + PREFIX_ID + " " + ARGUMENT_ID + " or "
            + PREFIX_NAME + " " + ARGUMENT_NAME + "\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " " + PREFIX_ID + " " + EXAMPLE_ID + "\n"
            + "  " + COMMAND_WORD + " " + PREFIX_NAME + " " + EXAMPLE_NAME;
    private static final String MESSAGE_NO_PROGRESS = "No deliverable progress found for %s (%s).";

    private final Optional<Id> studentId;
    private final Optional<String> studentName;

    /**
     * Creates a command that identifies a student by ID.
     */
    public ViewProgressCommand(Id studentId) {
        this.studentId = Optional.of(requireNonNull(studentId));
        this.studentName = Optional.empty();
    }

    /**
     * Creates a command that identifies a student by name.
     */
    public ViewProgressCommand(String studentName) {
        this.studentId = Optional.empty();
        this.studentName = Optional.of(requireNonNull(studentName));
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Student student = StudentProgressCommand.findStudent(model, studentId, studentName);
        List<StudentCourseProgress> progressRecords = model.getStudentCourseProgressList().stream()
                .filter(candidate -> candidate.getStudentId().equals(student.getId()))
                .toList();

        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        if (progressRecords.isEmpty()) {
            return new CommandResult(formatProgress(student, progressRecords));
        }
        return new CommandResult(formatProgress(student, progressRecords),
                new StudentDeliverablesView(student, progressRecords));
    }

    private String formatProgress(Student student, List<StudentCourseProgress> progressRecords) {
        if (progressRecords.isEmpty()) {
            return String.format(MESSAGE_NO_PROGRESS, student.getName(), student.getId());
        }

        String courseProgress = progressRecords.stream()
                .map(this::formatCourseProgress)
                .collect(Collectors.joining("\n\n"));
        return String.format("Progress for %s (%s):\n%s", student.getName(), student.getId(), courseProgress);
    }

    private String formatCourseProgress(StudentCourseProgress progress) {
        String statuses = progress.getDeliverableStatuses().entrySet().stream()
                .map(entry -> String.format("[%s] %s", formatStatus(entry.getValue()), entry.getKey()))
                .collect(Collectors.joining("\n"));
        return String.format("%s %s:\n%s", progress.getCourseCode(), progress.getSemester(), statuses);
    }

    private String formatStatus(CompletionStatus status) {
        return status == CompletionStatus.DONE ? "Done" : "Not Done";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ViewProgressCommand otherCommand)) {
            return false;
        }
        return studentId.equals(otherCommand.studentId)
                && studentName.equals(otherCommand.studentName);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(studentId, studentName);
    }
}
