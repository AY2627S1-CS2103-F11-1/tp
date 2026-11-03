package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BENSON;
import static seedu.address.testutil.TypicalStudents.getTypicalAddressBook;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.course.exceptions.DuplicateCourseException;
import seedu.address.model.enrolment.Notes;
import seedu.address.model.enrolment.NotesList;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.enrolment.exceptions.DuplicateStudentCourseEnrolmentException;
import seedu.address.model.enrolment.exceptions.StudentCourseEnrolmentNotFoundException;
import seedu.address.model.progress.StudentCourseProgress;
import seedu.address.model.student.Student;
import seedu.address.model.student.TutorialClass;
import seedu.address.model.student.exceptions.DuplicateStudentException;
import seedu.address.testutil.StudentBuilder;

public class AddressBookTest {

    private static final Course CS2103 = new Course(new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
            List.of(new Deliverable("v1.2"), new Deliverable("MVP")));

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(List.of(), addressBook.getStudentList());
        assertEquals(List.of(), addressBook.getCourseList());
        assertEquals(List.of(), addressBook.getStudentCourseProgressList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicateStudents_throwsDuplicateStudentException() {
        // Two students with the same identity fields
        Student editedAlice = new StudentBuilder(ALICE).build();
        List<Student> newStudents = List.of(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newStudents);

        assertThrows(DuplicateStudentException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasStudent_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasStudent(null));
    }

    @Test
    public void hasStudent_studentNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasStudent(ALICE));
    }

    @Test
    public void hasStudent_studentInAddressBook_returnsTrue() {
        addressBook.addStudent(ALICE);
        assertTrue(addressBook.hasStudent(ALICE));
    }

    @Test
    public void hasStudent_studentWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addStudent(ALICE);
        Student editedAlice = new StudentBuilder(ALICE).build();
        assertTrue(addressBook.hasStudent(editedAlice));
    }

    @Test
    public void getStudentList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getStudentList().remove(0));
    }

    @Test
    public void hasCourse_nullCourse_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasCourse(null));
    }

    @Test
    public void hasCourse_courseNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasCourse(CS2103));
    }

    @Test
    public void hasCourse_courseInAddressBook_returnsTrue() {
        addressBook.addCourse(CS2103);
        assertTrue(addressBook.hasCourse(CS2103));
    }

    @Test
    public void addCourse_duplicateCourseSemester_throwsDuplicateCourseException() {
        addressBook.addCourse(CS2103);
        Course sameCourseSemester = new Course(new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
                List.of(new Deliverable("PE")));
        assertThrows(DuplicateCourseException.class, () -> addressBook.addCourse(sameCourseSemester));
    }

    @Test
    public void addCourse_courseAndSemesterDifferOnlyByCase_throwsDuplicateCourseException() {
        addressBook.addCourse(CS2103);
        Course sameCourseSemester = new Course(new CourseCode("cs2103t"), new Semester("ay26/27-s1"),
                List.of(new Deliverable("PE")));
        assertThrows(DuplicateCourseException.class, () -> addressBook.addCourse(sameCourseSemester));
    }

    @Test
    public void addStudent_existingCourse_initialisesProgress() {
        addressBook.addCourse(CS2103);

        addressBook.addStudent(ALICE);

        assertTrue(addressBook.getStudentCourseProgressList().isEmpty());
    }

    @Test
    public void addCourse_existingStudent_initialisesProgress() {
        addressBook.addStudent(ALICE);

        addressBook.addCourse(CS2103);

        assertTrue(addressBook.getStudentCourseProgressList().isEmpty());
    }

    @Test
    public void setStudentCourseProgress_identityChange_throwsIllegalArgumentException() {
        Course secondCourse = new Course(new CourseCode("IS2108"), new Semester("AY26/27-S1"),
                List.of(new Deliverable("Project")));
        addressBook.addCourse(CS2103);
        addressBook.addCourse(secondCourse);
        addressBook.addStudentWithEnrolment(ALICE, new StudentCourseEnrolment(
                ALICE.getId(), CS2103.getCourseCode(), CS2103.getSemester(),
                new TutorialClass("T07"), new seedu.address.model.enrolment.NotesList()));

        StudentCourseProgress target = addressBook.getStudentCourseProgressList().getFirst();
        StudentCourseProgress differentIdentity = new StudentCourseProgress(ALICE.getId(), secondCourse);

        assertThrows(IllegalArgumentException.class, () -> addressBook
                .setStudentCourseProgress(target, differentIdentity));
    }

    @Test
    public void setStudentCourseProgresses_withoutEnrolment_throwsIllegalArgumentException() {
        addressBook.addCourse(CS2103);
        StudentCourseProgress progress = new StudentCourseProgress(ALICE.getId(), CS2103);

        assertThrows(IllegalArgumentException.class, () -> addressBook
                .setStudentCourseProgresses(List.of(progress)));
    }

    @Test
    public void addEnrolment_missingStudent_throwsIllegalArgumentException() {
        StudentCourseEnrolment enrolment = enrolment(ALICE, CS2103);
        addressBook.addCourse(CS2103);

        assertThrows(IllegalArgumentException.class, () -> addressBook.addEnrolment(enrolment));
    }

    @Test
    public void addEnrolment_missingCourse_throwsIllegalArgumentException() {
        addressBook.addStudent(ALICE);
        StudentCourseEnrolment enrolment = enrolment(ALICE, CS2103);

        assertThrows(IllegalArgumentException.class, () -> addressBook.addEnrolment(enrolment));
    }

    @Test
    public void addEnrolment_duplicate_throwsDuplicateStudentCourseEnrolmentException() {
        addressBook.addCourse(CS2103);
        addressBook.addStudentWithEnrolment(ALICE, enrolment(ALICE, CS2103));

        assertThrows(DuplicateStudentCourseEnrolmentException.class, () -> addressBook
                .addEnrolment(enrolment(ALICE, CS2103)));
    }

    @Test
    public void addStudentWithEnrolments_emptyList_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> addressBook
                .addStudentWithEnrolments(ALICE, List.of()));
    }

    @Test
    public void addStudentWithEnrolments_mismatchedStudent_throwsIllegalArgumentException() {
        addressBook.addCourse(CS2103);
        StudentCourseEnrolment enrolment = enrolment(BENSON, CS2103);

        assertThrows(IllegalArgumentException.class, () -> addressBook
                .addStudentWithEnrolments(ALICE, List.of(enrolment)));
    }

    @Test
    public void setEnrolment_identityChange_throwsIllegalArgumentException() {
        addressBook.addCourse(CS2103);
        addressBook.addStudentWithEnrolment(ALICE, enrolment(ALICE, CS2103));
        StudentCourseEnrolment target = addressBook.getEnrolmentList().getFirst();
        StudentCourseEnrolment changed = target.withStudentId(BENSON.getId());

        assertThrows(IllegalArgumentException.class, () -> addressBook.setEnrolment(target, changed));
    }

    @Test
    public void removeEnrolment_missingEnrolment_throwsNotFoundException() {
        assertThrows(StudentCourseEnrolmentNotFoundException.class, () -> addressBook
                .removeEnrolment(ALICE.getId(), CS2103.getCourseCode(), CS2103.getSemester()));
    }

    @Test
    public void setEnrolments_invalidReferences_throwsIllegalArgumentException() {
        StudentCourseEnrolment enrolment = enrolment(ALICE, CS2103);

        assertThrows(IllegalArgumentException.class, () -> addressBook.setEnrolments(List.of(enrolment)));
    }

    @Test
    public void setEnrolments_invalidNoteDeliverable_throwsIllegalArgumentException() {
        addressBook.addStudent(ALICE);
        addressBook.addCourse(CS2103);
        StudentCourseEnrolment enrolment = new StudentCourseEnrolment(ALICE.getId(), CS2103.getCourseCode(),
                CS2103.getSemester(), new TutorialClass("T07"),
                new NotesList(new Notes("Unrelated note", "Unknown")));

        assertThrows(IllegalArgumentException.class, () -> addressBook.setEnrolments(List.of(enrolment)));
    }

    @Test
    public void removeStudent_removesStudentProgress() {
        addressBook.addStudent(ALICE);
        addressBook.addCourse(CS2103);

        addressBook.removeStudent(ALICE);

        assertTrue(addressBook.getStudentCourseProgressList().isEmpty());
    }

    @Test
    public void getCourseList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getCourseList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{students=" + addressBook.getStudentList()
                + ", courses=" + addressBook.getCourseList() + ", enrolments="
                + addressBook.getEnrolmentList() + ", studentCourseProgresses="
                + addressBook.getStudentCourseProgressList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    @Test
    public void equals_sameData_areEqual() {
        AddressBook copy = getTypicalAddressBook();

        assertEquals(copy, new AddressBook(copy));
        assertEquals(copy.hashCode(), new AddressBook(copy).hashCode());
        assertFalse(copy.equals(new Object()));
    }

    private static StudentCourseEnrolment enrolment(Student student, Course course) {
        return new StudentCourseEnrolment(student.getId(), course.getCourseCode(), course.getSemester(),
                new TutorialClass("T07"), new NotesList());
    }

    /**
     * A stub ReadOnlyAddressBook whose students list can violate interface
     * constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Student> students = FXCollections.observableArrayList();

        AddressBookStub(Collection<Student> students) {
            this.students.setAll(students);
        }

        @Override
        public ObservableList<Student> getStudentList() {
            return students;
        }

        @Override
        public ObservableList<Course> getCourseList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public ObservableList<StudentCourseProgress> getStudentCourseProgressList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public ObservableList<seedu.address.model.enrolment.StudentCourseEnrolment> getEnrolmentList() {
            return FXCollections.observableArrayList();
        }
    }

}
