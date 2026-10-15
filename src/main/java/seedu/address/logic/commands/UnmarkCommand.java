package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.progress.CompletionStatus;
import seedu.address.model.student.Id;

/** Marks one or more student deliverables as incomplete. */
public class UnmarkCommand extends StudentProgressCommand {

    public static final String COMMAND_WORD = "unmark";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks student deliverables as incomplete.\n"
            + "Parameters: " + PREFIX_ID + " ID or " + PREFIX_NAME + " NAME " + PREFIX_COURSE + " COURSE "
            + PREFIX_SEMESTER + " SEMESTER " + PREFIX_TASK + " TASK[, TASK]...\n"
            + "Example: " + COMMAND_WORD
            + " " + PREFIX_ID + " A1234567Z " + PREFIX_COURSE + " CS2103T "
            + PREFIX_SEMESTER + " AY26/27-S1 " + PREFIX_TASK + " MVP";
    public static final String MESSAGE_SUCCESS = "Marked %s as incomplete for %s.";
    public static final String MESSAGE_ALREADY_INCOMPLETE = "%s is already marked as incomplete.";

    public UnmarkCommand(Id studentId, CourseCode courseCode, Semester semester, List<Deliverable> deliverables) {
        super(studentId, courseCode, semester, deliverables, CompletionStatus.NOT_DONE);
    }

    public UnmarkCommand(String studentName, CourseCode courseCode, Semester semester,
                         List<Deliverable> deliverables) {
        super(studentName, courseCode, semester, deliverables, CompletionStatus.NOT_DONE);
    }

    @Override
    String getSuccessMessage() {
        return MESSAGE_SUCCESS;
    }

    @Override
    String getAlreadyInStatusMessage() {
        return MESSAGE_ALREADY_INCOMPLETE;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }
}
