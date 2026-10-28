package seedu.address.logic.parser;

import static seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELIVERABLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.ListCommand;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.student.TutorialClass;

/** Tests the parser for {@link ListCommand}. */
public class ListCommandParserTest {

    private final ListCommandParser parser = new ListCommandParser();

    @Test
    public void parse_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_emptyOrWhitespace_success() {
        assertParseSuccess(parser, "", new ListCommand());
        assertParseSuccess(parser, "   \t\n", new ListCommand());
    }

    @Test
    public void parse_courseAndSemester_success() {
        assertParseSuccess(parser, " /course CS2103T /sem AY26/27-S1",
                new ListCommand(new CourseCode("CS2103T"), new Semester("AY26/27-S1"), Optional.empty()));
    }

    @Test
    public void parse_courseSemesterAndClass_success() {
        assertParseSuccess(parser, " /class T07 /sem AY26/27-S1 /course CS2103T",
                new ListCommand(new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
                        Optional.of(new TutorialClass("T07"))));
    }

    @Test
    public void parse_courseSemesterAndDeliverable_success() {
        assertParseSuccess(parser, " /deliverable v1.3 /sem AY26/27-S1 /course CS2103T",
                new ListCommand(new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
                        Optional.empty(), Optional.of(new Deliverable("v1.3"))));
    }

    @Test
    public void parse_courseSemesterClassAndDeliverable_success() {
        assertParseSuccess(parser, " /class T07 /deliverable MVP /sem AY26/27-S1 /course CS2103T",
                new ListCommand(new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
                        Optional.of(new TutorialClass("T07")), Optional.of(new Deliverable("MVP"))));
    }

    @Test
    public void parse_missingOrEmptyParameters_failure() {
        assertParseFailure(parser, " /course CS2103T", "Missing required parameter(s): /sem");
        assertParseFailure(parser, " /sem AY26/27-S1", "Missing required parameter(s): /course");
        assertParseFailure(parser, " /class T07", ListCommandParser.MESSAGE_CLASS_REQUIRES_COURSE);
        assertParseFailure(parser, " /deliverable MVP",
                ListCommandParser.MESSAGE_DELIVERABLE_REQUIRES_COURSE);
        assertParseFailure(parser, " /course /sem AY26/27-S1", "Empty parameter value(s): /course");
        assertParseFailure(parser, " /course CS2103T /sem AY26/27-S1 /class",
                "Empty parameter value(s): /class");
        assertParseFailure(parser, " /course CS2103T /sem AY26/27-S1 /deliverable",
                "Empty parameter value(s): /deliverable");
    }

    @Test
    public void parse_duplicateParameters_failure() {
        assertParseFailure(parser, " /course CS2103T /course CS2105 /sem AY26/27-S1",
                getErrorMessageForDuplicatePrefixes(PREFIX_COURSE));
        assertParseFailure(parser, " /course CS2103T /sem AY26/27-S1 /sem AY25/26-S2",
                getErrorMessageForDuplicatePrefixes(PREFIX_SEMESTER));
        assertParseFailure(parser, " /course CS2103T /sem AY26/27-S1 /class T07 /class T08",
                getErrorMessageForDuplicatePrefixes(PREFIX_CLASS));
        assertParseFailure(parser,
                " /course CS2103T /sem AY26/27-S1 /deliverable MVP /deliverable Final",
                getErrorMessageForDuplicatePrefixes(PREFIX_DELIVERABLE));
    }

    @Test
    public void parse_unknownOrInvalidParameter_failure() {
        assertParseFailure(parser, " /name Alice",
                String.format(Messages.MESSAGE_UNKNOWN_PARAMETER, "/name"));
        assertParseFailure(parser, " /unknown value",
                String.format(Messages.MESSAGE_UNKNOWN_PARAMETER, "/unknown"));
        assertParseFailure(parser, " /course CS2103T /sem AY26/27-S1 /class Tutorial07",
                TutorialClass.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " /course CS2103T /sem AY26/27-S1 /deliverable MVP,Final",
                ListCommandParser.MESSAGE_SINGLE_DELIVERABLE);
    }

    @Test
    public void parse_unexpectedText_failure() {
        assertParseFailure(parser, "students", ListCommandParser.MESSAGE_UNEXPECTED_TEXT);
        assertParseFailure(parser, "show students", ListCommandParser.MESSAGE_UNEXPECTED_TEXT);
        assertParseFailure(parser, "/", ListCommandParser.MESSAGE_UNEXPECTED_TEXT);
    }
}
