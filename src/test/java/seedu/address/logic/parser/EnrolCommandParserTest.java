package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EnrolCommand;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.student.Id;
import seedu.address.model.student.TutorialClass;

/** Tests for {@code EnrolCommandParser}. */
public class EnrolCommandParserTest {
    private final EnrolCommandParser parser = new EnrolCommandParser();

    @Test
    public void parse_validArguments_success() {
        assertParseSuccess(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1 /class T07",
                new EnrolCommand(new Id("A1234567Z"), new CourseCode("CS2103T"),
                        new Semester("AY26/27-S1"), new TutorialClass("T07")));
    }

    @Test
    public void parse_missingParameter_failure() {
        assertParseFailure(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1",
                "Missing required parameter(s): /class");
    }

    @Test
    public void parse_duplicateParameter_failure() {
        assertParseFailure(parser, " /id A1234567Z /id A7654321Z /course CS2103T /sem AY26/27-S1 /class T07",
                "Multiple values specified for the following single-valued field(s): /id");
    }

    @Test
    public void parse_unknownParameter_failure() {
        assertParseFailure(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1 /foo T07",
                String.format(ParserUtil.MESSAGE_UNKNOWN_PARAMETER, "/foo"));
    }
}
