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
        assertParseFailure(parser, "", SearchCommandParser.MESSAGE_MISSING_IDENTIFIER);
        assertParseFailure(parser, " /name ", SearchCommandParser.MESSAGE_EMPTY_IDENTIFIER);
        assertParseFailure(parser, " /id ", SearchCommandParser.MESSAGE_EMPTY_IDENTIFIER);
    }

    @Test
    public void parse_invalidOrUnknownParameter_failure() {
        assertParseFailure(parser, " /id A123", Id.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " /class T07", SearchCommandParser.MESSAGE_UNKNOWN_PARAMETER);
        assertParseFailure(parser, " John /name John", SearchCommandParser.MESSAGE_UNEXPECTED_TEXT);
        assertParseFailure(parser, " John", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " student/search",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicateOrConflictingIdentifier_failure() {
        assertParseFailure(parser, " /name John /name Jane",
                String.format(SearchCommandParser.MESSAGE_DUPLICATE_PARAMETER, "/name"));
        assertParseFailure(parser, " /id A1234567Z /id A2345678X",
                String.format(SearchCommandParser.MESSAGE_DUPLICATE_PARAMETER, "/id"));
        assertParseFailure(parser, " /name John /id A1234567Z", SearchCommandParser.MESSAGE_BOTH_IDENTIFIERS);
    }
}
