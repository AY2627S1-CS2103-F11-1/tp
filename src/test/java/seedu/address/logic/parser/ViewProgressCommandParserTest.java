package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.ViewProgressCommand;
import seedu.address.model.student.Id;

/** Tests for {@code ViewProgressCommandParser}. */
public class ViewProgressCommandParserTest {

    private final ViewProgressCommandParser parser = new ViewProgressCommandParser();

    @Test
    public void parse_validId_returnsViewProgressCommand() {
        assertParseSuccess(parser, " /id A1234567Z",
                new ViewProgressCommand(new Id("A1234567Z")));
    }

    @Test
    public void parse_validName_returnsViewProgressCommand() {
        assertParseSuccess(parser, " /name John Doe", new ViewProgressCommand("John Doe"));
    }

    @Test
    public void parse_missingStudent_throwsParseException() {
        assertParseFailure(parser, "", ViewProgressCommandParser.MESSAGE_MISSING_STUDENT);
    }

    @Test
    public void parse_bothStudentIdentifiers_throwsParseException() {
        assertParseFailure(parser, " /id A1234567Z /name John Doe",
                ViewProgressCommandParser.MESSAGE_BOTH_IDENTIFIERS);
    }

    @Test
    public void parse_emptyIdentifier_throwsParseException() {
        assertParseFailure(parser, " /id ", ViewProgressCommandParser.MESSAGE_EMPTY_PARAMETER);
        assertParseFailure(parser, " /name ", ViewProgressCommandParser.MESSAGE_EMPTY_PARAMETER);
    }

    @Test
    public void parse_duplicateIdentifier_throwsParseException() {
        assertParseFailure(parser, " /id A1234567Z /id A2345678Y",
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_ID));
        assertParseFailure(parser, " /name John Doe /name Jane Doe",
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_NAME));
    }

    @Test
    public void parse_invalidId_throwsParseException() {
        assertParseFailure(parser, " /id invalid", Id.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_unknownParameter_throwsParseException() {
        assertParseFailure(parser, " /id A1234567Z /course CS2103T",
                ViewProgressCommandParser.MESSAGE_UNKNOWN_PARAMETER);
        assertParseFailure(parser, " /name John Doe /sem AY26/27-S1",
                ViewProgressCommandParser.MESSAGE_UNKNOWN_PARAMETER);
    }

    @Test
    public void parse_unexpectedPreamble_throwsParseException() {
        assertParseFailure(parser, "John Doe /id A1234567Z",
                ViewProgressCommandParser.MESSAGE_UNEXPECTED_TEXT);
    }
}
