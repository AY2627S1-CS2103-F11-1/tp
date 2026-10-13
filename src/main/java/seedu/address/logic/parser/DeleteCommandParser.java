package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses arguments for a {@code DeleteCommand}.
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    public static final String MESSAGE_MISSING_IDENTIFIER = "Missing required parameter: /id or /name";
    public static final String MESSAGE_EMPTY_IDENTIFIER = "id or name cannot be empty.";
    public static final String MESSAGE_BOTH_IDENTIFIERS = "Please specify either /id or /name, not both.";
    public static final String MESSAGE_UNEXPECTED_TEXT = "Unexpected text found before parameter.";
    public static final String MESSAGE_UNKNOWN_PARAMETER = "Unknown parameter. Parameters allowed: /id, /name";
    public static final String MESSAGE_DUPLICATE_PARAMETER = "Parameter %s should only be specified once.";

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("(^|\\s)/(\\S+)");

    @Override
    public DeleteCommand parse(String args) throws ParseException {
        rejectUnknownParameters(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ID, PREFIX_NAME);
        boolean hasId = argMultimap.getValue(PREFIX_ID).isPresent();
        boolean hasName = argMultimap.getValue(PREFIX_NAME).isPresent();

        if (!hasId && !hasName) {
            if (args.trim().isEmpty()) {
                throw new ParseException(MESSAGE_MISSING_IDENTIFIER);
            }
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_UNEXPECTED_TEXT);
        }
        rejectDuplicateParameters(argMultimap);
        if (hasId && hasName) {
            throw new ParseException(MESSAGE_BOTH_IDENTIFIERS);
        }

        if (hasId) {
            String id = argMultimap.getValue(PREFIX_ID).orElseThrow();
            if (id.isBlank()) {
                throw new ParseException(MESSAGE_EMPTY_IDENTIFIER);
            }
            return new DeleteCommand(ParserUtil.parseId(id));
        }

        String name = argMultimap.getValue(PREFIX_NAME).orElseThrow();
        if (name.isBlank()) {
            throw new ParseException(MESSAGE_EMPTY_IDENTIFIER);
        }
        return new DeleteCommand(name);
    }

    private void rejectUnknownParameters(String args) throws ParseException {
        Matcher matcher = PARAMETER_PATTERN.matcher(args);
        while (matcher.find()) {
            String parameter = matcher.group(2);
            if (!parameter.equals("id") && !parameter.equals("name")) {
                throw new ParseException(MESSAGE_UNKNOWN_PARAMETER);
            }
        }
    }

    private void rejectDuplicateParameters(ArgumentMultimap argMultimap) throws ParseException {
        if (argMultimap.getAllValues(PREFIX_ID).size() > 1) {
            throw new ParseException(String.format(MESSAGE_DUPLICATE_PARAMETER, PREFIX_ID));
        }
        if (argMultimap.getAllValues(PREFIX_NAME).size() > 1) {
            throw new ParseException(String.format(MESSAGE_DUPLICATE_PARAMETER, PREFIX_NAME));
        }
    }
}
