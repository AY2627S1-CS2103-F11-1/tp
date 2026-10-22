package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.NotesCommand;
import seedu.address.model.student.Id;
import seedu.address.model.student.Notes;

/** Tests for {@code NotesCommandParser}. */
public class NotesCommandParserTest {

    private final NotesCommandParser parser = new NotesCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        assertParseSuccess(parser, " /id A1234567Z /notes Implemented inheritance /task Level 1",
                new NotesCommand(new Id("A1234567Z"), new Notes("Implemented inheritance", "Level 1")));
    }

    @Test
    public void parse_missingRequiredField_failure() {
        assertParseFailure(parser, " notes /id A1234567Z",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, NotesCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " notes /id A1234567Z /notes Implemented inheritance",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, NotesCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " notes /id A1234567Z /task Level 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, NotesCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " notes /notes Implemented inheritance /task Level 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, NotesCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_blankNotes_failure() {
        assertParseFailure(parser, " /id A1234567Z /notes /task Level 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        NotesCommand.INVALID_NOTES + '\n' + NotesCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_blankTask_failure() {
        assertParseFailure(parser, " /id A1234567Z /notes Used inheritance /task",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        NotesCommand.INVALID_NOTES_TASK + '\n' + NotesCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noteExceedingMaximumLength_failure() {
        String notesOverMaximumLength = "a".repeat(Notes.MAX_LENGTH + 1);

        assertParseFailure(parser, " /id A1234567Z /notes " + notesOverMaximumLength
                + " /task Level 1", Notes.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_duplicatePrefix_failure() {
        assertParseFailure(parser, " /id A1234567Z /id A7654321Z /notes Discussed inheritance",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, NotesCommand.MESSAGE_USAGE));
    }
}
