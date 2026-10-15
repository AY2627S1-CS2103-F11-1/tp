package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteNotesCommand;
import seedu.address.model.student.Id;

/** Tests for {@code DeleteNotesCommandParser}. */
public class DeleteNotesCommandParserTest {

    private final DeleteNotesCommandParser parser = new DeleteNotesCommandParser();

    @Test
    public void parse_validId_success() {
        assertParseSuccess(parser, " /id A1234567Z", new DeleteNotesCommand(new Id("A1234567Z")));
    }

    @Test
    public void parse_missingOrDuplicateId_failure() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteNotesCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " /id A1234567Z /id A7654321Z",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteNotesCommand.MESSAGE_USAGE));
    }
}
