package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.progress.CompletionStatus;
import seedu.address.model.progress.StudentCourseProgress;
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;

/**
 * Displays a student's deliverable completion status for one course-semester.
 */
public class ViewProgressCommand extends Command {

    public static final String COMMAND_WORD = "view";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Displays a student's deliverable progress for a "
            + "course-semester.\n"
            + "Parameters: /id ID or /name NAME /course COURSE /sem SEMESTER\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " /id A1234567Z /course CS2103T /sem AY26/27-S1\n"
            + "  " + COMMAND_WORD + " /name John Doe /course CS2103T /sem AY26/27-S1";

    private final Optional<Id> studentId;
    private final Optional<String> studentName;
    private final CourseCode courseCode;
    private final Semester semester;

    /**
     * Creates a command that identifies a student by ID.
     */
    public ViewProgressCommand(Id studentId, CourseCode courseCode, Semester semester) {
        this.studentId = Optional.of(requireNonNull(studentId));
        this.studentName = Optional.empty();
        this.courseCode = requireNonNull(courseCode);
        this.semester = requireNonNull(semester);
    }

    /**
     * Creates a command that identifies a student by name.
     */
    public ViewProgressCommand(String studentName, CourseCode courseCode, Semester semester) {
        this.studentId = Optional.empty();
        this.studentName = Optional.of(requireNonNull(studentName));
        this.courseCode = requireNonNull(courseCode);
        this.semester = requireNonNull(semester);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Student student = findStudent(model);
        StudentCourseProgress progress = model.getStudentCourseProgressList().stream()
                .filter(candidate -> candidate.isFor(student.getId(), courseCode, semester))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(
                        StudentProgressCommand.MESSAGE_COURSE_NOT_FOUND, courseCode, semester)));

        return new CommandResult(formatProgress(student, progress));
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

    private String formatProgress(Student student, StudentCourseProgress progress) {
        String statuses = progress.getDeliverableStatuses().entrySet().stream()
                .map(entry -> String.format("[%s] %s", formatStatus(entry.getValue()), entry.getKey()))
                .reduce((first, second) -> first + "\n" + second)
                .orElseThrow();
        return String.format("Progress for %s (%s) in %s %s:\n%s", student.getName(), student.getId(), courseCode,
                semester, statuses);
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
                && studentName.equals(otherCommand.studentName)
                && courseCode.equals(otherCommand.courseCode)
                && semester.equals(otherCommand.semester);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(studentId, studentName, courseCode, semester);
    }
}
