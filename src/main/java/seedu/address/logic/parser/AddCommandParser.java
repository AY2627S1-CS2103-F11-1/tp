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
import java.util.Optional;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Email;
import seedu.address.model.student.Id;
import seedu.address.model.student.Name;
import seedu.address.model.student.Notes;
import seedu.address.model.student.Student;
import seedu.address.model.student.TutorialClass;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    public static final String MESSAGE_UNEXPECTED_TEXT = "Unexpected text found before parameter.";
    public static final String MESSAGE_UNKNOWN_PARAMETER = String.format(
            "Unknown parameter. Parameters allowed: %s, %s, %s, %s, %s, %s",
            PREFIX_NAME, PREFIX_ID, PREFIX_EMAIL,
            PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS);
    public static final String MESSAGE_MISSING_PARAMETERS = "Missing required parameter(s): %s";
    public static final String MESSAGE_EMPTY_PARAMETERS = "Empty parameter value(s): %s";
    public static final String MESSAGE_DUPLICATE_ENROLMENT = "Duplicate course-semester enrolment specified.";
    public static final String MESSAGE_EMPTY_ENROLMENT_VALUE = "Enrolment values cannot be empty.";
    public static final String MESSAGE_MISMATCHED_ENROLMENTS =
            "The number of courses, semesters and tutorial classes must match.";

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("(^|\\s)/(\\S+)");

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected
     *                        format
     */
    public AddCommand parse(String args) throws ParseException {
        requireNonNull(args);
        rejectUnknownParameters(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ID, PREFIX_EMAIL,
                PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_UNEXPECTED_TEXT);
        }
        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_NAME, PREFIX_ID, PREFIX_EMAIL,
                PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS);
        validateRequiredParameters(argMultimap);
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
                    id, courseCode, semester, tutorialClass, new Notes());

            if (enrolments.stream().anyMatch(existing -> existing.isSameEnrolment(enrolment))) {
                throw new ParseException(MESSAGE_DUPLICATE_ENROLMENT);
            }

            enrolments.add(enrolment);
        }

        return new AddCommand(student, enrolments);

    }

    /** Validates that every required parameter is present and non-empty. */
    private void validateRequiredParameters(ArgumentMultimap argMultimap) throws ParseException {
        StringJoiner missingParameters = new StringJoiner(", ");
        StringJoiner emptyParameters = new StringJoiner(", ");
        for (Prefix prefix : new Prefix[] {
            PREFIX_NAME, PREFIX_ID, PREFIX_EMAIL,
            PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS }) {
            Optional<String> value = argMultimap.getValue(prefix);

            if (value.isEmpty()) {
                missingParameters.add(prefix.toString());
            } else if (value.get().isBlank()) {
                emptyParameters.add(prefix.toString());
            }

        }

        StringJoiner errorMessages = new StringJoiner("\n");

        if (missingParameters.length() > 0) {
            errorMessages.add(String.format(
                    MESSAGE_MISSING_PARAMETERS, missingParameters));
        }

        if (emptyParameters.length() > 0) {
            errorMessages.add(String.format(
                    MESSAGE_EMPTY_PARAMETERS, emptyParameters));
        }

        if (errorMessages.length() > 0) {
            throw new ParseException(errorMessages.toString());
        }

    }

    /** Rejects parameters that are not supported by the add command. */
    private void rejectUnknownParameters(String args) throws ParseException {
        Matcher matcher = PARAMETER_PATTERN.matcher(args);

        while (matcher.find()) {
            String parameter = "/" + matcher.group(2);

            if (!parameter.equals(PREFIX_NAME.toString())
                    && !parameter.equals(PREFIX_ID.toString())
                    && !parameter.equals(PREFIX_EMAIL.toString())
                    && !parameter.equals(PREFIX_COURSE.toString())
                    && !parameter.equals(PREFIX_SEMESTER.toString())
                    && !parameter.equals(PREFIX_CLASS.toString())) {
                throw new ParseException(MESSAGE_UNKNOWN_PARAMETER);
            }
        }
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
