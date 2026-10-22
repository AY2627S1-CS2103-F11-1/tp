
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import java.util.Optional;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.EnrolCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.student.Id;
import seedu.address.model.student.TutorialClass;

/**
 * Parses input arguments and creates an {@code EnrolCommand}.
 */
public class EnrolCommandParser implements Parser<EnrolCommand> {

    public static final String MESSAGE_UNEXPECTED_TEXT = "Unexpected text found before parameter.";

    public static final String MESSAGE_UNKNOWN_PARAMETER = String.format(
            "Unknown parameter. Parameters allowed: %s, %s, %s, %s",
            PREFIX_ID, PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS);

    public static final String MESSAGE_MISSING_PARAMETERS = "Missing required parameter(s): %s";

    public static final String MESSAGE_EMPTY_PARAMETERS = "Empty parameter value(s): %s";

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("(^|\\s)/(\\S+)");

    @Override
    public EnrolCommand parse(String args) throws ParseException {
        requireNonNull(args);

        rejectUnknownParameters(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_ID, PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_UNEXPECTED_TEXT);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_ID, PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS);

        validateRequiredParameters(argMultimap);

        Id studentId = ParserUtil.parseId(
                argMultimap.getValue(PREFIX_ID).orElseThrow());

        CourseCode courseCode = ParserUtil.parseCourseCode(
                argMultimap.getValue(PREFIX_COURSE).orElseThrow());

        Semester semester = ParserUtil.parseSemester(
                argMultimap.getValue(PREFIX_SEMESTER).orElseThrow());

        TutorialClass tutorialClass = ParserUtil.parseTutorialClass(
                argMultimap.getValue(PREFIX_CLASS).orElseThrow());

        return new EnrolCommand(studentId, courseCode, semester, tutorialClass);
    }

    /** Validates that every required parameter is present and non-empty. */
    private void validateRequiredParameters(ArgumentMultimap argMultimap) throws ParseException {
        StringJoiner missingParameters = new StringJoiner(", ");
        StringJoiner emptyParameters = new StringJoiner(", ");
        for (Prefix prefix : new Prefix[] {
            PREFIX_ID, PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS }) {
            Optional<String> value = argMultimap.getValue(prefix);

            if (value.isEmpty()) {
                missingParameters.add(prefix.toString());
            } else if (value.get().isBlank()) {
                emptyParameters.add(prefix.toString());
            }

        }

        StringJoiner errorMessages = new StringJoiner("\n");

        if (missingParameters.length() > 0) {
            errorMessages.add(String.format(
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
            String parameter = "/" + matcher.group(2);

            if (!parameter.equals(PREFIX_ID.toString())
                    && !parameter.equals(PREFIX_COURSE.toString())
                    && !parameter.equals(PREFIX_SEMESTER.toString())
                    && !parameter.equals(PREFIX_CLASS.toString())) {
                throw new ParseException(MESSAGE_UNKNOWN_PARAMETER);
            }
        }
    }

}
