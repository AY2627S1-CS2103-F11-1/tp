package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.SearchCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.Id;
import seedu.address.model.student.StudentMatchesQueryPredicate;

/** Parses input arguments and creates a {@code SearchCommand}. */
public class SearchCommandParser implements Parser<SearchCommand> {
    public static final String MESSAGE_MISSING_IDENTIFIER = "Missing required parameter: "
            + PREFIX_NAME + " or " + PREFIX_ID;
    public static final String MESSAGE_EMPTY_IDENTIFIER = "Search value cannot be empty.";
    public static final String MESSAGE_BOTH_IDENTIFIERS = "Please search using either "
            + PREFIX_NAME + " or " + PREFIX_ID + ", not both.";
    public static final String MESSAGE_UNEXPECTED_TEXT = "Unexpected text found before parameter.";
    public static final String MESSAGE_UNKNOWN_PARAMETER = "Unknown parameter. Parameters allowed: " + PREFIX_NAME
            + ", " + PREFIX_ID;

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("(^|\\s)(/\\S+)");

    @Override
    public SearchCommand parse(String args) throws ParseException {
        rejectUnknownParameters(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ID);
        boolean hasName = argMultimap.getValue(PREFIX_NAME).isPresent();
        boolean hasId = argMultimap.getValue(PREFIX_ID).isPresent();

        if (!hasName && !hasId) {
            if (args.trim().isEmpty()) {
                throw new ParseException(MESSAGE_MISSING_IDENTIFIER);
            }
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
        }
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_UNEXPECTED_TEXT);
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_ID);
        if (hasName && hasId) {
            throw new ParseException(MESSAGE_BOTH_IDENTIFIERS);
        }

        if (hasName) {
            String name = argMultimap.getValue(PREFIX_NAME).orElseThrow();
            if (name.isBlank()) {
                throw new ParseException(MESSAGE_EMPTY_IDENTIFIER);
            }
            return new SearchCommand(new StudentMatchesQueryPredicate(name));
        }

        String id = argMultimap.getValue(PREFIX_ID).orElseThrow();
        if (id.isBlank()) {
            throw new ParseException(MESSAGE_EMPTY_IDENTIFIER);
        }
        Id parsedId = ParserUtil.parseId(id.toUpperCase(Locale.ROOT));
        return new SearchCommand(new StudentMatchesQueryPredicate(parsedId));
    }

    private void rejectUnknownParameters(String args) throws ParseException {
        Matcher matcher = PARAMETER_PATTERN.matcher(args);
        while (matcher.find()) {
            String parameter = matcher.group(2);
            if (!parameter.equals(PREFIX_NAME.toString()) && !parameter.equals(PREFIX_ID.toString())) {
                throw new ParseException(MESSAGE_UNKNOWN_PARAMETER);
            }
        }
    }
}
