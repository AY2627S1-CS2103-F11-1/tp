//@@author mizzfain
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELIVERABLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.NotesCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.Notes;
import seedu.address.model.student.Id;

/**
 * Parses input arguments and creates a NotesCommand.
 */
public class NotesCommandParser implements Parser<NotesCommand> {

    public static final String MESSAGE_MISSING_NOTES = "Missing required parameter: " + PREFIX_NOTES;

    public static final String MESSAGE_EMPTY_NOTES = "Empty parameter value: " + PREFIX_NOTES;

    public static final String MESSAGE_PARAMETERS_AFTER_NOTES = "The " + PREFIX_NOTES
            + " parameter must be the final parameter.";

    private static final String NOTES_PREFIX_REGEX = "(^|\\s)" + Pattern.quote(PREFIX_NOTES.toString()) + "(?=\\s|$)";

    private static final Pattern NOTES_PREFIX_PATTERN = Pattern.compile(NOTES_PREFIX_REGEX);
    private static final Pattern PARAMETER_AFTER_NOTES_PATTERN = Pattern.compile(
            "(^|\\s)/\\S+(?=\\s|$)");

    private static final Prefix[] STRUCTURED_PREFIXES = {
        PREFIX_ID, PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_DELIVERABLE
    };

    @Override
    public NotesCommand parse(String args) throws ParseException {
        requireNonNull(args);

        Matcher notesMatcher = NOTES_PREFIX_PATTERN.matcher(args);
        if (!notesMatcher.find()) {
            throw new ParseException(MESSAGE_MISSING_NOTES);
        }
        String structuredArgs = args.substring(0, notesMatcher.start());

        ParserUtil.rejectUnknownParameters(
                structuredArgs, STRUCTURED_PREFIXES);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                structuredArgs, STRUCTURED_PREFIXES);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(Messages.MESSAGE_UNEXPECTED_TEXT);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(STRUCTURED_PREFIXES);

        ParserUtil.validateRequiredParameters(
                argMultimap, STRUCTURED_PREFIXES);

        Id studentId = ParserUtil.parseId(
                argMultimap.getValue(PREFIX_ID).orElseThrow());

        CourseCode courseCode = ParserUtil.parseCourseCode(
                argMultimap.getValue(PREFIX_COURSE).orElseThrow());

        Semester semester = ParserUtil.parseSemester(
                argMultimap.getValue(PREFIX_SEMESTER).orElseThrow());

        String deliverable = argMultimap.getValue(PREFIX_DELIVERABLE).orElseThrow();

        String noteText = getNotesText(args, notesMatcher.end());

        Notes notes;
        try {
            notes = new Notes(noteText, deliverable);
        } catch (IllegalArgumentException exception) {
            throw new ParseException(exception.getMessage(), exception);
        }

        return new NotesCommand(studentId, courseCode, semester, notes);
    }

    /**
     * Returns the free-text value following the notes parameter.
     *
     * @throws ParseException if the notes value is blank.
     */
    private static String getNotesText(String args, int notesEnd)
            throws ParseException {
        String noteText = args.substring(notesEnd).trim();

        if (noteText.isBlank()) {
            throw new ParseException(MESSAGE_EMPTY_NOTES);
        }

        if (PARAMETER_AFTER_NOTES_PATTERN.matcher(noteText).find()) {
            throw new ParseException(MESSAGE_PARAMETERS_AFTER_NOTES);
        }

        return noteText;
    }
}
