package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELIVERABLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import seedu.address.logic.commands.InitCourseCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;

/**
 * Parses input arguments and creates an {@link InitCourseCommand}.
 */
public class InitCourseCommandParser implements Parser<InitCourseCommand> {

    private static final String COURSE_SUBCOMMAND = "course";

    @Override
    public InitCourseCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmedArgs = args.trim();
        if (!trimmedArgs.equals(COURSE_SUBCOMMAND)
                && !trimmedArgs.startsWith(COURSE_SUBCOMMAND + " ")) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, InitCourseCommand.MESSAGE_USAGE));
        }

        String arguments = trimmedArgs.substring(COURSE_SUBCOMMAND.length());

        ParserUtil.rejectUnknownParameters(
                arguments, PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_DELIVERABLE);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                arguments, PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_DELIVERABLE);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, InitCourseCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_DELIVERABLE);

        ParserUtil.validateRequiredParameters(
                argMultimap, PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_DELIVERABLE);

        CourseCode courseCode = ParserUtil.parseCourseCode(
                argMultimap.getValue(PREFIX_COURSE).orElseThrow());

        Semester semester = ParserUtil.parseSemester(
                argMultimap.getValue(PREFIX_SEMESTER).orElseThrow());

        Course course = new Course(
                courseCode,
                semester,
                ParserUtil.parseDeliverables(
                        argMultimap.getValue(PREFIX_DELIVERABLE).orElseThrow()));

        return new InitCourseCommand(course);
    }
}
