package seedu.address.model.student;





import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class IdTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Id(null));
    }

    @Test
    public void constructor_invalidId_throwsIllegalArgumentException() {
        String invalidId = "";
        assertThrows(IllegalArgumentException.class, () -> new Id(invalidId));
    }

    @Test
    public void isValidId() {
        // null id number
        assertThrows(NullPointerException.class, () -> Id.isValidId(null));

        // invalid id numbers
        assertFalse(Id.isValidId("")); // empty string
        assertFalse(Id.isValidId(" ")); // spaces only
        assertFalse(Id.isValidId("91")); // less than 3 numbers
        assertFalse(Id.isValidId("id")); // non-numeric
        assertFalse(Id.isValidId("9011p041")); // alphabets within digits
        assertFalse(Id.isValidId("9312 1534")); // spaces within digits

        // valid id numbers
        assertTrue(Id.isValidId("911")); // exactly 3 numbers
        assertTrue(Id.isValidId("93121534"));
        assertTrue(Id.isValidId("124293842033123")); // long id numbers
    }

    @Test
    public void equals() {
        Id id = new Id("999");

        // same values -> returns true
        assertTrue(id.equals(new Id("999")));

        // same object -> returns true
        assertTrue(id.equals(id));

        // null -> returns false
        assertFalse(id.equals(null));

        // different types -> returns false
        assertFalse(id.equals(5.0f));

        // different values -> returns false
        assertFalse(id.equals(new Id("995")));
    }
}
