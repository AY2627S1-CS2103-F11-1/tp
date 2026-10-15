package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.regex.Pattern;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses arguments for the {@code list} command. */
public class ListCommandParser implements Parser<ListCommand> {

    public static final String MESSAGE_UNEXPECTED_TEXT = "Unexpected text found after list command.";
    public static final String MESSAGE_UNKNOWN_PARAMETER =
            "Unknown parameter. List command does not accept parameters.";
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("(^|\\s)/\\S+");

    /**
     * Parses list command arguments.
     *
     * @param args arguments supplied after the command word
     * @return a list command
     * @throws ParseException if arguments contain text or parameters
     */
    @Override
    public ListCommand parse(String args) throws ParseException {
        requireNonNull(args);

        if (PARAMETER_PATTERN.matcher(args).find()) {
            throw new ParseException(MESSAGE_UNKNOWN_PARAMETER);
        }

        if (!args.trim().isEmpty()) {
            throw new ParseException(MESSAGE_UNEXPECTED_TEXT);
        }

        return new ListCommand();
    }
}
