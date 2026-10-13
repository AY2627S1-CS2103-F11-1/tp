package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Tests the parser for {@link ListCommand}. */
public class ListCommandParserTest {

    private final ListCommandParser parser = new ListCommandParser();

    @Test
    public void parse_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_emptyOrWhitespace_success() throws ParseException {
        assertTrue(parser.parse("") instanceof ListCommand);
        assertTrue(parser.parse("   \t\n") instanceof ListCommand);
    }

    @Test
    public void parse_parameters_failure() {
        assertParseFailure(parser, "/name Alice", ListCommandParser.MESSAGE_UNKNOWN_PARAMETER);
        assertParseFailure(parser, " /id A1234567Y", ListCommandParser.MESSAGE_UNKNOWN_PARAMETER);
        assertParseFailure(parser, "/unknown value", ListCommandParser.MESSAGE_UNKNOWN_PARAMETER);
    }

    @Test
    public void parse_unexpectedText_failure() {
        assertParseFailure(parser, "students", ListCommandParser.MESSAGE_UNEXPECTED_TEXT);
        assertParseFailure(parser, "show students", ListCommandParser.MESSAGE_UNEXPECTED_TEXT);
        assertParseFailure(parser, "/", ListCommandParser.MESSAGE_UNEXPECTED_TEXT);
    }
}
