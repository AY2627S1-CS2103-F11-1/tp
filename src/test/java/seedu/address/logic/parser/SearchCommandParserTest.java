package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SearchCommand;
import seedu.address.model.student.Id;
import seedu.address.model.student.StudentMatchesQueryPredicate;

public class SearchCommandParserTest {
    private final SearchCommandParser parser = new SearchCommandParser();

    @Test
    public void parse_name_success() {
        assertParseSuccess(parser, " /name John Doe",
                new SearchCommand(new StudentMatchesQueryPredicate("John Doe")));
    }

    @Test
    public void parse_id_success() {
        assertParseSuccess(parser, " /id A1234567Z",
                new SearchCommand(new StudentMatchesQueryPredicate(new Id("A1234567Z"))));
        assertParseSuccess(parser, " /id a1234567z",
                new SearchCommand(new StudentMatchesQueryPredicate(new Id("A1234567Z"))));
    }

    @Test
    public void parse_missingOrEmptyIdentifier_failure() {
        assertParseFailure(parser, "", "Missing required parameter: /name or /id");
        assertParseFailure(parser, " /name ", "Empty parameter value(s): /name");
        assertParseFailure(parser, " /id ", "Empty parameter value(s): /id");
    }

    @Test
    public void parse_invalidOrUnknownParameter_failure() {
        assertParseFailure(parser, " /id A123", Id.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " /class T07", "Unknown parameter: /class");
        assertParseFailure(parser, " John /name John", "Unexpected text found before parameter.");
        assertParseFailure(parser, " John", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " student/search",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicateOrConflictingIdentifier_failure() {
        assertParseFailure(parser, " /name John /name Jane",
                "Multiple values specified for the following single-valued field(s): /name");
        assertParseFailure(parser, " /id A1234567Z /id A2345678X",
                "Multiple values specified for the following single-valued field(s): /id");
        assertParseFailure(parser, " /name John /id A1234567Z",
                "Please specify either /name or /id, not both.");
    }
}
