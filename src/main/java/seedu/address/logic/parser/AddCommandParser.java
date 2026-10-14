package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.Optional;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.Email;
import seedu.address.model.student.Id;
import seedu.address.model.student.Name;
import seedu.address.model.student.Student;
import seedu.address.model.student.TutorialClass;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    public static final String MESSAGE_UNEXPECTED_TEXT = "Unexpected text found before parameter.";
    public static final String MESSAGE_UNKNOWN_PARAMETER =
            "Unknown parameter. Parameters allowed: /name, /id, /email, /class";
    public static final String MESSAGE_MISSING_PARAMETERS = "Missing required parameter(s): %s";
    public static final String MESSAGE_EMPTY_PARAMETERS = "Empty parameter value(s): %s";

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("(^|\\s)/(\\S+)");

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected
     *                        format
     */
    public AddCommand parse(String args) throws ParseException {
        requireNonNull(args);
        rejectUnknownParameters(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ID, PREFIX_EMAIL,
                PREFIX_CLASS);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_UNEXPECTED_TEXT);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_ID, PREFIX_EMAIL, PREFIX_CLASS);
        validateRequiredParameters(argMultimap);

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).orElseThrow());

        Id id = ParserUtil.parseId(argMultimap.getValue(PREFIX_ID).orElseThrow());

        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).orElseThrow());

        TutorialClass tutorialClass = ParserUtil.parseTutorialClass(
                argMultimap.getValue(PREFIX_CLASS).orElseThrow());

        Student student = new Student(name, id, email, tutorialClass);

        return new AddCommand(student);
    }

    /** Validates that every required parameter is present and non-empty. */
    private void validateRequiredParameters(ArgumentMultimap argMultimap) throws ParseException {
        StringJoiner missingParameters = new StringJoiner(", ");
        StringJoiner emptyParameters = new StringJoiner(", ");
        for (Prefix prefix : new Prefix[] {
            PREFIX_NAME, PREFIX_ID, PREFIX_EMAIL, PREFIX_CLASS }) {
            Optional<String> value = argMultimap.getValue(prefix);

            if (value.isEmpty()) {
                missingParameters.add(prefix.toString());
            } else if (value.get().isBlank() && !prefix.equals(PREFIX_CLASS)) {
                emptyParameters.add(prefix.toString());
            }

        }

        StringJoiner errorMessages = new StringJoiner("\n");

        if (missingParameters.length() > 0) {
            throw new ParseException(String.format(
                    MESSAGE_MISSING_PARAMETERS, missingParameters));
        }

        if (emptyParameters.length() > 0) {
            errorMessages.add(String.format(
                    MESSAGE_EMPTY_PARAMETERS, emptyParameters));
        }

        if (errorMessages.length() > 0) {
            throw new ParseException(errorMessages.toString());
        }

    }

    /** Rejects parameters that are not supported by the add command. */
    private void rejectUnknownParameters(String args) throws ParseException {
        Matcher matcher = PARAMETER_PATTERN.matcher(args);
        while (matcher.find()) {
            String parameter = matcher.group(2);
            if (!parameter.startsWith("name") && !parameter.startsWith("id")
                    && !parameter.startsWith("email") && !parameter.startsWith("class")) {
                throw new ParseException(MESSAGE_UNKNOWN_PARAMETER);
            }
        }
    }

}
