package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import seedu.address.logic.commands.CourseProgressOverviewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;

/** Parses arguments for the {@code overview} command. */
public class CourseProgressOverviewCommandParser implements Parser<CourseProgressOverviewCommand> {

    public static final String MESSAGE_UNEXPECTED_TEXT = "Unexpected text found after overview command.";

    @Override
    public CourseProgressOverviewCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ParserUtil.rejectUnknownParameters(args, PREFIX_COURSE, PREFIX_SEMESTER);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_COURSE, PREFIX_SEMESTER);
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_UNEXPECTED_TEXT);
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_COURSE, PREFIX_SEMESTER);
        ParserUtil.validateRequiredParameters(argMultimap, PREFIX_COURSE, PREFIX_SEMESTER);

        CourseCode courseCode = ParserUtil.parseCourseCode(
                argMultimap.getValue(PREFIX_COURSE).orElseThrow());
        Semester semester = ParserUtil.parseSemester(
                argMultimap.getValue(PREFIX_SEMESTER).orElseThrow());
        return new CourseProgressOverviewCommand(courseCode, semester);
    }
}
