package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.StudentMatchesQueryPredicate;

/** Searches for students by a name fragment or exact student ID. */
public class SearchCommand extends Command {
    public static final String COMMAND_WORD = "search";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Searches students by name or student ID.\n"
            + "Parameters: " + PREFIX_NAME + " NAME or " + PREFIX_ID + " ID\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " " + PREFIX_NAME + " John Doe\n"
            + "  " + COMMAND_WORD + " " + PREFIX_ID + " A1234567Z";
    public static final String MESSAGE_NO_STUDENTS = "No student records found. Add students before searching.";
    public static final String MESSAGE_SUCCESS = "Found %1$d matching student(s).";

    private final StudentMatchesQueryPredicate predicate;

    public SearchCommand(StudentMatchesQueryPredicate predicate) {
        this.predicate = requireNonNull(predicate);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.getAddressBook().getStudentList().isEmpty()) {
            throw new CommandException(MESSAGE_NO_STUDENTS);
        }
        model.updateFilteredStudentList(predicate);
        return new CommandResult(String.format(MESSAGE_SUCCESS, model.getFilteredStudentList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        return other instanceof SearchCommand otherCommand && predicate.equals(otherCommand.predicate);
    }

    @Override
    public int hashCode() {
        return predicate.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("predicate", predicate).toString();
    }
}
