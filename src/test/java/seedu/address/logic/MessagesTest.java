package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalStudents.ALICE;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.Prefix;

/** Tests the public formatting helpers in {@link Messages}. */
public class MessagesTest {

    @Test
    public void format_validStudent_returnsUserVisibleDetails() {
        assertEquals("Alice Pauline; ID: A1234567Y; Email: alice@u.nus.edu",
                Messages.format(ALICE));
    }

    @Test
    public void getErrorMessageForDuplicatePrefixes_returnsAllDuplicatePrefixes() {
        Prefix idPrefix = new Prefix("/id");
        Prefix coursePrefix = new Prefix("/course");

        String message = Messages.getErrorMessageForDuplicatePrefixes(
                idPrefix, coursePrefix, idPrefix);

        assertTrue(message.startsWith(Messages.MESSAGE_DUPLICATE_FIELDS));
        assertTrue(message.contains("/id"));
        assertTrue(message.contains("/course"));
    }

    @Test
    public void getErrorMessageForDuplicatePrefixes_emptyInput_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> Messages.getErrorMessageForDuplicatePrefixes());
    }
}
