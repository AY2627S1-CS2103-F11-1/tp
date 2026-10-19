package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;

/** Displays notes recorded for a student. */
public class ViewNotesCommand extends Command {

    public static final String COMMAND_WORD = "viewnotes";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Displays notes for the student identified by their student ID.\n"
            + "Parameters: " + PREFIX_ID + " ID\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_ID + " A1234567Z";
    public static final String MESSAGE_VIEW_NOTES_SUCCESS = "Notes for student %s (%s):\n%s";
    public static final String MESSAGE_NO_NOTES = "No notes recorded.";

    private final Id studentId;

    /** Creates a command that displays the selected student's notes. */
    public ViewNotesCommand(Id studentId) {
        this.studentId = requireNonNull(studentId);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Student student = StudentNotesCommandUtil.findStudentById(model, studentId);
        String notes = student.getNotes().isEmpty() ? MESSAGE_NO_NOTES : student.getNotes().toString();

        return new CommandResult(String.format(MESSAGE_VIEW_NOTES_SUCCESS,
                student.getName(), student.getId(), notes), false, false, student.getId());
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof ViewNotesCommand otherCommand
                && studentId.equals(otherCommand.studentId));
    }
}
