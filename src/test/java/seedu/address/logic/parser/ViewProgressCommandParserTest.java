package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

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
        assertParseSuccess(parser, " /name John Doe",
                new ViewProgressCommand("John Doe"));
    }

    @Test
    public void parse_missingStudent_throwsParseException() {
        assertParseFailure(parser, "",
                "Missing required parameter: /id or /name");
    }

    @Test
    public void parse_bothStudentIdentifiers_throwsParseException() {
        assertParseFailure(parser, " /id A1234567Z /name John Doe",
                "Please specify either /id or /name, not both.");
    }

    @Test
    public void parse_unknownParameter_throwsParseException() {
        assertParseFailure(parser, " /id A1234567Z /deliverable MVP",
                "Unknown parameter: /deliverable");
    }
}
