package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.ViewProgressCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses arguments for {@code ViewProgressCommand}. */
public class ViewProgressCommandParser implements Parser<ViewProgressCommand> {

    public static final String MESSAGE_MISSING_STUDENT = "Missing required parameter: " + PREFIX_ID + " or "
            + PREFIX_NAME;
    public static final String MESSAGE_EMPTY_PARAMETER = "Parameters cannot be empty.";
    public static final String MESSAGE_BOTH_IDENTIFIERS = "Please specify either /id or /name, not both.";
    public static final String MESSAGE_UNEXPECTED_TEXT = "Unexpected text found before parameter.";
    public static final String MESSAGE_UNKNOWN_PARAMETER =
            "Unknown parameter. Parameters allowed: " + PREFIX_ID + ", " + PREFIX_NAME;

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("(^|\\s)/(\\S+)");

    @Override
    public ViewProgressCommand parse(String args) throws ParseException {
        rejectUnknownParameters(args);

        ArgumentMultimap arguments = ArgumentTokenizer.tokenize(args, PREFIX_ID, PREFIX_NAME);
        boolean hasId = arguments.getValue(PREFIX_ID).isPresent();
        boolean hasName = arguments.getValue(PREFIX_NAME).isPresent();

        if (!hasId && !hasName) {
            throw new ParseException(MESSAGE_MISSING_STUDENT);
        }
        if (!arguments.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_UNEXPECTED_TEXT);
        }
        arguments.verifyNoDuplicatePrefixesFor(PREFIX_ID, PREFIX_NAME);
        if (hasId && hasName) {
            throw new ParseException(MESSAGE_BOTH_IDENTIFIERS);
        }

        if (hasId) {
            String id = arguments.getValue(PREFIX_ID).orElseThrow();
            if (id.isBlank()) {
                throw new ParseException(MESSAGE_EMPTY_PARAMETER);
            }
            return new ViewProgressCommand(ParserUtil.parseId(id));
        }

        String name = arguments.getValue(PREFIX_NAME).orElseThrow();
        if (name.isBlank()) {
            throw new ParseException(MESSAGE_EMPTY_PARAMETER);
        }
        return new ViewProgressCommand(name);
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

}
