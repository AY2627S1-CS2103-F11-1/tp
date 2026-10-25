package seedu.address.storage;






import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BENSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;

/** Tests the JSON adapter for students. */
public class JsonAdaptedStudentTest {

    @Test
    public void toModelType_validStudent_returnsStudent() throws Exception {
        JsonAdaptedStudent adaptedStudent = new JsonAdaptedStudent(ALICE);
        assertEquals(ALICE, adaptedStudent.toModelType());
    }

    @Test
    public void toModelType_studentWithoutNotes_returnsStudentWithoutNotes() throws Exception {
        JsonAdaptedStudent adaptedStudent = new JsonAdaptedStudent(BENSON);
        assertEquals(BENSON, adaptedStudent.toModelType());
    }

    @Test
    public void toModelType_missingRequiredField_throwsIllegalValueException() {
        JsonAdaptedStudent adaptedStudent = new JsonAdaptedStudent(null, "A1234567Y",
                "alice@u.nus.edu");
        assertThrows(IllegalValueException.class, adaptedStudent::toModelType);
    }
}
