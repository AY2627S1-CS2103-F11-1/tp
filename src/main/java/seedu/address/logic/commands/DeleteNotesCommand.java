package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Id;
import seedu.address.model.student.Notes;
import seedu.address.model.student.Student;

/** Deletes notes recorded for a student. */
public class DeleteNotesCommand extends Command {

    public static final String COMMAND_WORD = "deletenotes";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes all notes for the student identified by their student ID.\n"
            + "Parameters: " + PREFIX_ID + " ID\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_ID + " A1234567Z";
    public static final String MESSAGE_DELETE_NOTES_SUCCESS = "Deleted notes for student %s (%s).";
    public static final String MESSAGE_NO_NOTES = "This student has no notes to delete.";

    private final Id studentId;

    /** Creates a command that deletes a student's notes. */
    public DeleteNotesCommand(Id studentId) {
        this.studentId = requireNonNull(studentId);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Student studentToEdit = StudentNotesCommandUtil.findStudentById(model, studentId);
        if (studentToEdit.getNotes().isEmpty()) {
            throw new CommandException(MESSAGE_NO_NOTES);
        }

        Student editedStudent = new Student(
                studentToEdit.getName(), studentToEdit.getId(), studentToEdit.getEmail(),
                studentToEdit.getTutorialClass(), new Notes());
        model.setStudent(studentToEdit, editedStudent);

        return new CommandResult(String.format(MESSAGE_DELETE_NOTES_SUCCESS,
                editedStudent.getName(), editedStudent.getId()), false, false, editedStudent.getId());
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof DeleteNotesCommand otherCommand
                && studentId.equals(otherCommand.studentId));
    }
}
