package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class TutorialClass {

    public static final String MESSAGE_CONSTRAINTS = "Student class must be 1 to 5 alphanumeric characters (e.g. T07, F10, 7) without spaces.";

    public static final String VALIDATION_REGEX = "[A-Za-z0-9]{1,5}";

    public final String tutorialClass;

    public TutorialClass(String tutorialClass) {
        requireNonNull(tutorialClass);
        checkArgument(isValidClass(tutorialClass), MESSAGE_CONSTRAINTS);
        this.tutorialClass = tutorialClass;
    }

    public static boolean isValidClass(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    
    @Override
    public String toString() {
        return tutorialClass;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TutorialClass otherClass)) {
            return false;
        }

        return tutorialClass.equals(otherClass.tutorialClass);
    }

    @Override
    public int hashCode() {
        return tutorialClass.hashCode();
    }

}
