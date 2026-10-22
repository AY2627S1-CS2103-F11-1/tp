package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_ID;

import java.util.Optional;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditStudentDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.Id;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {
    public static final String MESSAGE_UNEXPECTED_TEXT = "Unexpected text found before parameter.";
    public static final String MESSAGE_UNKNOWN_PARAMETER = String.format(
            "Unknown parameter. Parameters allowed: %s, %s, %s, %s",
            PREFIX_ID, PREFIX_NAME, PREFIX_NEW_ID, PREFIX_EMAIL);
    public static final String MESSAGE_MISSING_ID = String.format("Missing required parameter: %s", PREFIX_ID);
    public static final String MESSAGE_EMPTY_PARAMETERS = "Empty parameter value(s): %s";
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("(^|\\s)/(\\S+)");

    /**
     * Parses the given {@code String} of arguments in the context of the
     * EditCommand
     * and returns an EditCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected
     *                        format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        rejectUnknownParameters(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args,
                PREFIX_ID,
                PREFIX_NAME,
                PREFIX_NEW_ID,
                PREFIX_EMAIL);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_UNEXPECTED_TEXT);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ID, PREFIX_NAME, PREFIX_NEW_ID, PREFIX_EMAIL);

        validateParameters(argMultimap);

        Id currentId = ParserUtil.parseId(
                argMultimap.getValue(PREFIX_ID).orElseThrow());

        EditStudentDescriptor descriptor = new EditStudentDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            descriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).orElseThrow()));
        }
        if (argMultimap.getValue(PREFIX_NEW_ID).isPresent()) {
            descriptor.setId(ParserUtil.parseId(argMultimap.getValue(PREFIX_NEW_ID).orElseThrow()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            descriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).orElseThrow()));
        }

        if (!descriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(currentId, descriptor);
    }

    private void validateParameters(ArgumentMultimap argMultimap) throws ParseException {
        StringJoiner emptyParameters = new StringJoiner(", ");

        if (argMultimap.getValue(PREFIX_ID).isEmpty()) {
            throw new ParseException(MESSAGE_MISSING_ID);
        }

        for (Prefix prefix : new Prefix[] { PREFIX_ID, PREFIX_NAME, PREFIX_NEW_ID, PREFIX_EMAIL }) {
            Optional<String> value = argMultimap.getValue(prefix);
            if (value.isPresent() && value.get().isBlank()) {
                emptyParameters.add(prefix.toString());
            }
        }
        if (emptyParameters.length() > 0) {
            throw new ParseException(String.format(
                    MESSAGE_EMPTY_PARAMETERS, emptyParameters));
        }
    }

    /**
     * Rejects parameters that are not supported by the edit command.
     *
     * @throws ParseException if an unknown parameter is found.
     */
    private void rejectUnknownParameters(String args) throws ParseException {
        Matcher matcher = PARAMETER_PATTERN.matcher(args);

        while (matcher.find()) {
            String parameter = "/" + matcher.group(2);

            if (!parameter.equals(PREFIX_ID.toString())
                    && !parameter.equals(PREFIX_NEW_ID.toString())
                    && !parameter.equals(PREFIX_NAME.toString())
                    && !parameter.equals(PREFIX_EMAIL.toString())) {
                throw new ParseException(MESSAGE_UNKNOWN_PARAMETER);
            }
        }
    }
}
