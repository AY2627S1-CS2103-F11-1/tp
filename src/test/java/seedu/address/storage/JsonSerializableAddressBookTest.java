package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.NotesList;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.progress.CompletionStatus;
import seedu.address.model.progress.StudentCourseProgress;
import seedu.address.model.student.TutorialClass;
import seedu.address.testutil.TypicalStudents;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_STUDENTS_FILE = TEST_DATA_FOLDER.resolve("typicalStudentsAddressBook.json");
    private static final Path INVALID_STUDENT_FILE = TEST_DATA_FOLDER.resolve("invalidStudentAddressBook.json");
    private static final Path DUPLICATE_STUDENT_FILE = TEST_DATA_FOLDER.resolve("duplicateStudentAddressBook.json");
    private static final Path MISMATCHED_PROGRESS_FILE = TEST_DATA_FOLDER.resolve("mismatchedProgressAddressBook.json");
    private static final Path INVALID_NOTE_DELIVERABLE_FILE = TEST_DATA_FOLDER
            .resolve("invalidNoteDeliverableAddressBook.json");

    @Test
    public void toModelType_typicalStudentsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_STUDENTS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalStudentsAddressBook = TypicalStudents.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalStudentsAddressBook);
    }

    @Test
    public void toModelType_invalidStudentFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_STUDENT_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateStudents_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_STUDENT_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_STUDENT,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_progressDeliverableNotInCourse_reconcilesProgress() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(MISMATCHED_PROGRESS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBook = dataFromFile.toModelType();
        assertEquals(List.of(new Deliverable("MVP"), new Deliverable("Final")),
                addressBook.getStudentCourseProgressList().getFirst().getDeliverableStatuses().keySet()
                        .stream().toList());
    }

    @Test
    public void toModelType_noteDeliverableNotInCourse_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_NOTE_DELIVERABLE_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateEnrolment_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = readTestFile("duplicateEnrolmentAddressBook.json");
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateProgress_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = readTestFile("duplicateProgressAddressBook.json");
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_enrolmentReferencesMissingStudent_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = readTestFile("invalidEnrolmentStudentAddressBook.json");
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_enrolmentReferencesMissingCourse_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = readTestFile("invalidEnrolmentCourseAddressBook.json");
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_progressReferencesMissingEnrolment_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = readTestFile("invalidProgressEnrolmentAddressBook.json");
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_progressReferencesMissingCourse_throwsSpecificIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = readTestFile("invalidProgressCourseAddressBook.json");
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_INVALID_PROGRESS_COURSE,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_multipleReconciliationCases_preservesValidStatuses() throws Exception {
        JsonSerializableAddressBook dataFromFile = readTestFile("reconciliationAddressBook.json");
        AddressBook addressBook = dataFromFile.toModelType();

        assertEquals(2, addressBook.getStudentCourseProgressList().size());
        StudentCourseProgress first = addressBook.getStudentCourseProgressList().get(0);
        StudentCourseProgress second = addressBook.getStudentCourseProgressList().get(1);
        assertEquals(CompletionStatus.DONE, first.getDeliverableStatuses().get(new Deliverable("MVP")));
        assertEquals(CompletionStatus.NOT_DONE, first.getDeliverableStatuses().get(new Deliverable("Final")));
        assertEquals(CompletionStatus.NOT_DONE, second.getDeliverableStatuses().get(new Deliverable("Proposal")));
        assertEquals(CompletionStatus.NOT_DONE, second.getDeliverableStatuses().get(new Deliverable("Demo")));
        assertEquals(2, first.getDeliverableStatuses().size());
        assertEquals(2, second.getDeliverableStatuses().size());
        assertEquals(List.of(new Deliverable("MVP"), new Deliverable("Final")),
                first.getDeliverableStatuses().keySet().stream().toList());
        assertEquals(List.of(new Deliverable("Proposal"), new Deliverable("Demo")),
                second.getDeliverableStatuses().keySet().stream().toList());
    }

    private JsonSerializableAddressBook readTestFile(String fileName) throws Exception {
        return JsonUtil.readJsonFile(TEST_DATA_FOLDER.resolve(fileName), JsonSerializableAddressBook.class).get();
    }

    @Test
    public void toModelType_multipleCoursesAndEnrolments_roundTrips() throws Exception {
        AddressBook original = TypicalStudents.getTypicalAddressBook();
        Course secondCourse = new Course(new CourseCode("IS2108"), new Semester("AY26/27-S1"),
                java.util.List.of(new Deliverable("Project")));
        original.addCourse(secondCourse);
        original.addEnrolment(new StudentCourseEnrolment(
                TypicalStudents.ALICE.getId(), secondCourse.getCourseCode(), secondCourse.getSemester(),
                new TutorialClass("T08"), new NotesList()));

        AddressBook restored = new JsonSerializableAddressBook(original).toModelType();
        assertEquals(original, restored);
    }

    @Test
    public void toModelType_studentWithoutEnrolment_roundTrips() throws Exception {
        AddressBook original = new AddressBook();
        original.addStudent(TypicalStudents.ALICE);

        AddressBook restored = new JsonSerializableAddressBook(original).toModelType();
        assertEquals(original, restored);
    }

}
