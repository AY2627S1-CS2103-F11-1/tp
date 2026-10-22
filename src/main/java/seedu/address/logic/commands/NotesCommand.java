package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Id;
import seedu.address.model.student.Notes;
import seedu.address.model.student.Student;

/** Adds or replaces the notes for an existing student. */
public class NotesCommand extends Command {

    public static final String COMMAND_WORD = "notes";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds notes for a deliverable for the student identified by their student ID.\n"
            + "Parameters: " + PREFIX_ID + " ID " + PREFIX_NOTES + " NOTES "
            + PREFIX_TASK + " TASK\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_ID + " A1234567Z "
            + PREFIX_NOTES + " Used Task as Parent class of ToDo and Deadline "
            + PREFIX_TASK + "Level 4";
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
        Student editedStudent = new Student(
                studentToEdit.getName(), studentToEdit.getId(), studentToEdit.getEmail(),
                studentToEdit.getTutorialClass(), studentToEdit.getNotes().add(notes));

        model.setStudent(studentToEdit, editedStudent);

        return new CommandResult(String.format(MESSAGE_UPDATE_NOTES_SUCCESS,
                editedStudent.getName(), editedStudent.getId()), false, false, editedStudent.getId());
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
