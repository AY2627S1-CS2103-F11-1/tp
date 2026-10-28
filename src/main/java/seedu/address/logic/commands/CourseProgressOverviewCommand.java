package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import java.util.Objects;
import java.util.stream.Collectors;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.progress.CourseProgressOverview;

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
        CourseProgressOverview overview = model.getCourseProgressOverview(courseCode, semester)
                .orElseThrow(() -> new CommandException(String.format(
                        MESSAGE_COURSE_NOT_FOUND, courseCode, semester)));

        if (overview.getStudentProgressSummaries().isEmpty()) {
            return new CommandResult(String.format(MESSAGE_NO_ENROLMENTS, courseCode, semester));
        }

        String rows = overview.getStudentProgressSummaries().stream()
                .map(summary -> formatStudentProgress(summary, overview.getTotalDeliverables()))
                .collect(Collectors.joining("\n"));
        return new CommandResult(String.format(MESSAGE_HEADER, courseCode, semester, rows));
    }

    private String formatStudentProgress(CourseProgressOverview.StudentProgressSummary summary,
            int totalDeliverables) {
        return String.format("%s (%s): %d/%d completed", summary.getStudent().getName(),
                summary.getStudent().getId(), summary.getCompletedDeliverables(), totalDeliverables);
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
