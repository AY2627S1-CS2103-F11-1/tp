package seedu.address.logic.commands;

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
            + "Parameters: /id ID or /name NAME /course COURSE /sem SEMESTER /task TASK[, TASK]...\n"
            + "Example: " + COMMAND_WORD
            + " /id A1234567Z /course CS2103T /sem AY26/27-S1 /task MVP";
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
