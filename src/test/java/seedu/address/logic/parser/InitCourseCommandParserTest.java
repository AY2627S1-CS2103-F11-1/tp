package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.InitCourseCommand;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;

public class InitCourseCommandParserTest {
    private final InitCourseCommandParser parser = new InitCourseCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Course course = new Course(new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
                List.of(new Deliverable("v1.2"), new Deliverable("MVP")));
        assertParseSuccess(parser, "course /course CS2103T /sem AY26/27-S1 /task v1.2, MVP",
                new InitCourseCommand(course));
    }

    @Test
    public void parse_missingRequiredPrefix_failure() {
        assertParseFailure(parser, "course /course CS2103T /sem AY26/27-S1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, InitCourseCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidSubcommand_failure() {
        assertParseFailure(parser, "student /course CS2103T /sem AY26/27-S1 /task v1.2",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, InitCourseCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_blankDeliverable_failure() {
        assertParseFailure(parser, "course /course CS2103T /sem AY26/27-S1 /task v1.2, ",
                "Deliverable name must not be blank.");
    }

    @Test
    public void parse_duplicateDeliverable_failure() {
        assertParseFailure(parser, "course /course CS2103T /sem AY26/27-S1 /task v1.2, V1.2",
                "A course must contain at least one unique deliverable.");
    }
}
