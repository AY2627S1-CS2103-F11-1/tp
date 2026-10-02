package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Greets the user without changing address book data.
 */
public class GreetCommand extends Command {

    public static final String COMMAND_WORD = "greet";
    public static final String MESSAGE_SUCCESS = "Hello! What would you like to do with your address book?";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
