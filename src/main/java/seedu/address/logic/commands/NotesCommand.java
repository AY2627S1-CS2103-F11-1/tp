package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Id;
import seedu.address.model.student.Notes;
import seedu.address.model.student.Student;

/** Adds or replaces the notes for an existing student. */
public class NotesCommand extends Command {

    public static final String COMMAND_WORD = "notes";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds or replaces notes for the student identified by their student ID. "
            + "Existing notes will be overwritten by the input.\n"
            + "Parameters: " + PREFIX_ID + " ID " + PREFIX_NOTES + " NOTES\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_ID + " A1234567Z "
            + PREFIX_NOTES + " Used Task as Parent class of ToDo and Deadline";
    public static final String INVALID_NOTES = "Notes cannot be blank";

    public static final String MESSAGE_UPDATE_NOTES_SUCCESS = "Updated notes for student %s (%s).";

    private final Id studentId;
    private final Notes notes;

    /** Creates a command to set a student's notes. */
    public NotesCommand(Id studentId, Notes notes) {
        requireAllNonNull(studentId, notes);

        this.studentId = studentId;
        this.notes = notes;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Student studentToEdit = StudentNotesCommandUtil.findStudentById(model, studentId);
        StudentCourseEnrolment enrolment = StudentNotesCommandUtil.findEnrolmentByStudentId(model, studentId);
        model.setEnrolment(enrolment, enrolment.withNotes(notes));

        return new CommandResult(String.format(MESSAGE_UPDATE_NOTES_SUCCESS,
                studentToEdit.getName(), studentToEdit.getId()), false, false, studentToEdit.getId());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof NotesCommand otherNotesCommand)) {
            return false;
        }

        return studentId.equals(otherNotesCommand.studentId)
                && notes.equals(otherNotesCommand.notes);
    }
}
