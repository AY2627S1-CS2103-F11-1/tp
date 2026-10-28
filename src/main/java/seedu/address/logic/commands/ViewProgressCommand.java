package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
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
            + "Parameters: " + PREFIX_ID + " ID or " + PREFIX_NAME + " NAME\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " " + PREFIX_ID + " A1234567Z\n"
            + "  " + COMMAND_WORD + " " + PREFIX_NAME + " John Doe";
    public static final String MESSAGE_NO_PROGRESS = "No deliverable progress found for %s (%s).";

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

        Student student = findStudent(model);
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

    private Student findStudent(Model model) throws CommandException {
        if (studentId.isPresent()) {
            return model.getAddressBook().getStudentList().stream()
                    .filter(student -> student.getId().equals(studentId.get()))
                    .findFirst()
                    .orElseThrow(() -> new CommandException(String.format(
                            StudentProgressCommand.MESSAGE_STUDENT_ID_NOT_FOUND, studentId.get())));
        }

        List<Student> matchingStudents = model.getAddressBook().getStudentList().stream()
                .filter(student -> student.getName().fullName.equalsIgnoreCase(studentName.orElseThrow()))
                .toList();
        if (matchingStudents.isEmpty()) {
            throw new CommandException(String.format(StudentProgressCommand.MESSAGE_STUDENT_NAME_NOT_FOUND,
                    studentName.orElseThrow()));
        }
        if (matchingStudents.size() > 1) {
            throw new CommandException(StudentProgressCommand.MESSAGE_MULTIPLE_STUDENTS_WITH_NAME);
        }
        return matchingStudents.getFirst();
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
