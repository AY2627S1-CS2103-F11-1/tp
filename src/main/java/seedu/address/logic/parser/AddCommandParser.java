package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.NotesList;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Email;
import seedu.address.model.student.Id;
import seedu.address.model.student.Name;
import seedu.address.model.student.Student;
import seedu.address.model.student.TutorialClass;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    public static final String MESSAGE_DUPLICATE_ENROLMENT = "Duplicate course-semester enrolment specified.";
    public static final String MESSAGE_EMPTY_ENROLMENT_VALUE = "Enrolment values cannot be empty.";
    public static final String MESSAGE_MISMATCHED_ENROLMENTS =
            "The number of courses, semesters and tutorial classes must match.";

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected
     *                        format
     */
    public AddCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ParserUtil.rejectUnknownParameters(args, PREFIX_NAME, PREFIX_ID, PREFIX_EMAIL,
                PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ID, PREFIX_EMAIL,
                PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(Messages.MESSAGE_UNEXPECTED_TEXT);
        }
        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_NAME, PREFIX_ID, PREFIX_EMAIL,
                PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS);
        ParserUtil.validateRequiredParameters(
                argMultimap, PREFIX_NAME, PREFIX_ID, PREFIX_EMAIL,
                PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).orElseThrow());
        Id id = ParserUtil.parseId(argMultimap.getValue(PREFIX_ID).orElseThrow());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).orElseThrow());

        List<String> courses = parseValues(
                argMultimap.getValue(PREFIX_COURSE).orElseThrow());

        List<String> semesters = parseValues(
                argMultimap.getValue(PREFIX_SEMESTER).orElseThrow());

        List<String> classes = parseValues(
                argMultimap.getValue(PREFIX_CLASS).orElseThrow());
        Student student = new Student(name, id, email);

        if (courses.size() != semesters.size()
                || courses.size() != classes.size()) {
            throw new ParseException(MESSAGE_MISMATCHED_ENROLMENTS);
        }

        List<StudentCourseEnrolment> enrolments = new ArrayList<>();

        for (int i = 0; i < courses.size(); i++) {
            CourseCode courseCode = ParserUtil.parseCourseCode(courses.get(i));
            Semester semester = ParserUtil.parseSemester(semesters.get(i));
            TutorialClass tutorialClass = ParserUtil.parseTutorialClass(classes.get(i));

            StudentCourseEnrolment enrolment = new StudentCourseEnrolment(
                    id, courseCode, semester, tutorialClass, new NotesList());

            if (enrolments.stream().anyMatch(existing -> existing.isSameEnrolment(enrolment))) {
                throw new ParseException(MESSAGE_DUPLICATE_ENROLMENT);
            }

            enrolments.add(enrolment);
        }

        return new AddCommand(student, enrolments);

    }

    /**
     * Parses a comma-separated list of non-blank values.
     *
     * @throws ParseException if any value is blank.
     */
    private static List<String> parseValues(String input) throws ParseException {
        List<String> values = Arrays.stream(input.split(",", -1))
                .map(String::trim)
                .toList();

        if (values.stream().anyMatch(String::isBlank)) {
            throw new ParseException(MESSAGE_EMPTY_ENROLMENT_VALUE);
        }

        return values;
    }
}
