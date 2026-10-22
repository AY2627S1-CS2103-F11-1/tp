package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ViewProgressCommand;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.student.Id;

/** Tests for {@code ViewProgressCommandParser}. */
public class ViewProgressCommandParserTest {

    private final ViewProgressCommandParser parser = new ViewProgressCommandParser();

    @Test
    public void parse_validId_returnsViewProgressCommand() {
        assertParseSuccess(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1",
                new ViewProgressCommand(new Id("A1234567Z"), new CourseCode("CS2103T"),
                        new Semester("AY26/27-S1")));
    }

    @Test
    public void parse_validName_returnsViewProgressCommand() {
        assertParseSuccess(parser, " /name John Doe /course CS2103T /sem AY26/27-S1",
                new ViewProgressCommand("John Doe", new CourseCode("CS2103T"), new Semester("AY26/27-S1")));
    }

    @Test
    public void parse_missingStudent_throwsParseException() {
        assertParseFailure(parser, " /course CS2103T /sem AY26/27-S1",
                ViewProgressCommandParser.MESSAGE_MISSING_STUDENT);
    }

    @Test
    public void parse_bothStudentIdentifiers_throwsParseException() {
        assertParseFailure(parser, " /id A1234567Z /name John Doe /course CS2103T /sem AY26/27-S1",
                ViewProgressCommandParser.MESSAGE_BOTH_IDENTIFIERS);
    }

    @Test
    public void parse_unknownParameter_throwsParseException() {
        assertParseFailure(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1 /task MVP",
                "Unknown parameter: /task");
    }
}
