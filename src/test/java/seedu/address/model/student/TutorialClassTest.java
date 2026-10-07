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
        assertTrue(TutorialClass.isValidClass("T07"));
        assertTrue(TutorialClass.isValidClass("F10"));
        assertTrue(TutorialClass.isValidClass("7"));
    }

    @Test
    public void equals() {
        TutorialClass tutorialClass = new TutorialClass("T07");

        // same values -> returns true
        assertTrue(tutorialClass.equals(new TutorialClass("T07")));

        // same object -> returns true
        assertTrue(tutorialClass.equals(tutorialClass));

        // null -> returns false
        assertFalse(tutorialClass.equals(null));

        // different types -> returns false
        assertFalse(tutorialClass.equals(5.0f));

        // different values -> returns false
        assertFalse(tutorialClass.equals(new TutorialClass("F10")));
    }
}
