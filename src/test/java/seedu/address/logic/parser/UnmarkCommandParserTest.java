package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UnmarkCommand;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.student.Id;

/** Tests for {@code UnmarkCommandParser}. */
public class UnmarkCommandParserTest {

    private final UnmarkCommandParser parser = new UnmarkCommandParser();

    @Test
    public void parse_validId_returnsUnmarkCommand() {
        assertParseSuccess(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1 /task MVP, v1.2",
                new UnmarkCommand(new Id("A1234567Z"), new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
                        List.of(new Deliverable("MVP"), new Deliverable("v1.2"))));
    }

    @Test
    public void parse_validName_returnsUnmarkCommand() {
        assertParseSuccess(parser, " /name John Doe /course CS2103T /sem AY26/27-S1 /task MVP",
                new UnmarkCommand("John Doe", new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
                        List.of(new Deliverable("MVP"))));
    }

    @Test
    public void parse_missingStudent_throwsParseException() {
        assertParseFailure(parser, " /course CS2103T /sem AY26/27-S1 /task MVP",
                StudentProgressCommandParser.MESSAGE_MISSING_STUDENT);
    }

    @Test
    public void parse_bothStudentIdentifiers_throwsParseException() {
        assertParseFailure(parser, " /id A1234567Z /name John Doe /course CS2103T /sem AY26/27-S1 /task MVP",
                StudentProgressCommandParser.MESSAGE_BOTH_IDENTIFIERS);
    }

    @Test
    public void parse_unknownParameter_throwsParseException() {
        assertParseFailure(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1 /task MVP /class T07",
                StudentProgressCommandParser.MESSAGE_UNKNOWN_PARAMETER);
    }

    @Test
    public void parse_emptyTask_throwsParseException() {
        assertParseFailure(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1 /task",
                StudentProgressCommandParser.MESSAGE_EMPTY_PARAMETER);
    }
}
