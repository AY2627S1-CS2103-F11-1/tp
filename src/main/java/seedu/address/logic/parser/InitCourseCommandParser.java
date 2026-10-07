package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK;

import java.util.stream.Stream;

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
        String trimmedArgs = args.trim();
        if (!trimmedArgs.equals(COURSE_SUBCOMMAND)
                && !trimmedArgs.startsWith(COURSE_SUBCOMMAND + " ")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, InitCourseCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                trimmedArgs.substring(COURSE_SUBCOMMAND.length()), PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_TASK);
        if (!arePrefixesPresent(argMultimap, PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_TASK)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, InitCourseCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_TASK);
        CourseCode courseCode = ParserUtil.parseCourseCode(argMultimap.getValue(PREFIX_COURSE).get());
        Semester semester = ParserUtil.parseSemester(argMultimap.getValue(PREFIX_SEMESTER).get());
        Course course = new Course(courseCode, semester,
                ParserUtil.parseDeliverables(argMultimap.getValue(PREFIX_TASK).get()));
        return new InitCourseCommand(course);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
