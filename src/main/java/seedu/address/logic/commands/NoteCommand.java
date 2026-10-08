package seedu.address.logic.commands;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.student.Notes;

import java.util.List;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

/**
 * Changes the remark of an existing person in the address book.
 */
public class NoteCommand extends Command {

    public static final String COMMAND_WORD = "note";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds notes to the person identified "
            + "by the index number used in the last person listing. "
            + "Existing notes will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "notes/ [NOTES]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + "notes/ Likes to swim.";

    public static final String MESSAGE_ARGUMENTS = "Index: %1$d, Notes: %2$s";
    public static final String MESSAGE_ADD_REMARK_SUCCESS = "Added notes to Person: %1$s";
    public static final String MESSAGE_DELETE_REMARK_SUCCESS = "Removed notes from Person: %1$s";

    private final Index index;
    private final Notes notes;

    public static final String MESSAGE_NOT_IMPLEMENTED_YET =
            "Notes command not implemented yet";

    /**
     * @param index of the person in the filtered person list to edit the remark
     * @param notes of the person to be updated to
     */
    public NoteCommand(Index index, Notes notes) {
        requireAllNonNull(index, notes);

        this.index = index;
        this.notes = notes;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Student> lastShownList = model.getFilteredStudentList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Student studentToEdit = lastShownList.get(index.getZeroBased());
        Student editedStudent = new Student(
                studentToEdit.getName(), studentToEdit.getId(), studentToEdit.getEmail(),
                studentToEdit.getTutorialClass(), notes);

        model.setStudent(studentToEdit, editedStudent);
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);

        return new CommandResult(generateSuccessMessage(editedStudent));
    }

    /**
     * Generates a command execution success message based on whether
     * the notes is added to or removed from
     * {@code personToEdit}.
     */
    private String generateSuccessMessage(Student personToEdit) {
        String message = !notes.notes.isEmpty() ? MESSAGE_ADD_REMARK_SUCCESS : MESSAGE_DELETE_REMARK_SUCCESS;
        return String.format(message, Messages.format(personToEdit));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NoteCommand)) {
            return false;
        }

        NoteCommand e = (NoteCommand) other;
        return index.equals(e.index)
                && notes.equals(e.notes);
    }
}
