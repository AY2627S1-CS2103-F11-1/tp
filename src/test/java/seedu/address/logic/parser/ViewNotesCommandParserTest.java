package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ViewNotesCommand;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.student.Id;

/** Tests for {@code ViewNotesCommandParser}. */
public class ViewNotesCommandParserTest {

    private final ViewNotesCommandParser parser = new ViewNotesCommandParser();

    @Test
    public void parse_validId_success() {
        assertParseSuccess(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1",
                new ViewNotesCommand(new Id("A1234567Z"), new CourseCode("CS2103T"),
                        new Semester("AY26/27-S1")));
    }

    @Test
    public void parse_missingOrDuplicateId_failure() {
        assertParseFailure(parser, "", "Missing required parameter(s): /id, /course, /sem");
        assertParseFailure(parser, " /id A1234567Z /id A7654321Z /course CS2103T /sem AY26/27-S1",
                "Multiple values specified for the following single-valued field(s): /id");
    }
}
