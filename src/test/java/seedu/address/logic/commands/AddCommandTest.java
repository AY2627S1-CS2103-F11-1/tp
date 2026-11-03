package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalStudents.ALICE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.NotesList;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.progress.CourseProgressOverview;
import seedu.address.model.progress.StudentCourseProgress;
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;
import seedu.address.model.student.TutorialClass;
import seedu.address.testutil.StudentBuilder;

public class AddCommandTest {

    private static StudentCourseEnrolment enrolment(Student student) {
        Id id = student == null ? new Id("A1234567Y") : student.getId();
        return new StudentCourseEnrolment(id, new CourseCode("CS2103T"),
                new Semester("AY26/27-S1"), new TutorialClass("T07"), new NotesList());
    }

    @Test
    public void constructor_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null, List.of(enrolment(null))));
    }

    @Test
    public void constructor_emptyEnrolments_throwsIllegalArgumentException() {
        Student validStudent = new StudentBuilder().build();

        assertThrows(IllegalArgumentException.class,
                "A student must have at least one enrolment.", (
                ) -> new AddCommand(validStudent, List.of()));
    }

    @Test
    public void execute_studentAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingStudentAdded modelStub = new ModelStubAcceptingStudentAdded();
        Student validStudent = new StudentBuilder().build();

        CommandResult commandResult = new AddCommand(validStudent, List.of(enrolment(validStudent))).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validStudent)),
                commandResult.getFeedbackToUser());
        assertEquals(List.of(validStudent), modelStub.studentsAdded);
    }

    @Test
    public void execute_duplicateStudent_throwsCommandException() {
        Student validStudent = new StudentBuilder().build();
        AddCommand addCommand = new AddCommand(validStudent, List.of(enrolment(validStudent)));
        ModelStub modelStub = new ModelStubWithStudent(validStudent);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_STUDENT, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_uninitialisedCourse_throwsCommandException() {
        Student validStudent = new StudentBuilder().build();
        AddCommand addCommand = new AddCommand(validStudent, List.of(enrolment(validStudent)));

        assertThrows(CommandException.class,
                String.format(AddCommand.MESSAGE_COURSE_NOT_FOUND, "CS2103T", "AY26/27-S1"), (
                ) -> addCommand.execute(new ModelStubMissingCourse()));
    }

    @Test
    public void equals() {
        Student alice = new StudentBuilder().withName("Alice").build();
        Student bob = new StudentBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice, List.of(enrolment(alice)));
        AddCommand addBobCommand = new AddCommand(bob, List.of(enrolment(bob)));

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice, List.of(enrolment(alice)));
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));
        assertEquals(addAliceCommand.hashCode(), addAliceCommandCopy.hashCode());

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different student -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));

        // same student with different enrolments -> returns false
        StudentCourseEnrolment differentEnrolment = new StudentCourseEnrolment(
                alice.getId(), new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
                new TutorialClass("T08"), new NotesList());
        assertFalse(addAliceCommand.equals(new AddCommand(alice, List.of(differentEnrolment))));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE, List.of(enrolment(ALICE)));
        String expected = AddCommand.class.getCanonicalName() + "{student=" + ALICE
                + ", initialEnrolments=" + List.of(enrolment(ALICE)) + "}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * A default model stub that has all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addStudent(Student student) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasStudent(Student student) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteStudent(Student target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setStudent(Student target, Student editedStudent) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasDuplicateStudent(Student otherStudent, Student originalStudent) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasCourse(Course course) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasEnrolment(StudentCourseEnrolment enrolment) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Optional<Course> getCourse(CourseCode courseCode, Semester semester) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public List<Student> getStudentsEnrolledIn(CourseCode courseCode, Semester semester) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Optional<CourseProgressOverview> getCourseProgressOverview(
                CourseCode courseCode, Semester semester) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addCourse(Course course) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setStudentCourseProgresses(List<StudentCourseProgress> studentCourseProgresses) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setStudentCourseProgress(StudentCourseProgress target, StudentCourseProgress updatedProgress) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void removeEnrolment(Id studentId, CourseCode courseCode, Semester semester) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setEnrolment(StudentCourseEnrolment target, StudentCourseEnrolment editedEnrolment) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addEnrolment(StudentCourseEnrolment enrolment) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addStudentWithEnrolments(Student student,
                List<StudentCourseEnrolment> enrolments) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addStudentWithEnrolment(Student student, StudentCourseEnrolment enrolment) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Student> getFilteredStudentList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<StudentCourseProgress> getStudentCourseProgressList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredStudentList(Predicate<Student> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Optional<Student> getStudentById(Id id) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single student.
     */
    private class ModelStubWithStudent extends ModelStub {
        private final Student student;

        ModelStubWithStudent(Student student) {
            requireNonNull(student);
            this.student = student;
        }

        @Override
        public boolean hasStudent(Student student) {
            requireNonNull(student);
            return this.student.isSameStudent(student);
        }
    }

    /**
     * A Model stub that always accepts the student being added.
     */
    private class ModelStubAcceptingStudentAdded extends ModelStub {
        final ArrayList<Student> studentsAdded = new ArrayList<>();

        @Override
        public boolean hasStudent(Student student) {
            requireNonNull(student);
            return studentsAdded.stream().anyMatch(student::isSameStudent);
        }

        @Override
        public Optional<Course> getCourse(CourseCode courseCode, Semester semester) {
            return Optional.of(new Course(courseCode, semester, List.of(new Deliverable("MVP"))));
        }

        @Override
        public void addStudent(Student student) {
            requireNonNull(student);
            studentsAdded.add(student);
        }

        @Override
        public void addStudentWithEnrolments(Student student,
                List<StudentCourseEnrolment> enrolments) {
            requireNonNull(student);
            studentsAdded.add(student);
        }

        @Override
        public void updateFilteredStudentList(Predicate<Student> predicate) {
            requireNonNull(predicate);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /** A model stub that reports that the enrolment course is not initialised. */
    private class ModelStubMissingCourse extends ModelStub {
        @Override
        public boolean hasStudent(Student student) {
            return false;
        }

        @Override
        public Optional<Course> getCourse(CourseCode courseCode, Semester semester) {
            return Optional.empty();
        }
    }

}
