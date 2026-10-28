package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.ARGUMENT_COURSE;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_DELIVERABLE;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_ID;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_NAME;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_COURSE;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_DELIVERABLE;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_ID;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELIVERABLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.progress.CompletionStatus;
import seedu.address.model.student.Id;

/** Marks one or more student deliverables as complete. */
public class MarkCommand extends StudentProgressCommand {

    public static final String COMMAND_WORD = "mark";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks student deliverables as complete.\n"
            + "Parameters: " + PREFIX_ID + " " + ARGUMENT_ID + " or " + PREFIX_NAME + " " + ARGUMENT_NAME + " "
            + PREFIX_COURSE + " " + ARGUMENT_COURSE + " " + PREFIX_SEMESTER + " " + ARGUMENT_SEMESTER + " "
            + PREFIX_DELIVERABLE + " " + ARGUMENT_DELIVERABLE + "[, " + ARGUMENT_DELIVERABLE + "]...\n"
            + "Example: " + COMMAND_WORD
            + " " + PREFIX_ID + " " + EXAMPLE_ID + " " + PREFIX_COURSE + " " + EXAMPLE_COURSE + " "
            + PREFIX_SEMESTER + " " + EXAMPLE_SEMESTER + " " + PREFIX_DELIVERABLE + " " + EXAMPLE_DELIVERABLE;
    public static final String MESSAGE_SUCCESS = "Marked %s as completed for %s.";
    public static final String MESSAGE_ALREADY_COMPLETE = "%s is already marked as complete.";

    public MarkCommand(Id studentId, CourseCode courseCode, Semester semester, List<Deliverable> deliverables) {
        super(studentId, courseCode, semester, deliverables, CompletionStatus.DONE);
    }

    public MarkCommand(String studentName, CourseCode courseCode, Semester semester, List<Deliverable> deliverables) {
        super(studentName, courseCode, semester, deliverables, CompletionStatus.DONE);
    }

    @Override
    String getSuccessMessage() {
        return MESSAGE_SUCCESS;
    }

    @Override
    String getAlreadyInStatusMessage() {
        return MESSAGE_ALREADY_COMPLETE;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }
}
