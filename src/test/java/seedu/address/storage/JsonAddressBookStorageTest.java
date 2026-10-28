package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.HOON;
import static seedu.address.testutil.TypicalStudents.IDA;
import static seedu.address.testutil.TypicalStudents.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.NotesList;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.progress.CompletionStatus;
import seedu.address.model.progress.StudentCourseProgress;
import seedu.address.model.student.TutorialClass;

public class JsonAddressBookStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonAddressBookStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readAddressBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readAddressBook(null));
    }

    private java.util.Optional<ReadOnlyAddressBook> readAddressBook(String filePath) throws Exception {
        return new JsonAddressBookStorage(Paths.get(filePath)).readAddressBook(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readAddressBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_emptyJsonObject_returnsEmptyAddressBook() throws Exception {
        Path filePath = testFolder.resolve("emptyAddressBook.json");
        Files.writeString(filePath, "{}");

        ReadOnlyAddressBook addressBook = new JsonAddressBookStorage(filePath)
                .readAddressBook(filePath).orElseThrow();

        assertTrue(addressBook.getStudentList().isEmpty());
        assertTrue(addressBook.getCourseList().isEmpty());
        assertTrue(addressBook.getEnrolmentList().isEmpty());
        assertTrue(addressBook.getStudentCourseProgressList().isEmpty());
    }

    @Test
    public void read_zeroByteFile_throwsDataLoadingException() throws Exception {
        Path filePath = testFolder.resolve("zeroByteAddressBook.json");
        Files.createFile(filePath);

        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        assertThrows(DataLoadingException.class, () -> storage.readAddressBook(filePath));
    }

    @Test
    public void read_nullJsonFile_throwsDataLoadingExceptionWithoutOverwriting() throws Exception {
        Path filePath = testFolder.resolve("nullAddressBook.json");
        String originalContent = "null";
        Files.writeString(filePath, originalContent);

        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        assertThrows(DataLoadingException.class, () -> storage.readAddressBook(filePath));

        assertEquals(originalContent, Files.readString(filePath));
    }

    @Test
    public void read_whitespaceOnlyFile_throwsDataLoadingExceptionWithoutOverwriting() throws Exception {
        Path filePath = testFolder.resolve("whitespaceAddressBook.json");
        String originalContent = "   \n\t";
        Files.writeString(filePath, originalContent);

        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        assertThrows(DataLoadingException.class, () -> storage.readAddressBook(filePath));

        assertEquals(originalContent, Files.readString(filePath));
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("notJsonFormatAddressBook.json"));
    }

    @Test
    public void readAddressBook_invalidStudentAddressBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("invalidStudentAddressBook.json"));
    }

    @Test
    public void readAddressBook_invalidAndValidStudentAddressBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("invalidAndValidStudentAddressBook.json"));
    }

    @Test
    public void readAddressBook_legacyStudentFormat_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("legacyStudentAddressBook.json"));
    }

    @Test
    public void readAddressBook_mismatchedProgressDeliverable_repairsAndSaves() throws Exception {
        Path filePath = testFolder.resolve("mismatchedProgressAddressBook.json");
        Files.copy(TEST_DATA_FOLDER.resolve("mismatchedProgressAddressBook.json"), filePath);

        new JsonAddressBookStorage(filePath).readAddressBook();

        String savedContent = Files.readString(filePath);
        assertTrue(savedContent.contains("\"MVP\" : \"NOT_DONE\""));
        assertFalse(savedContent.contains("\"ABC\""));
    }

    @Test
    public void readAddressBook_invalidNoteDeliverable_rejectsWithoutOverwriting() throws Exception {
        Path filePath = testFolder.resolve("invalidNoteDeliverableAddressBook.json");
        Path sourcePath = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest",
                "invalidNoteDeliverableAddressBook.json");
        Files.copy(sourcePath, filePath);
        String originalContent = Files.readString(filePath);

        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        assertThrows(DataLoadingException.class, storage::readAddressBook);

        assertEquals(originalContent, Files.readString(filePath));
    }

    @Test
    public void readAddressBook_invalidReferences_rejectWithoutOverwriting() throws Exception {
        String[] fileNames = {
                "duplicateEnrolmentAddressBook.json",
                "duplicateProgressAddressBook.json",
                "invalidEnrolmentStudentAddressBook.json",
                "invalidEnrolmentCourseAddressBook.json",
                "invalidProgressEnrolmentAddressBook.json",
                "invalidProgressCourseAddressBook.json",
            "invalidNoteAndProgressAddressBook.json",
            "invalidCompletionStatusAddressBook.json"
        };

        for (String fileName : fileNames) {
            Path filePath = testFolder.resolve(fileName);
            Path sourcePath = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest", fileName);
            Files.copy(sourcePath, filePath);
            String originalContent = Files.readString(filePath);

            JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
            assertThrows(DataLoadingException.class, storage::readAddressBook);
            assertEquals(originalContent, Files.readString(filePath));
        }
    }

    @Test
    public void readAddressBook_multipleReconciliationCases_repairsAndSaves() throws Exception {
        Path sourcePath = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest",
                "reconciliationAddressBook.json");
        Path filePath = testFolder.resolve("reconciliationAddressBook.json");
        Files.copy(sourcePath, filePath);

        new JsonAddressBookStorage(filePath).readAddressBook();

        String savedContent = Files.readString(filePath);
        assertTrue(savedContent.contains("\"MVP\" : \"DONE\""));
        assertTrue(savedContent.contains("\"Final\" : \"NOT_DONE\""));
        assertTrue(savedContent.contains("\"Proposal\" : \"NOT_DONE\""));
        assertTrue(savedContent.contains("\"Demo\" : \"NOT_DONE\""));
        assertFalse(savedContent.contains("\"Obsolete\""));
        assertFalse(savedContent.contains("\"Old\""));
        assertFalse(savedContent.contains("\"mvp\""));
    }

    @Test
    public void readAddressBook_caseOnlyProgressDifference_doesNotRewriteFile() throws Exception {
        Path sourcePath = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest",
                "caseOnlyProgressAddressBook.json");
        Path filePath = testFolder.resolve("caseOnlyProgressAddressBook.json");
        Files.copy(sourcePath, filePath);
        String originalContent = Files.readString(filePath);

        new JsonAddressBookStorage(filePath).readAddressBook();

        assertEquals(originalContent, Files.readString(filePath));
    }

    @Test
    public void readAddressBook_consistentData_doesNotRewriteFile() throws Exception {
        Path filePath = testFolder.resolve("consistentAddressBook.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        storage.saveAddressBook(getTypicalAddressBook(), filePath);
        String originalContent = Files.readString(filePath);

        storage.readAddressBook(filePath);

        assertEquals(originalContent, Files.readString(filePath));
    }

    @Test
    public void readAndSaveAddressBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempAddressBook.json");
        AddressBook original = getTypicalAddressBook();
        JsonAddressBookStorage jsonAddressBookStorage = new JsonAddressBookStorage(filePath);

        // Save in new file and read back
        jsonAddressBookStorage.saveAddressBook(original, filePath);
        ReadOnlyAddressBook readBack = jsonAddressBookStorage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        // Modify data, overwrite existing file, and read back
        original.addStudentWithEnrolment(HOON, new StudentCourseEnrolment(HOON.getId(),
                new CourseCode("CS2103T"), new Semester("AY26/27-S1"), new TutorialClass("T07"), new NotesList()));
        original.removeStudent(ALICE);
        jsonAddressBookStorage.saveAddressBook(original, filePath);
        readBack = jsonAddressBookStorage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        // Save and read without specifying file path
        original.addStudentWithEnrolment(IDA, new StudentCourseEnrolment(IDA.getId(),
                new CourseCode("CS2103T"), new Semester("AY26/27-S1"), new TutorialClass("T07"), new NotesList()));
        jsonAddressBookStorage.saveAddressBook(original); // file path not specified
        readBack = jsonAddressBookStorage.readAddressBook().get(); // file path not specified
        assertEquals(original, new AddressBook(readBack));

    }

    @Test
    public void readAndSaveAddressBook_withStudentCourseProgress_success() throws Exception {
        Path filePath = testFolder.resolve("ProgressAddressBook.json");
        AddressBook original = new AddressBook();
        Course course = new Course(new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
                List.of(new Deliverable("MVP")));
        original.addCourse(course);
        original.addStudentWithEnrolment(ALICE, new StudentCourseEnrolment(ALICE.getId(),
                course.getCourseCode(), course.getSemester(), new TutorialClass("T07"), new NotesList()));

        StudentCourseProgress completedProgress = original.getStudentCourseProgressList().getFirst()
                .withStatus(new Deliverable("MVP"), CompletionStatus.DONE);
        original.setStudentCourseProgresses(List.of(completedProgress));

        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        storage.saveAddressBook(original, filePath);
        ReadOnlyAddressBook readBack = storage.readAddressBook(filePath).get();

        assertEquals(original, new AddressBook(readBack));
    }

    @Test
    public void saveAddressBook_nullAddressBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAddressBook(null, "SomeFile.json"));
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveAddressBook(ReadOnlyAddressBook addressBook, String filePath) {
        try {
            new JsonAddressBookStorage(Paths.get(filePath))
                    .saveAddressBook(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveAddressBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAddressBook(new AddressBook(), null));
    }
}
