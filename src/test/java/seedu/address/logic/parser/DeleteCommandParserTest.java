package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.model.student.Id;

/** Tests for {@code DeleteCommandParser}. */
public class DeleteCommandParserTest {

    private final DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validId_returnsDeleteCommand() {
        assertParseSuccess(parser, " /id A1234567Z", new DeleteCommand(new Id("A1234567Z")));
    }

    @Test
    public void parse_validName_returnsDeleteCommand() {
        assertParseSuccess(parser, " /name John Doe", new DeleteCommand("John Doe"));
    }

    @Test
    public void parse_missingIdentifier_throwsParseException() {
        assertParseFailure(parser, "", DeleteCommandParser.MESSAGE_MISSING_IDENTIFIER);
    }

    @Test
    public void parse_emptyIdentifier_throwsParseException() {
        assertParseFailure(parser, " /id", DeleteCommandParser.MESSAGE_EMPTY_IDENTIFIER);
        assertParseFailure(parser, " /name", DeleteCommandParser.MESSAGE_EMPTY_IDENTIFIER);
    }

    @Test
    public void parse_bothIdentifiers_throwsParseException() {
        assertParseFailure(parser, " /id A1234567Z /name John Doe",
                DeleteCommandParser.MESSAGE_BOTH_IDENTIFIERS);
    }

    @Test
    public void parse_duplicateIdentifier_throwsParseException() {
        assertParseFailure(parser, " /id A1234567Z /id A7654321Z",
                String.format(DeleteCommandParser.MESSAGE_DUPLICATE_PARAMETER, "/id"));
    }

    @Test
    public void parse_unknownParameter_throwsParseException() {
        assertParseFailure(parser, " /id A1234567Z /studentName John Doe",
                DeleteCommandParser.MESSAGE_UNKNOWN_PARAMETER);
    }

    @Test
    public void parse_unexpectedTextBeforeParameter_throwsParseException() {
        assertParseFailure(parser, " randomText /id A1234567Z",
                DeleteCommandParser.MESSAGE_UNEXPECTED_TEXT);
    }

    @Test
    public void parse_invalidFormat_throwsParseException() {
        assertParseFailure(parser, " A1234567Z",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }
}
