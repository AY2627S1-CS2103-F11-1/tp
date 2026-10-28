package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.progress.CompletionStatus;
import seedu.address.model.progress.StudentCourseProgress;
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;

/** Displays completed and total deliverable counts for students in one course-semester. */
public class CourseProgressOverviewCommand extends Command {

    public static final String COMMAND_WORD = "overview";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Displays a progress summary for every student enrolled in a course-semester.\n"
            + "Parameters: " + PREFIX_COURSE + " COURSE " + PREFIX_SEMESTER + " SEMESTER\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_COURSE + " CS2103T "
            + PREFIX_SEMESTER + " AY26/27-S1";
    public static final String MESSAGE_COURSE_NOT_FOUND =
            "Course %s for semester %s has not been initialised.";
    public static final String MESSAGE_NO_ENROLMENTS =
            "No students are enrolled in %s (%s).";
    public static final String MESSAGE_HEADER = "Progress overview for %s (%s):\n%s";

    private final CourseCode courseCode;
    private final Semester semester;

    /** Creates a course progress overview command. */
    public CourseProgressOverviewCommand(CourseCode courseCode, Semester semester) {
        this.courseCode = requireNonNull(courseCode);
        this.semester = requireNonNull(semester);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Course course = model.getCourse(courseCode, semester)
                .orElseThrow(() -> new CommandException(String.format(
                        MESSAGE_COURSE_NOT_FOUND, courseCode, semester)));

        List<Student> enrolledStudents = model.getStudentsEnrolledIn(courseCode, semester);
        if (enrolledStudents.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_NO_ENROLMENTS, courseCode, semester));
        }

        Map<Id, StudentCourseProgress> progressByStudentId = model.getStudentCourseProgressList().stream()
                .filter(progress -> progress.getCourseCode().equals(courseCode)
                        && progress.getSemester().equals(semester))
                .collect(Collectors.toMap(StudentCourseProgress::getStudentId, Function.identity()));
        String rows = enrolledStudents.stream()
                .map(student -> formatStudentProgress(student, course, progressByStudentId))
                .collect(Collectors.joining("\n"));
        return new CommandResult(String.format(MESSAGE_HEADER, courseCode, semester, rows));
    }

    private String formatStudentProgress(Student student, Course course,
            Map<Id, StudentCourseProgress> progressByStudentId) {
        int completed = java.util.Optional.ofNullable(progressByStudentId.get(student.getId()))
                .map(this::countCompletedDeliverables)
                .orElse(0);
        return String.format("%s (%s): %d/%d completed", student.getName(), student.getId(),
                completed, course.getDeliverables().size());
    }

    private int countCompletedDeliverables(StudentCourseProgress progress) {
        return (int) progress.getDeliverableStatuses().values().stream()
                .filter(status -> status == CompletionStatus.DONE)
                .count();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof CourseProgressOverviewCommand otherCommand
                && courseCode.equals(otherCommand.courseCode)
                && semester.equals(otherCommand.semester));
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode, semester);
    }
}
