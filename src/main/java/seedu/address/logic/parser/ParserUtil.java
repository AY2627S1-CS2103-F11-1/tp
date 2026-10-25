package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Messages;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.student.Email;
import seedu.address.model.student.Id;
import seedu.address.model.student.Name;
import seedu.address.model.student.TutorialClass;

/**
 * Contains utility methods used for parsing strings in the various *Parser
 * classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index must be a positive integer.";
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("(^|\\s)/(\\S+)");

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
        try {
            return Index.fromOneBased(Integer.parseInt(trimmedIndex));
        } catch (NumberFormatException exception) {
            throw new ParseException(MESSAGE_INVALID_INDEX, exception);
        }
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
        String trimmedId = id.trim().toUpperCase(Locale.ROOT);
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
        List<Deliverable> parsedDeliverables = new java.util.ArrayList<>();
        for (String deliverableName : deliverableNames) {
            String trimmedName = deliverableName.trim();
            if (!Deliverable.isValidDeliverable(trimmedName)) {
                throw new ParseException(Deliverable.MESSAGE_CONSTRAINTS);
            }
            parsedDeliverables.add(new Deliverable(trimmedName));
        }
        if (!Course.hasUniqueDeliverables(parsedDeliverables)) {
            throw new ParseException(Course.MESSAGE_DELIVERABLES);
        }
        return parsedDeliverables;
    }

    /**
     * Rejects parameters that are not included in {@code allowedPrefixes}.
     *
     * @throws ParseException if an unknown parameter is found.
     */
    public static void rejectUnknownParameters(
            String args, Prefix... allowedPrefixes) throws ParseException {
        requireNonNull(args);
        requireNonNull(allowedPrefixes);

        Set<String> allowed = Arrays.stream(allowedPrefixes)
                .map(Objects::requireNonNull)
                .map(Prefix::toString)
                .collect(Collectors.toSet());

        Matcher matcher = PARAMETER_PATTERN.matcher(args);

        while (matcher.find()) {
            String parameter = "/" + matcher.group(2);

            if (!allowed.contains(parameter)) {
                throw new ParseException(String.format(Messages.MESSAGE_UNKNOWN_PARAMETER, parameter));
            }
        }
    }

    /**
     * Validates that all required parameters are present and non-blank.
     *
     * @throws ParseException if any required parameter is missing or empty.
     */
    public static void validateRequiredParameters(
            ArgumentMultimap argMultimap, Prefix... requiredPrefixes)
            throws ParseException {
        requireNonNull(argMultimap);
        requireNonNull(requiredPrefixes);

        StringJoiner missingParameters = new StringJoiner(", ");
        StringJoiner emptyParameters = new StringJoiner(", ");

        for (Prefix prefix : requiredPrefixes) {
            requireNonNull(prefix);

            Optional<String> value = argMultimap.getValue(prefix);

            if (value.isEmpty()) {
                missingParameters.add(prefix.toString());
            } else if (value.orElseThrow().isBlank()) {
                emptyParameters.add(prefix.toString());
            }
        }

        StringJoiner errors = new StringJoiner("\n");

        if (missingParameters.length() > 0) {
            errors.add(String.format(Messages.MESSAGE_MISSING_PARAMETERS, missingParameters));
        }

        if (emptyParameters.length() > 0) {
            errors.add(String.format(Messages.MESSAGE_EMPTY_PARAMETERS, emptyParameters));
        }

        if (errors.length() > 0) {
            throw new ParseException(errors.toString());
        }
    }

    /** Validates that exactly one of the supplied alternative prefixes is present. */
    public static void validateExactlyOneOf(ArgumentMultimap argMultimap,
                                            String missingMessage, String conflictMessage,
                                            Prefix firstPrefix, Prefix secondPrefix) throws ParseException {
        requireNonNull(argMultimap);
        requireNonNull(missingMessage);
        requireNonNull(conflictMessage);
        requireNonNull(firstPrefix);
        requireNonNull(secondPrefix);

        boolean hasFirst = argMultimap.getValue(firstPrefix).isPresent();
        boolean hasSecond = argMultimap.getValue(secondPrefix).isPresent();
        if (!hasFirst && !hasSecond) {
            throw new ParseException(missingMessage);
        }
        if (hasFirst && hasSecond) {
            throw new ParseException(conflictMessage);
        }
    }

    /**
     * Rejects parameters that appear more than once.
     *
     * @throws ParseException if any specified parameter is duplicated.
     */
    public static void rejectDuplicateParameters(
            ArgumentMultimap argMultimap, Prefix... prefixes)
            throws ParseException {
        requireNonNull(argMultimap);
        requireNonNull(prefixes);

        for (Prefix prefix : prefixes) {
            requireNonNull(prefix);

            if (argMultimap.getAllValues(prefix).size() > 1) {
                throw new ParseException(
                        String.format(Messages.MESSAGE_DUPLICATE_PARAMETER, prefix));
            }
        }
    }
}
