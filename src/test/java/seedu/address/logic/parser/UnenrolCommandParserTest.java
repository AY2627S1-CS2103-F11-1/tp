package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UnenrolCommand;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.student.Id;

/** Tests for {@code UnenrolCommandParser}. */
public class UnenrolCommandParserTest {
    private final UnenrolCommandParser parser = new UnenrolCommandParser();

    @Test
    public void parse_validArguments_success() {
        assertParseSuccess(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1",
                new UnenrolCommand(new Id("A1234567Z"), new CourseCode("CS2103T"),
                        new Semester("AY26/27-S1")));
    }

    @Test
    public void parse_missingParameter_failure() {
        assertParseFailure(parser, " /id A1234567Z /course CS2103T",
                "Missing required parameter(s): /sem");
    }

    @Test
    public void parse_duplicateParameter_failure() {
        assertParseFailure(parser, " /id A1234567Z /id A7654321Z /course CS2103T /sem AY26/27-S1",
                "Multiple values specified for the following single-valued field(s): /id");
    }

    @Test
    public void parse_unknownParameter_failure() {
        assertParseFailure(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1 /class T07",
                String.format(ParserUtil.MESSAGE_UNKNOWN_PARAMETER, "/class"));
    }
}
