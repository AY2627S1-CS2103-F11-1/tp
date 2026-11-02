package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteNotesCommand;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.student.Id;

/** Tests for {@code DeleteNotesCommandParser}. */
public class DeleteNotesCommandParserTest {

    private final DeleteNotesCommandParser parser = new DeleteNotesCommandParser();

    @Test
    public void parse_validId_success() {
        assertParseSuccess(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1",
                new DeleteNotesCommand(new Id("A1234567Z"), new CourseCode("CS2103T"),
                        new Semester("AY26/27-S1")));
    }

    @Test
    public void parse_missingParameter_failure() {
        assertParseFailure(parser, " /course CS2103T, /sem AY26/27-S1",
                "Missing required parameter(s): /id");
        assertParseFailure(parser, " /id A1234567Z /course CS2103T",
                "Missing required parameter(s): /sem");
        assertParseFailure(parser, " /id A1234567Z /sem AY26/27-S1",
                "Missing required parameter(s): /course");
        assertParseFailure(parser, "", "Missing required parameter(s): /id, /course, /sem");
    }

    @Test
    public void parse_emptyParameter_failure() {
        assertParseFailure(parser, " /id  /course CS2103T, /sem AY26/27-S1",
                "Empty parameter value(s): /id");
        assertParseFailure(parser, " /id A1234567Z /course CS2103T /sem    ",
                "Empty parameter value(s): /sem");
        assertParseFailure(parser, " /id A1234567Z /course   /sem AY26/27-S1",
                "Empty parameter value(s): /course");
        assertParseFailure(parser, " /id  /course    /sem", "Empty parameter value(s): /id, /course, /sem");
    }

    @Test
    public void parse_duplicateParameter_failure() {
        assertParseFailure(parser, " /id A1234567Z /id A7654321Z /course CS2103T /sem AY26/27-S1",
                "Multiple values specified for the following single-valued field(s): /id");
        assertParseFailure(parser, " /id A1234567Z /course CS2103T /course CS2103T /sem AY26/27-S1",
                "Multiple values specified for the following single-valued field(s): /course");
        assertParseFailure(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1 /sem AY26/27-S1",
                "Multiple values specified for the following single-valued field(s): /sem");
    }
}
