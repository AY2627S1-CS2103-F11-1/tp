package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.CLASS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.CLASS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_CLASS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ID_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NOTES_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.ID_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ID_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.NOTES_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.NOTES_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTES_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTES_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalStudents.AMY;
import static seedu.address.testutil.TypicalStudents.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.student.TutorialClass;
import seedu.address.model.student.Email;
import seedu.address.model.student.Name;
import seedu.address.model.student.Student;
import seedu.address.model.student.Id;
import seedu.address.model.student.Notes;
import seedu.address.testutil.StudentBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Student expectedStudent = new StudentBuilder(BOB).withNotes(VALID_NOTES_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + ID_DESC_BOB + EMAIL_DESC_BOB
                + CLASS_DESC_BOB + NOTES_DESC_FRIEND, new AddCommand(expectedStudent));


        // multiple tags - all accepted
        Student expectedStudentMultipleTags = new StudentBuilder(BOB).withNotes(VALID_NOTES_FRIEND, VALID_NOTES_HUSBAND)
                .build();
        assertParseSuccess(parser,
                NAME_DESC_BOB + ID_DESC_BOB + EMAIL_DESC_BOB + CLASS_DESC_BOB + NOTES_DESC_HUSBAND + NOTES_DESC_FRIEND,
                new AddCommand(expectedStudentMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedStudentString = NAME_DESC_BOB + ID_DESC_BOB + EMAIL_DESC_BOB
                + CLASS_DESC_BOB + NOTES_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedStudentString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple ids
        assertParseFailure(parser, ID_DESC_AMY + validExpectedStudentString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ID));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedStudentString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple tutorialClasses
        assertParseFailure(parser, CLASS_DESC_AMY + validExpectedStudentString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CLASS));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedStudentString + ID_DESC_AMY + EMAIL_DESC_AMY + NAME_DESC_AMY + CLASS_DESC_AMY
                        + validExpectedStudentString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_CLASS, PREFIX_EMAIL, PREFIX_ID));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedStudentString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedStudentString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid id
        assertParseFailure(parser, INVALID_ID_DESC + validExpectedStudentString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ID));

        // invalid tutorialClass
        assertParseFailure(parser, INVALID_CLASS_DESC + validExpectedStudentString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CLASS));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedStudentString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedStudentString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid id
        assertParseFailure(parser, validExpectedStudentString + INVALID_ID_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ID));

        // invalid tutorialClass
        assertParseFailure(parser, validExpectedStudentString + INVALID_CLASS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CLASS));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Student expectedStudent = new StudentBuilder(AMY).withNotes().build();
        assertParseSuccess(parser, NAME_DESC_AMY + ID_DESC_AMY + EMAIL_DESC_AMY + CLASS_DESC_AMY,
                new AddCommand(expectedStudent));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + ID_DESC_BOB + EMAIL_DESC_BOB + CLASS_DESC_BOB,
                expectedMessage);

        // missing id prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_ID_BOB + EMAIL_DESC_BOB + CLASS_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB + VALID_EMAIL_BOB + CLASS_DESC_BOB,
                expectedMessage);

        // missing tutorialClass prefix
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB + EMAIL_DESC_BOB + VALID_CLASS_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_ID_BOB + VALID_EMAIL_BOB + VALID_CLASS_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + ID_DESC_BOB + EMAIL_DESC_BOB + CLASS_DESC_BOB
                + NOTES_DESC_HUSBAND + NOTES_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid id
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_ID_DESC + EMAIL_DESC_BOB + CLASS_DESC_BOB
                + NOTES_DESC_HUSBAND + NOTES_DESC_FRIEND, Id.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB + INVALID_EMAIL_DESC + CLASS_DESC_BOB
                + NOTES_DESC_HUSBAND + NOTES_DESC_FRIEND, Email.MESSAGE_CONSTRAINTS);

        // invalid tutorialClass
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB + EMAIL_DESC_BOB + INVALID_CLASS_DESC
                + NOTES_DESC_HUSBAND + NOTES_DESC_FRIEND, TutorialClass.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB + EMAIL_DESC_BOB + CLASS_DESC_BOB
                + INVALID_NOTES_DESC + VALID_NOTES_FRIEND, Notes.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + ID_DESC_BOB + EMAIL_DESC_BOB + INVALID_CLASS_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + ID_DESC_BOB + EMAIL_DESC_BOB
                + CLASS_DESC_BOB + NOTES_DESC_HUSBAND + NOTES_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
