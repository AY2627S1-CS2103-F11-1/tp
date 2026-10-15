package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Lists all students in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Displayed %1$d student(s).";

    public static final String MESSAGE_NO_STUDENTS = "No student records found. Add a student to get started.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        int studentCount = model.getFilteredStudentList().size();
        if (studentCount == 0) {
            throw new CommandException(MESSAGE_NO_STUDENTS);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, studentCount));
    }
}
