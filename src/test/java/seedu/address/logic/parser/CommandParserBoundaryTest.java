package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;

/** Boundary tests shared by command parsers that require parameters. */
public class CommandParserBoundaryTest {

    private static final List<Parser<?>> PARAMETERIZED_PARSERS = List.of(
            new AddCommandParser(),
            new DeleteCommandParser(),
            new DeleteNotesCommandParser(),
            new EditCommandParser(),
            new EnrolCommandParser(),
            new InitCourseCommandParser(),
            new MarkCommandParser(),
            new NotesCommandParser(),
            new SearchCommandParser(),
            new UnenrolCommandParser(),
            new UnmarkCommandParser(),
            new ViewNotesCommandParser(),
            new ViewProgressCommandParser());

    @Test
    public void parse_emptyInput_rejectsForEveryParameterizedParser() {
        // BVA: empty input at the lower boundary of required arguments.
        PARAMETERIZED_PARSERS.forEach(parser ->
                assertThrows(ParseException.class, () -> parser.parse("")));
    }

    @Test
    public void parse_whitespaceOnlyInput_rejectsForEveryParameterizedParser() {
        // EP: whitespace-only values are invalid parameter input.
        PARAMETERIZED_PARSERS.forEach(parser ->
                assertThrows(ParseException.class, () -> parser.parse(" \t\n ")));
    }

    @Test
    public void parse_unknownPrefix_rejectsForEveryParameterizedParser() {
        // EP: an unrecognised prefix is outside the supported parameter syntax.
        PARAMETERIZED_PARSERS.forEach(parser ->
                assertThrows(ParseException.class, () -> parser.parse(" /unknown value")));
    }
}
