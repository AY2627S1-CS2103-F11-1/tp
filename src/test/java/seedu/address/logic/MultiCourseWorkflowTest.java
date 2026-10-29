package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.progress.CompletionStatus;
import seedu.address.model.student.Id;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

/** End-to-end regression tests for the multi-course command and storage workflow. */
public class MultiCourseWorkflowTest {

    private static final String SEMESTER = "AY26/27-S1";
    private static final String ORIGINAL_ID = "A1234567Z";
    private static final String UPDATED_ID = "A7654321B";

    @TempDir
    public Path temporaryFolder;

    private Path dataFile;
    private Model model;
    private Logic logic;

    @BeforeEach
    public void setUp() {
        dataFile = temporaryFolder.resolve("addressBook.json");
        model = new ModelManager();
        StorageManager storage = new StorageManager(
                new JsonAddressBookStorage(dataFile),
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json")));
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_completeMultiCourseWorkflow_preservesRelatedData() throws Exception {
        initialiseCoursesAndStudent();

        execute("notes /id " + ORIGINAL_ID + " /course CS2103T /sem " + SEMESTER
                + " /deliverable v1.4 /notes Reviewed inheritance design");
        execute("notes /id " + ORIGINAL_ID + " /course CS2103T /sem " + SEMESTER
                + " /deliverable v1.5 /notes Follow up on exception handling");
        execute("notes /id " + ORIGINAL_ID + " /course CS2105 /sem " + SEMESTER
                + " /deliverable Assignment 1 /notes Check socket implementation");
        CommandResult notesResult = execute("view notes /id " + ORIGINAL_ID
                + " /course CS2103T /sem " + SEMESTER);
        assertTrue(notesResult.getFeedbackToUser().contains("v1.4: Reviewed inheritance design"));
        assertTrue(notesResult.getFeedbackToUser().contains("v1.5: Follow up on exception handling"));
        CommandResult secondCourseNotes = execute("view notes /id " + ORIGINAL_ID
                + " /course CS2105 /sem " + SEMESTER);
        assertTrue(secondCourseNotes.getFeedbackToUser().contains("Assignment 1: Check socket implementation"));

        execute("mark /id " + ORIGINAL_ID + " /course CS2103T /sem " + SEMESTER + " /deliverable v1.4");
        execute("mark /id " + ORIGINAL_ID + " /course CS2105 /sem " + SEMESTER + " /deliverable Assignment 1");
        execute("unmark /id " + ORIGINAL_ID + " /course CS2103T /sem " + SEMESTER + " /deliverable v1.4");
        CommandResult viewResult = execute("view /id " + ORIGINAL_ID);
        assertTrue(viewResult.getStudentDeliverablesView().isPresent());
        assertEquals(2, viewResult.getStudentDeliverablesView().orElseThrow().getCourseProgresses().size());

        execute("edit /id " + ORIGINAL_ID + " /newid " + UPDATED_ID);
        Id updatedId = new Id(UPDATED_ID);
        assertEquals(1, model.getAddressBook().getStudentList().size());
        assertEquals(updatedId, model.getAddressBook().getStudentList().getFirst().getId());
        assertTrue(model.getAddressBook().getEnrolmentList().stream()
                .allMatch(enrolment -> enrolment.getStudentId().equals(updatedId)));
        assertTrue(model.getStudentCourseProgressList().stream()
                .allMatch(progress -> progress.getStudentId().equals(updatedId)));

        var cs2105Progress = model.getStudentCourseProgressList().stream()
                .filter(progress -> progress.getCourseCode().equals(new CourseCode("CS2105")))
                .findFirst()
                .orElseThrow();
        assertEquals(CompletionStatus.DONE, cs2105Progress.getStatus(new Deliverable("Assignment 1")));

        JsonAddressBookStorage storage = new JsonAddressBookStorage(dataFile);
        AddressBook reloaded = new AddressBook(storage.readAddressBook().orElseThrow());
        assertEquals(model.getAddressBook(), reloaded);

        execute("delete notes /id " + UPDATED_ID + " /course CS2103T /sem " + SEMESTER);
        assertTrue(model.getAddressBook().getEnrolmentList().stream()
                .filter(enrolment -> enrolment.getCourseCode().equals(new CourseCode("CS2103T")))
                .findFirst().orElseThrow().getNotes().isEmpty());
        assertFalse(model.getAddressBook().getEnrolmentList().stream()
                .filter(enrolment -> enrolment.getCourseCode().equals(new CourseCode("CS2105")))
                .findFirst().orElseThrow().getNotes().isEmpty());

        execute("unenrol /id " + UPDATED_ID + " /course CS2105 /sem " + SEMESTER);
        assertEquals(1, model.getAddressBook().getStudentList().size());
        assertEquals(1, model.getAddressBook().getEnrolmentList().size());
        assertEquals(1, model.getStudentCourseProgressList().size());

        execute("unenrol /id " + UPDATED_ID + " /course CS2103T /sem " + SEMESTER);
        assertTrue(model.getAddressBook().getStudentList().isEmpty());
        assertTrue(model.getAddressBook().getEnrolmentList().isEmpty());
        assertTrue(model.getStudentCourseProgressList().isEmpty());
    }

    @Test
    public void execute_invalidDomainCommand_leavesModelAndFileUnchanged() throws Exception {
        initialiseCoursesAndStudent();
        AddressBook expectedModel = new AddressBook(model.getAddressBook());
        String expectedFile = Files.readString(dataFile);

        assertThrows(CommandException.class, () -> execute("mark /id " + ORIGINAL_ID
                + " /course CS2103T /sem " + SEMESTER + " /deliverable Final Demo"));

        assertEquals(expectedModel, model.getAddressBook());
        assertEquals(expectedFile, Files.readString(dataFile));
    }

    @Test
    public void execute_invalidSyntax_leavesModelAndFileUnchanged() throws Exception {
        initialiseCoursesAndStudent();
        AddressBook expectedModel = new AddressBook(model.getAddressBook());
        String expectedFile = Files.readString(dataFile);

        assertThrows(ParseException.class, () -> execute("enrol /id " + ORIGINAL_ID
                + " /course CS2105 /course CS2103T /sem " + SEMESTER + " /class T03"));

        assertEquals(expectedModel, model.getAddressBook());
        assertEquals(expectedFile, Files.readString(dataFile));
    }

    @Test
    public void execute_deleteStudent_removesAllDependentRecords() throws Exception {
        initialiseCoursesAndStudent();

        execute("delete /id " + ORIGINAL_ID);

        assertTrue(model.getAddressBook().getStudentList().isEmpty());
        assertTrue(model.getAddressBook().getEnrolmentList().isEmpty());
        assertTrue(model.getStudentCourseProgressList().isEmpty());
        AddressBook reloaded = new AddressBook(new JsonAddressBookStorage(dataFile)
                .readAddressBook().orElseThrow());
        assertEquals(model.getAddressBook(), reloaded);
    }

    private void initialiseCoursesAndStudent() throws CommandException, ParseException {
        execute("init course /course CS2103T /sem " + SEMESTER + " /deliverable v1.4, v1.5");
        execute("init course /course CS2105 /sem " + SEMESTER + " /deliverable Assignment 1, Assignment 2");
        execute("add /name Alex Tan /id " + ORIGINAL_ID
                + " /email alex@u.nus.edu /course CS2103T /sem " + SEMESTER + " /class T01");
        execute("enrol /id " + ORIGINAL_ID + " /course CS2105 /sem " + SEMESTER + " /class T02");
        assertFalse(model.getAddressBook().getStudentList().isEmpty());
    }

    private CommandResult execute(String command) throws CommandException, ParseException {
        return logic.execute(command);
    }
}
