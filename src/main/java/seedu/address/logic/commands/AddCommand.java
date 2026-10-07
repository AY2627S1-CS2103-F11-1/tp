package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;

/**
 * Adds a student to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a student to TAssist. "
            + "Parameters: "
            + PREFIX_NAME + " NAME "
            + PREFIX_ID + " ID "
            + PREFIX_EMAIL + " EMAIL "
            + PREFIX_CLASS + " CLASS "
            + PREFIX_NOTES + " NOTES "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + " John Doe "
            + PREFIX_ID + " A1234567Y "
            + PREFIX_EMAIL + " johnd@u.nus.edu "
            + PREFIX_CLASS + " T07 "
            + PREFIX_NOTES + " He is on track with his deliverables.";

    public static final String MESSAGE_SUCCESS = "New student added: %1$s";
    public static final String MESSAGE_DUPLICATE_STUDENT = "This student already exists in TAssist.";

    private final Student student;

    /**
     * Creates an AddCommand to add the specified {@code Student}
     */
    public AddCommand(Student student) {
        requireNonNull(student);
        this.student = student;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasStudent(student)) {
            throw new CommandException(MESSAGE_DUPLICATE_STUDENT);
        }

        model.addStudent(student);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(student)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand otherAddCommand)) {
            return false;
        }

        return student.equals(otherAddCommand.student);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", student)
                .toString();
    }
}
