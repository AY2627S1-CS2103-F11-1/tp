package seedu.address.logic.parser;

import static seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CourseProgressOverviewCommand;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;

public class CourseProgressOverviewCommandParserTest {

    private final CourseProgressOverviewCommandParser parser = new CourseProgressOverviewCommandParser();

    @Test
    public void parse_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_validArguments_success() {
        assertParseSuccess(parser, " /sem AY26/27-S1 /course CS2103T",
                new CourseProgressOverviewCommand(
                        new CourseCode("CS2103T"), new Semester("AY26/27-S1")));
    }

    @Test
    public void parse_missingOrEmptyParameter_failure() {
        assertParseFailure(parser, " /course CS2103T", "Missing required parameter(s): /sem");
        assertParseFailure(parser, " /sem AY26/27-S1", "Missing required parameter(s): /course");
        assertParseFailure(parser, " /course /sem AY26/27-S1", "Empty parameter value(s): /course");
        assertParseFailure(parser, " /course CS2103T /sem", "Empty parameter value(s): /sem");
    }

    @Test
    public void parse_duplicateParameter_failure() {
        assertParseFailure(parser, " /course CS2103T /course CS2105 /sem AY26/27-S1",
                getErrorMessageForDuplicatePrefixes(PREFIX_COURSE));
        assertParseFailure(parser, " /course CS2103T /sem AY26/27-S1 /sem AY25/26-S2",
                getErrorMessageForDuplicatePrefixes(PREFIX_SEMESTER));
    }

    @Test
    public void parse_unknownOrUnexpectedText_failure() {
        assertParseFailure(parser, " /course CS2103T /sem AY26/27-S1 /class T07",
                String.format(ParserUtil.MESSAGE_UNKNOWN_PARAMETER, "/class"));
        assertParseFailure(parser, "students /course CS2103T /sem AY26/27-S1",
                CourseProgressOverviewCommandParser.MESSAGE_UNEXPECTED_TEXT);
    }
}
