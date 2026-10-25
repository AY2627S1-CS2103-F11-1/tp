package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import java.util.Optional;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.student.TutorialClass;

/** Parses arguments for the {@code list} command. */
public class ListCommandParser implements Parser<ListCommand> {

    public static final String MESSAGE_UNEXPECTED_TEXT = "Unexpected text found after list command.";
    public static final String MESSAGE_CLASS_REQUIRES_COURSE = PREFIX_CLASS
            + " can be used only with " + PREFIX_COURSE + " and " + PREFIX_SEMESTER + ".";

    /**
     * Parses list command arguments.
     *
     * @param args arguments supplied after the command word
     * @return a list command
     * @throws ParseException if arguments do not match the supported filters
     */
    @Override
    public ListCommand parse(String args) throws ParseException {
        requireNonNull(args);

        if (args.trim().isEmpty()) {
            return new ListCommand();
        }

        ParserUtil.rejectUnknownParameters(args, PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS);
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_UNEXPECTED_TEXT);
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_COURSE, PREFIX_SEMESTER, PREFIX_CLASS);

        boolean hasCourse = argMultimap.getValue(PREFIX_COURSE).isPresent();
        boolean hasSemester = argMultimap.getValue(PREFIX_SEMESTER).isPresent();
        boolean hasClass = argMultimap.getValue(PREFIX_CLASS).isPresent();
        if (!hasCourse || !hasSemester) {
            if (hasClass && !hasCourse && !hasSemester) {
                throw new ParseException(MESSAGE_CLASS_REQUIRES_COURSE);
            }
            ParserUtil.validateRequiredParameters(argMultimap, PREFIX_COURSE, PREFIX_SEMESTER);
        }
        ParserUtil.validateRequiredParameters(argMultimap, PREFIX_COURSE, PREFIX_SEMESTER);

        Optional<TutorialClass> tutorialClass = Optional.empty();
        if (hasClass) {
            ParserUtil.validateRequiredParameters(argMultimap, PREFIX_CLASS);
            tutorialClass = Optional.of(ParserUtil.parseTutorialClass(
                    argMultimap.getValue(PREFIX_CLASS).orElseThrow()));
        }

        CourseCode courseCode = ParserUtil.parseCourseCode(
                argMultimap.getValue(PREFIX_COURSE).orElseThrow());
        Semester semester = ParserUtil.parseSemester(
                argMultimap.getValue(PREFIX_SEMESTER).orElseThrow());
        return new ListCommand(courseCode, semester, tutorialClass);
    }
}
