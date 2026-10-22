package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.CLASS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.CLASS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.ID_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ID_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_CLASS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ID_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalStudents.AMY;
import static seedu.address.testutil.TypicalStudents.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.student.Email;
import seedu.address.model.student.Id;
import seedu.address.model.student.Name;
import seedu.address.model.student.Student;
import seedu.address.testutil.StudentBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Student expectedStudent = new StudentBuilder(BOB).withNotes().build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + ID_DESC_BOB + EMAIL_DESC_BOB
                + CLASS_DESC_BOB, new AddCommand(expectedStudent, java.util.List.of()));

    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedStudentString = NAME_DESC_BOB + ID_DESC_BOB + EMAIL_DESC_BOB
                + CLASS_DESC_BOB;

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
        // no optional fields
        Student expectedStudent = new StudentBuilder(AMY).withNotes().build();
        assertParseSuccess(parser, NAME_DESC_AMY + ID_DESC_AMY + EMAIL_DESC_AMY + CLASS_DESC_AMY,
                new AddCommand(expectedStudent, java.util.List.of()));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + ID_DESC_BOB + EMAIL_DESC_BOB + CLASS_DESC_BOB,
                AddCommandParser.MESSAGE_UNEXPECTED_TEXT);

        // missing id prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_ID_BOB + EMAIL_DESC_BOB + CLASS_DESC_BOB,
                "Missing required parameter(s): /id");

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB + VALID_EMAIL_BOB + CLASS_DESC_BOB,
                "Missing required parameter(s): /email");

        // missing tutorialClass prefix
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB + EMAIL_DESC_BOB + VALID_CLASS_BOB,
                "Missing required parameter(s): /class");

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_ID_BOB + VALID_EMAIL_BOB + VALID_CLASS_BOB,
                AddCommandParser.MESSAGE_UNEXPECTED_TEXT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + ID_DESC_BOB + EMAIL_DESC_BOB + CLASS_DESC_BOB,
                Name.MESSAGE_CONSTRAINTS);

        // invalid id
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_ID_DESC + EMAIL_DESC_BOB + CLASS_DESC_BOB,
                Id.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB + INVALID_EMAIL_DESC + CLASS_DESC_BOB,
                Email.MESSAGE_CONSTRAINTS);

        // invalid tutorialClass
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB + EMAIL_DESC_BOB + INVALID_CLASS_DESC,
                "Empty parameter value(s): /class");

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + ID_DESC_BOB + EMAIL_DESC_BOB + INVALID_CLASS_DESC,
                "Empty parameter value(s): /class");

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + ID_DESC_BOB + EMAIL_DESC_BOB
                + CLASS_DESC_BOB,
                AddCommandParser.MESSAGE_UNEXPECTED_TEXT);
    }

    @Test
    public void parse_missingParameters_failure() {
        assertParseFailure(parser,
                " /name Alice /id A0123456X",
                "Missing required parameter(s): /email, /class");
    }

    @Test
    public void parse_emptyEmail_failure() {
        assertParseFailure(parser,
                " /name Alice /id A0123456X /email /class T01",
                "Empty parameter value(s): /email");
    }

    @Test
    public void parse_allParametersMissing_failure() {
        assertParseFailure(parser,
                "",
                "Missing required parameter(s): /name, /id, /email, /class");
    }

    @Test
    public void parse_missingAndEmptyParameters_failure() {
        assertParseFailure(parser,
                " /name /id A0123456X",
                "Missing required parameter(s): /email, /class\nEmpty parameter value(s): /name");
    }

    @Test
    public void parse_multipleEmptyParameters_failure() {
        assertParseFailure(parser,
                " /name /id A0123456X /email /class T01",
                "Empty parameter value(s): /name, /email");
    }

    @Test
    public void parse_unknownParameter_failure() {
        assertParseFailure(parser,
                " /name Alice /id A0123456X /email alice@u.nus.edu /class T01 /phone 12345678",
                AddCommandParser.MESSAGE_UNKNOWN_PARAMETER);
    }

}
