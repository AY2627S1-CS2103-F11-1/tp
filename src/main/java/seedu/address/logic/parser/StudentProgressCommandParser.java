package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.StudentProgressCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.student.Id;

/**
 * Parses shared arguments for commands that update student deliverable progress.
 */
abstract class StudentProgressCommandParser<T extends StudentProgressCommand> implements Parser<T> {

    static final String MESSAGE_MISSING_STUDENT = "Missing required parameter: " + PREFIX_ID + " or " + PREFIX_NAME;
    static final String MESSAGE_MISSING_COURSE = "Missing required parameter: " + PREFIX_COURSE;
    static final String MESSAGE_MISSING_SEMESTER = "Missing required parameter: " + PREFIX_SEMESTER;
    static final String MESSAGE_MISSING_TASK = "Missing required parameter: " + PREFIX_TASK;
    static final String MESSAGE_EMPTY_PARAMETER = "Parameters cannot be empty.";
    static final String MESSAGE_BOTH_IDENTIFIERS = "Please specify either /id or /name, not both.";
    static final String MESSAGE_UNEXPECTED_TEXT = "Unexpected text found before parameter.";
    static final String MESSAGE_UNKNOWN_PARAMETER =
            "Unknown parameter. Parameters allowed: " + PREFIX_ID + ", " + PREFIX_NAME + ", " + PREFIX_COURSE
                    + ", " + PREFIX_SEMESTER + ", " + PREFIX_TASK;

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("(^|\\s)/(\\S+)");

    @Override
    public T parse(String args) throws ParseException {
        rejectUnknownParameters(args);

        ArgumentMultimap arguments = ArgumentTokenizer.tokenize(args, PREFIX_ID, PREFIX_NAME, PREFIX_COURSE,
                PREFIX_SEMESTER, PREFIX_TASK);
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
        if (!arguments.getValue(PREFIX_TASK).isPresent()) {
            throw new ParseException(MESSAGE_MISSING_TASK);
        }
        if (!arguments.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_UNEXPECTED_TEXT);
        }
        arguments.verifyNoDuplicatePrefixesFor(PREFIX_ID, PREFIX_NAME, PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_TASK);
        if (hasId && hasName) {
            throw new ParseException(MESSAGE_BOTH_IDENTIFIERS);
        }

        String course = arguments.getValue(PREFIX_COURSE).orElseThrow();
        String semester = arguments.getValue(PREFIX_SEMESTER).orElseThrow();
        String tasks = arguments.getValue(PREFIX_TASK).orElseThrow();
        if (course.isBlank() || semester.isBlank() || tasks.isBlank()) {
            throw new ParseException(MESSAGE_EMPTY_PARAMETER);
        }

        CourseCode courseCode = ParserUtil.parseCourseCode(course);
        Semester parsedSemester = ParserUtil.parseSemester(semester);
        List<Deliverable> deliverables = ParserUtil.parseDeliverables(tasks);

        if (hasId) {
            String id = arguments.getValue(PREFIX_ID).orElseThrow();
            if (id.isBlank()) {
                throw new ParseException(MESSAGE_EMPTY_PARAMETER);
            }
            return createCommand(ParserUtil.parseId(id), courseCode, parsedSemester, deliverables);
        }

        String name = arguments.getValue(PREFIX_NAME).orElseThrow();
        if (name.isBlank()) {
            throw new ParseException(MESSAGE_EMPTY_PARAMETER);
        }
        return createCommand(name, courseCode, parsedSemester, deliverables);
    }

    private void rejectUnknownParameters(String args) throws ParseException {
        Matcher matcher = PARAMETER_PATTERN.matcher(args);
        while (matcher.find()) {
            String parameter = matcher.group(2);
            if (!parameter.equals("id") && !parameter.equals("name") && !parameter.equals("course")
                    && !parameter.equals("sem") && !parameter.equals("task")) {
                throw new ParseException(MESSAGE_UNKNOWN_PARAMETER);
            }
        }
    }

    abstract T createCommand(Id studentId, CourseCode courseCode, Semester semester,
                             List<Deliverable> deliverables);

    abstract T createCommand(String studentName, CourseCode courseCode, Semester semester,
                             List<Deliverable> deliverables);

}
