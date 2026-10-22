package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import seedu.address.logic.commands.ViewProgressCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;

/** Parses arguments for {@code ViewProgressCommand}. */
public class ViewProgressCommandParser implements Parser<ViewProgressCommand> {

    public static final String MESSAGE_MISSING_STUDENT = "Missing required parameter: " + PREFIX_ID + " or "
            + PREFIX_NAME;
    public static final String MESSAGE_MISSING_COURSE = "Missing required parameter: " + PREFIX_COURSE;
    public static final String MESSAGE_MISSING_SEMESTER = "Missing required parameter: " + PREFIX_SEMESTER;
    public static final String MESSAGE_EMPTY_PARAMETER = "Parameters cannot be empty.";
    public static final String MESSAGE_BOTH_IDENTIFIERS = "Please specify either /id or /name, not both.";
    public static final String MESSAGE_UNEXPECTED_TEXT = "Unexpected text found before parameter.";
    public static final String MESSAGE_UNKNOWN_PARAMETER = "Unknown parameter. Parameters allowed: " + PREFIX_ID + ", "
            + PREFIX_NAME + ", " + PREFIX_COURSE
            + ", " + PREFIX_SEMESTER;

    @Override
    public ViewProgressCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ParserUtil.rejectUnknownParameters(
                args, PREFIX_ID, PREFIX_NAME, PREFIX_COURSE, PREFIX_SEMESTER);

        ArgumentMultimap arguments = ArgumentTokenizer.tokenize(
                args, PREFIX_ID, PREFIX_NAME, PREFIX_COURSE, PREFIX_SEMESTER);

        if (!arguments.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_UNEXPECTED_TEXT);
        }

        arguments.verifyNoDuplicatePrefixesFor(
                PREFIX_ID, PREFIX_NAME, PREFIX_COURSE, PREFIX_SEMESTER);

        boolean hasId = arguments.getValue(PREFIX_ID).isPresent();
        boolean hasName = arguments.getValue(PREFIX_NAME).isPresent();

        if (!hasId && !hasName) {
            throw new ParseException(MESSAGE_MISSING_STUDENT);
        }

        if (hasId && hasName) {
            throw new ParseException(MESSAGE_BOTH_IDENTIFIERS);
        }

        ParserUtil.validateRequiredParameters(
                arguments, PREFIX_COURSE, PREFIX_SEMESTER);

        CourseCode courseCode = ParserUtil.parseCourseCode(
                arguments.getValue(PREFIX_COURSE).orElseThrow());

        Semester semester = ParserUtil.parseSemester(
                arguments.getValue(PREFIX_SEMESTER).orElseThrow());

        if (hasId) {
            ParserUtil.validateRequiredParameters(arguments, PREFIX_ID);

            return new ViewProgressCommand(
                    ParserUtil.parseId(arguments.getValue(PREFIX_ID).orElseThrow()),
                    courseCode, semester);
        }

        ParserUtil.validateRequiredParameters(arguments, PREFIX_NAME);

        return new ViewProgressCommand(
                arguments.getValue(PREFIX_NAME).orElseThrow(),
                courseCode, semester);
    }

}
