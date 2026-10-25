package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELIVERABLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import java.util.List;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.StudentProgressCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.student.Id;

/**
 * Parses shared arguments for commands that update student deliverable
 * progress.
 */
abstract class StudentProgressCommandParser<T extends StudentProgressCommand> implements Parser<T> {

    @Override
    public T parse(String args) throws ParseException {
        ParserUtil.rejectUnknownParameters(args, PREFIX_ID, PREFIX_NAME, PREFIX_COURSE, PREFIX_SEMESTER,
                PREFIX_DELIVERABLE);

        ArgumentMultimap arguments = ArgumentTokenizer.tokenize(args, PREFIX_ID, PREFIX_NAME, PREFIX_COURSE,
                PREFIX_SEMESTER, PREFIX_DELIVERABLE);
        if (!arguments.getPreamble().isEmpty()) {
            throw new ParseException(Messages.MESSAGE_UNEXPECTED_TEXT);
        }
        ParserUtil.rejectDuplicateParameters(arguments, PREFIX_ID, PREFIX_NAME, PREFIX_COURSE, PREFIX_SEMESTER,
                PREFIX_DELIVERABLE);
        ParserUtil.validateExactlyOneOf(arguments,
                String.format(Messages.MESSAGE_MISSING_IDENTIFIER, PREFIX_ID, PREFIX_NAME),
                String.format(Messages.MESSAGE_CONFLICTING_IDENTIFIERS, PREFIX_ID, PREFIX_NAME),
                PREFIX_ID, PREFIX_NAME);
        boolean hasId = arguments.getValue(PREFIX_ID).isPresent();
        ParserUtil.validateRequiredParameters(arguments, PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_DELIVERABLE);
        String course = arguments.getValue(PREFIX_COURSE).orElseThrow();
        String semester = arguments.getValue(PREFIX_SEMESTER).orElseThrow();
        String deliverablesText = arguments.getValue(PREFIX_DELIVERABLE).orElseThrow();

        CourseCode courseCode = ParserUtil.parseCourseCode(course);
        Semester parsedSemester = ParserUtil.parseSemester(semester);
        List<Deliverable> deliverables = ParserUtil.parseDeliverables(deliverablesText);

        if (hasId) {
            ParserUtil.validateRequiredParameters(arguments, PREFIX_ID);
            return createCommand(ParserUtil.parseId(arguments.getValue(PREFIX_ID).orElseThrow()),
                    courseCode, parsedSemester, deliverables);
        }

        ParserUtil.validateRequiredParameters(arguments, PREFIX_NAME);
        return createCommand(arguments.getValue(PREFIX_NAME).orElseThrow(),
                courseCode, parsedSemester, deliverables);
    }

    abstract T createCommand(Id studentId, CourseCode courseCode, Semester semester,
            List<Deliverable> deliverables);

    abstract T createCommand(String studentName, CourseCode courseCode, Semester semester,
            List<Deliverable> deliverables);

}
