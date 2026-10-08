package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.student.Email;
import seedu.address.model.student.Id;
import seedu.address.model.student.Name;
import seedu.address.model.student.Notes;
import seedu.address.model.student.TutorialClass;

/**
 * Contains utility methods used for parsing strings in the various *Parser
 * classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index must be a positive integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading
     * and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the specified index is invalid (not a non-zero
     *                        unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /** Parses a student ID. */
    public static Id parseId(String id) throws ParseException {
        requireNonNull(id);
        String trimmedId = id.trim();
        if (!Id.isValidId(trimmedId)) {
            throw new ParseException(Id.MESSAGE_CONSTRAINTS);
        }
        return new Id(trimmedId);
    }

    /** Parses a tutorial class. */
    public static TutorialClass parseTutorialClass(String tutorialClass) throws ParseException {
        requireNonNull(tutorialClass);
        String trimmedClass = tutorialClass.trim();
        if (!TutorialClass.isValidClass(trimmedClass)) {
            throw new ParseException(TutorialClass.MESSAGE_CONSTRAINTS);
        }
        return new TutorialClass(trimmedClass);
    }

    /** Parses optional student notes. */
    public static Notes parseNotes(String notes) throws ParseException {
        requireNonNull(notes);
        String trimmedNotes = notes.trim();
        if (!Notes.isValidNotes(trimmedNotes)) {
            throw new ParseException(Notes.MESSAGE_CONSTRAINTS);
        }
        return new Notes(trimmedNotes);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String courseCode} into a {@code CourseCode}.
     */
    public static CourseCode parseCourseCode(String courseCode) throws ParseException {
        requireNonNull(courseCode);
        String trimmedCourseCode = courseCode.trim();
        if (!CourseCode.isValidCourseCode(trimmedCourseCode)) {
            throw new ParseException(CourseCode.MESSAGE_CONSTRAINTS);
        }
        return new CourseCode(trimmedCourseCode);
    }

    /**
     * Parses a {@code String semester} into a {@code Semester}.
     */
    public static Semester parseSemester(String semester) throws ParseException {
        requireNonNull(semester);
        String trimmedSemester = semester.trim();
        if (!Semester.isValidSemester(trimmedSemester)) {
            throw new ParseException(Semester.MESSAGE_CONSTRAINTS);
        }
        return new Semester(trimmedSemester);
    }

    /**
     * Parses a comma-separated {@code String} into a list of deliverables.
     */
    public static List<Deliverable> parseDeliverables(String deliverables) throws ParseException {
        requireNonNull(deliverables);
        List<String> deliverableNames = List.of(deliverables.split(",", -1));
        if (deliverableNames.isEmpty() || deliverableNames.stream().anyMatch(String::isBlank)) {
            throw new ParseException(Deliverable.MESSAGE_CONSTRAINTS);
        }
        List<Deliverable> parsedDeliverables = deliverableNames.stream()
                .map(String::trim)
                .map(Deliverable::new)
                .toList();
        if (!Course.hasUniqueDeliverables(parsedDeliverables)) {
            throw new ParseException(Course.MESSAGE_DELIVERABLES);
        }
        return parsedDeliverables;
    }
}
