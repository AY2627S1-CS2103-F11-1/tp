package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.ViewProgressCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;

/** Parses arguments for {@code ViewProgressCommand}. */
public class ViewProgressCommandParser implements Parser<ViewProgressCommand> {

    public static final String MESSAGE_MISSING_STUDENT = "Missing required parameter: /id or /name";
    public static final String MESSAGE_MISSING_COURSE = "Missing required parameter: /course";
    public static final String MESSAGE_MISSING_SEMESTER = "Missing required parameter: /sem";
    public static final String MESSAGE_EMPTY_PARAMETER = "Parameters cannot be empty.";
    public static final String MESSAGE_BOTH_IDENTIFIERS = "Please specify either /id or /name, not both.";
    public static final String MESSAGE_UNEXPECTED_TEXT = "Unexpected text found before parameter.";
    public static final String MESSAGE_UNKNOWN_PARAMETER =
            "Unknown parameter. Parameters allowed: /id, /name, /course, /sem";
    public static final String MESSAGE_DUPLICATE_PARAMETER = "Parameter %s should only be specified once.";

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("(^|\\s)/(\\S+)");

    @Override
    public ViewProgressCommand parse(String args) throws ParseException {
        rejectUnknownParameters(args);

        ArgumentMultimap arguments = ArgumentTokenizer.tokenize(args, PREFIX_ID, PREFIX_NAME, PREFIX_COURSE,
                PREFIX_SEMESTER);
        boolean hasId = arguments.getValue(PREFIX_ID).isPresent();
        boolean hasName = arguments.getValue(PREFIX_NAME).isPresent();

        if (!hasId && !hasName) {
            throw new ParseException(MESSAGE_MISSING_STUDENT);
        }
        if (!arguments.getValue(PREFIX_COURSE).isPresent()) {
            throw new ParseException(MESSAGE_MISSING_COURSE);
        }
        if (!arguments.getValue(PREFIX_SEMESTER).isPresent()) {
            throw new ParseException(MESSAGE_MISSING_SEMESTER);
        }
        if (!arguments.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_UNEXPECTED_TEXT);
        }
        rejectDuplicateParameters(arguments);
        if (hasId && hasName) {
            throw new ParseException(MESSAGE_BOTH_IDENTIFIERS);
        }

        String course = arguments.getValue(PREFIX_COURSE).orElseThrow();
        String semester = arguments.getValue(PREFIX_SEMESTER).orElseThrow();
        if (course.isBlank() || semester.isBlank()) {
            throw new ParseException(MESSAGE_EMPTY_PARAMETER);
        }

        CourseCode courseCode = ParserUtil.parseCourseCode(course);
        Semester parsedSemester = ParserUtil.parseSemester(semester);
        if (hasId) {
            String id = arguments.getValue(PREFIX_ID).orElseThrow();
            if (id.isBlank()) {
                throw new ParseException(MESSAGE_EMPTY_PARAMETER);
            }
            return new ViewProgressCommand(ParserUtil.parseId(id), courseCode, parsedSemester);
        }

        String name = arguments.getValue(PREFIX_NAME).orElseThrow();
        if (name.isBlank()) {
            throw new ParseException(MESSAGE_EMPTY_PARAMETER);
        }
        return new ViewProgressCommand(name, courseCode, parsedSemester);
    }

    private void rejectUnknownParameters(String args) throws ParseException {
        Matcher matcher = PARAMETER_PATTERN.matcher(args);
        while (matcher.find()) {
            String parameter = matcher.group(2);
            if (!parameter.equals("id") && !parameter.equals("name") && !parameter.equals("course")
                    && !parameter.equals("sem")) {
                throw new ParseException(MESSAGE_UNKNOWN_PARAMETER);
            }
        }
    }

    private void rejectDuplicateParameters(ArgumentMultimap arguments) throws ParseException {
        for (Prefix prefix : List.of(PREFIX_ID, PREFIX_NAME, PREFIX_COURSE, PREFIX_SEMESTER)) {
            if (arguments.getAllValues(prefix).size() > 1) {
                throw new ParseException(String.format(MESSAGE_DUPLICATE_PARAMETER, prefix));
            }
        }
    }
}
