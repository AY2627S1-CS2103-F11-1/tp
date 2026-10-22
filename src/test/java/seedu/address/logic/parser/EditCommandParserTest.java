package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.CLASS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditStudentDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.Email;
import seedu.address.model.student.Id;
import seedu.address.model.student.Name;
import seedu.address.testutil.EditStudentDescriptorBuilder;

/** Unit tests for {@link EditCommandParser}. */
public class EditCommandParserTest {

    private final EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        assertParseFailure(parser, " /name " + VALID_NAME_AMY, EditCommandParser.MESSAGE_MISSING_ID);
        assertParseFailure(parser, " /id " + VALID_ID_AMY, EditCommand.MESSAGE_NOT_EDITED);
        assertParseFailure(parser, "", EditCommandParser.MESSAGE_MISSING_ID);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assertParseFailure(parser, "unexpected /id " + VALID_ID_AMY + NAME_DESC_AMY,
                EditCommandParser.MESSAGE_UNEXPECTED_TEXT);
        assertParseFailure(parser, "/id " + VALID_ID_AMY + " random text" + NAME_DESC_AMY,
                EditCommandParser.MESSAGE_UNEXPECTED_TEXT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, " /id " + VALID_ID_AMY + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " /id " + VALID_ID_AMY + " /newid 911a",
                Id.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " /id " + VALID_ID_AMY + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " /id " + VALID_ID_AMY + " /class",
                String.format(EditCommandParser.MESSAGE_EMPTY_PARAMETERS, "/class"));
    }

    @Test
    public void parse_unknownParameter_failure() {
        assertParseFailure(parser, " /id " + VALID_ID_AMY + " /notes old note",
                EditCommandParser.MESSAGE_UNKNOWN_PARAMETER);
    }

    @Test
    public void parse_duplicateParameter_failure() {
        String duplicateMessage = Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_NAME);
        assertParseFailure(parser, " /id " + VALID_ID_AMY + NAME_DESC_AMY + NAME_DESC_AMY, duplicateMessage);
    }

    @Test
    public void parse_allFieldsSpecified_success() throws ParseException {
        String userInput = " /id " + VALID_ID_AMY + NAME_DESC_AMY + " /newid " + VALID_ID_BOB + EMAIL_DESC_AMY
                + CLASS_DESC_AMY;
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withName(VALID_NAME_AMY)
                .withId(VALID_ID_BOB).withEmail(VALID_EMAIL_AMY).build();

        assertParseSuccess(parser, userInput, new EditCommand(new Id(VALID_ID_AMY), descriptor));
    }

    @Test
    public void parse_someFieldsSpecified_success() throws ParseException {
        String userInput = " /id " + VALID_ID_AMY + EMAIL_DESC_AMY + CLASS_DESC_AMY;
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder()
                .withEmail(VALID_EMAIL_AMY).build();

        assertParseSuccess(parser, userInput, new EditCommand(new Id(VALID_ID_AMY), descriptor));
    }

    @Test
    public void parse_newId_success() throws ParseException {
        String userInput = " /id " + VALID_ID_AMY + " /newid " + VALID_ID_BOB;
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withId(VALID_ID_BOB).build();

        assertParseSuccess(parser, userInput, new EditCommand(new Id(VALID_ID_AMY), descriptor));
    }
}
