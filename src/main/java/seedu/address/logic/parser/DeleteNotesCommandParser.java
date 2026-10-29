//@@author mizzfain
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.DeleteNotesCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.student.Id;

/**
 * Parses input arguments and creates a DeleteNotesCommand.
 */
public class DeleteNotesCommandParser implements Parser<DeleteNotesCommand> {


    @Override
    public DeleteNotesCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ParserUtil.rejectUnknownParameters(
                args, PREFIX_ID, PREFIX_COURSE, PREFIX_SEMESTER);

        ArgumentMultimap arguments = ArgumentTokenizer.tokenize(
                args, PREFIX_ID, PREFIX_COURSE, PREFIX_SEMESTER);

        if (!arguments.getPreamble().isEmpty()) {
            throw new ParseException(Messages.MESSAGE_UNEXPECTED_TEXT);
        }

        arguments.verifyNoDuplicatePrefixesFor(
                PREFIX_ID, PREFIX_COURSE, PREFIX_SEMESTER);

        ParserUtil.validateRequiredParameters(
                arguments, PREFIX_ID, PREFIX_COURSE, PREFIX_SEMESTER);

        Id studentId = ParserUtil.parseId(
                arguments.getValue(PREFIX_ID).orElseThrow());

        CourseCode courseCode = ParserUtil.parseCourseCode(
                arguments.getValue(PREFIX_COURSE).orElseThrow());

        Semester semester = ParserUtil.parseSemester(
                arguments.getValue(PREFIX_SEMESTER).orElseThrow());

        return new DeleteNotesCommand(studentId, courseCode, semester);
    }
}
