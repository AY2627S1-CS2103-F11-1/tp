
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import seedu.address.logic.Messages;
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

    @Override
    public EnrolCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ParserUtil.rejectUnknownParameters(
                args, PREFIX_ID, PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_ID, PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(Messages.MESSAGE_UNEXPECTED_TEXT);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_ID, PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS);

        ParserUtil.validateRequiredParameters(
                argMultimap, PREFIX_ID, PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS);

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

}
