package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TutorialClassTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TutorialClass(null));
    }

    @Test
    public void constructor_invalidTutorialClass_throwsIllegalArgumentException() {
        String invalidTutorialClass = "";
        assertThrows(IllegalArgumentException.class, () -> new TutorialClass(invalidTutorialClass));
    }

    @Test
    public void isValidClass() {
        // null tutorialClass
        assertThrows(NullPointerException.class, () -> TutorialClass.isValidClass(null));

        // invalid tutorialClasses
        assertFalse(TutorialClass.isValidClass("")); // empty string
        assertFalse(TutorialClass.isValidClass(" ")); // spaces only

        // valid tutorialClasses
        assertTrue(TutorialClass.isValidClass("Blk 456, Den Road, #01-355"));
        assertTrue(TutorialClass.isValidClass("-")); // one character
        assertTrue(TutorialClass.isValidClass("Leng Inc; 1234 Market St; San Francisco CA 2349879; USA")); // long
                                                                                                           // tutorialClass
    }

    @Test
    public void equals() {
        TutorialClass tutorialClass = new TutorialClass("Valid TutorialClass");

        // same values -> returns true
        assertTrue(tutorialClass.equals(new TutorialClass("Valid TutorialClass")));

        // same object -> returns true
        assertTrue(tutorialClass.equals(tutorialClass));

        // null -> returns false
        assertFalse(tutorialClass.equals(null));

        // different types -> returns false
        assertFalse(tutorialClass.equals(5.0f));

        // different values -> returns false
        assertFalse(tutorialClass.equals(new TutorialClass("Other Valid TutorialClass")));
    }
}
