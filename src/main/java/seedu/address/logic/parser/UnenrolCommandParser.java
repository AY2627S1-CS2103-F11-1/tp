
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import seedu.address.logic.commands.UnenrolCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.student.Id;

/**
 * Parses input arguments and creates an {@code UnenrolCommand}.
 */
public class UnenrolCommandParser implements Parser<UnenrolCommand> {

    public static final String MESSAGE_UNEXPECTED_TEXT = "Unexpected text found before parameter.";

    public static final String MESSAGE_UNKNOWN_PARAMETER = String.format(
            "Unknown parameter. Parameters allowed: %s, %s, %s, %s",
            PREFIX_ID, PREFIX_COURSE, PREFIX_SEMESTER);

    public static final String MESSAGE_MISSING_PARAMETERS = "Missing required parameter(s): %s";

    public static final String MESSAGE_EMPTY_PARAMETERS = "Empty parameter value(s): %s";

    @Override
    public UnenrolCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ParserUtil.rejectUnknownParameters(
                args,
                PREFIX_ID, PREFIX_COURSE, PREFIX_SEMESTER);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_ID, PREFIX_COURSE, PREFIX_SEMESTER);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_UNEXPECTED_TEXT);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_ID, PREFIX_COURSE, PREFIX_SEMESTER);

        ParserUtil.validateRequiredParameters(
                argMultimap, PREFIX_ID, PREFIX_COURSE, PREFIX_SEMESTER);

        Id studentId = ParserUtil.parseId(
                argMultimap.getValue(PREFIX_ID).orElseThrow());

        CourseCode courseCode = ParserUtil.parseCourseCode(
                argMultimap.getValue(PREFIX_COURSE).orElseThrow());

        Semester semester = ParserUtil.parseSemester(
                argMultimap.getValue(PREFIX_SEMESTER).orElseThrow());

        return new UnenrolCommand(studentId, courseCode, semester);
    }

}
