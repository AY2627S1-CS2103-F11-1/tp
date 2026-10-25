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
        assertParseFailure(parser, "", "Missing required parameter: /id or /name");
    }

    @Test
    public void parse_emptyIdentifier_throwsParseException() {
        assertParseFailure(parser, " /id", "Empty parameter value(s): /id");
        assertParseFailure(parser, " /name", "Empty parameter value(s): /name");
    }

    @Test
    public void parse_bothIdentifiers_throwsParseException() {
        assertParseFailure(parser, " /id A1234567Z /name John Doe",
                "Please specify either /id or /name, not both.");
    }

    @Test
    public void parse_duplicateIdentifier_throwsParseException() {
        assertParseFailure(parser, " /id A1234567Z /id A7654321Z",
                "Multiple values specified for parameter: /id");
    }

    @Test
    public void parse_unknownParameter_throwsParseException() {
        assertParseFailure(parser, " /id A1234567Z /studentName John Doe",
                "Unknown parameter: /studentName");
    }

    @Test
    public void parse_unexpectedTextBeforeParameter_throwsParseException() {
        assertParseFailure(parser, " randomText /id A1234567Z",
                "Unexpected text found before parameter.");
    }

    @Test
    public void parse_invalidFormat_throwsParseException() {
        assertParseFailure(parser, " A1234567Z",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }
}
