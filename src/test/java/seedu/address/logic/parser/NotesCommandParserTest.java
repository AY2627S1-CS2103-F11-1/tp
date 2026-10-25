package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.NotesCommand;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.Notes;
import seedu.address.model.student.Id;

/** Tests for {@code NotesCommandParser}. */
public class NotesCommandParserTest {

    private final NotesCommandParser parser = new NotesCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        assertParseSuccess(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1"
                        + " /task MVP /notes Implemented inheritance",
                new NotesCommand(new Id("A1234567Z"), new CourseCode("CS2103T"),
                        new Semester("AY26/27-S1"), new Notes("Implemented inheritance", "MVP")));
    }

    @Test
    public void parse_structuredParameterAfterNotes_failure() {
        assertParseFailure(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1"
                        + " /task MVP /notes Discussed /course usage",
                NotesCommandParser.MESSAGE_PARAMETERS_AFTER_NOTES);
        assertParseFailure(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1"
                        + " /task MVP /notes Discussed /unknown value",
                NotesCommandParser.MESSAGE_PARAMETERS_AFTER_NOTES);
    }

    @Test
    public void parse_missingRequiredField_failure() {
        assertParseFailure(parser, " notes /id A1234567Z",
                NotesCommandParser.MESSAGE_MISSING_NOTES);
        assertParseFailure(parser, " notes /id A1234567Z /notes Implemented inheritance",
                NotesCommandParser.MESSAGE_UNEXPECTED_TEXT);
        assertParseFailure(parser, " notes /id A1234567Z /task Level 1",
                NotesCommandParser.MESSAGE_MISSING_NOTES);
        assertParseFailure(parser, " notes /notes Implemented inheritance /task Level 1",
                NotesCommandParser.MESSAGE_UNEXPECTED_TEXT);
    }

    @Test
    public void parse_blankNotes_failure() {
        assertParseFailure(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1 /task MVP /notes",
                NotesCommandParser.MESSAGE_EMPTY_NOTES);
        assertParseFailure(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1 /task MVP /notes    ",
                NotesCommandParser.MESSAGE_EMPTY_NOTES);
    }

    @Test
    public void parse_blankTask_failure() {
        assertParseFailure(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1 /task /notes Used inheritance",
                "Empty parameter value(s): /task");
    }

    @Test
    public void parse_noteExceedingMaximumLength_failure() {
        String notesOverMaximumLength = "a".repeat(Notes.MAX_LENGTH + 1);

        assertParseFailure(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1 /task MVP /notes "
                + notesOverMaximumLength,
                Notes.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_unicodeNotes_success() {
        assertParseSuccess(parser, " /id A1234567Z /course CS2103T /sem AY26/27-S1"
                        + " /task MVP /notes Reviewed 👍 and discussed 测试",
                new NotesCommand(new Id("A1234567Z"), new CourseCode("CS2103T"),
                        new Semester("AY26/27-S1"), new Notes("Reviewed 👍 and discussed 测试", "MVP")));
    }

    @Test
    public void parse_duplicatePrefix_failure() {
        assertParseFailure(parser, " /id A1234567Z /id A7654321Z /notes Discussed inheritance",
                "Multiple values specified for the following single-valued field(s): /id");
        assertParseFailure(parser, " /ID A1234567Z /course CS2103T /sem AY26/27-S1"
                        + " /task MVP /notes Discussed inheritance",
                "Unknown parameter: /ID");
    }
}
