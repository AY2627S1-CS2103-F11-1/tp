package seedu.address.logic.parser;

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
        assertParseSuccess(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1 /task MVP",
                new UnmarkCommand(new Id("A1234567Z"), new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
                        List.of(new Deliverable("MVP"))));
    }
}
