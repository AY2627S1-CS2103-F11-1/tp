package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.progress.StudentCourseProgress;
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Student> filteredStudents;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredStudents = new FilteredList<>(this.addressBook.getStudentList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    // =========== UserPrefs
    // ==================================================================================

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    // =========== AddressBook
    // ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return addressBook.hasStudent(student);
    }

    @Override
    public void deleteStudent(Student target) {
        addressBook.removeStudent(target);
    }

    @Override
    public void addStudent(Student student) {
        addressBook.addStudent(student);
        updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
    }

    @Override
    public void setStudent(Student target, Student editedStudent) {
        requireAllNonNull(target, editedStudent);

        addressBook.setStudent(target, editedStudent);
    }

    @Override
    public boolean hasCourse(Course course) {
        requireNonNull(course);
        return addressBook.hasCourse(course);
    }

    @Override
    public void addCourse(Course course) {
        requireNonNull(course);
        addressBook.addCourse(course);
    }

    @Override
    public void setStudentCourseProgresses(List<StudentCourseProgress> studentCourseProgresses) {
        addressBook.setStudentCourseProgresses(studentCourseProgresses);
    }

    @Override
    public void setStudentCourseProgress(StudentCourseProgress target, StudentCourseProgress updatedProgress) {
        addressBook.setStudentCourseProgress(target, updatedProgress);
    }

    @Override
    public boolean hasEnrolment(StudentCourseEnrolment enrolment) {
        requireNonNull(enrolment);
        return addressBook.hasEnrolment(enrolment);
    }

    @Override
    public Optional<Course> getCourse(CourseCode courseCode, Semester semester) {
        requireAllNonNull(courseCode, semester);
        return addressBook.getCourse(courseCode, semester);
    }

    @Override
    public List<Student> getStudentsEnrolledIn(CourseCode courseCode, Semester semester) {
        requireAllNonNull(courseCode, semester);
        Set<Id> enrolledStudentIds = addressBook.getEnrolmentList().stream()
                .filter(enrolment -> enrolment.getCourseCode().equals(courseCode)
                        && enrolment.getSemester().equals(semester))
                .map(StudentCourseEnrolment::getStudentId)
                .collect(Collectors.toSet());
        return addressBook.getStudentList().stream()
                .filter(student -> enrolledStudentIds.contains(student.getId()))
                .toList();
    }

    @Override
    public void addStudentWithEnrolments(
            Student student, List<StudentCourseEnrolment> enrolments) {
        requireAllNonNull(student, enrolments);
        addressBook.addStudentWithEnrolments(student, enrolments);
    }

    @Override
    public void addStudentWithEnrolment(Student student, StudentCourseEnrolment enrolment) {
        requireAllNonNull(student, enrolment);
        addressBook.addStudentWithEnrolment(student, enrolment);
    }

    @Override
    public void addEnrolment(StudentCourseEnrolment enrolment) {
        requireNonNull(enrolment);
        addressBook.addEnrolment(enrolment);
    }

    @Override
    public void setEnrolment(StudentCourseEnrolment target, StudentCourseEnrolment editedEnrolment) {
        addressBook.setEnrolment(target, editedEnrolment);
    }

    @Override
    public void removeEnrolment(Id studentId, CourseCode courseCode, Semester semester) {
        requireAllNonNull(studentId, courseCode, semester);
        addressBook.removeEnrolment(studentId, courseCode, semester);
        updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
    }

    // =========== Filtered Student List Accessors
    // =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Student} backed by the
     * internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<Student> getFilteredStudentList() {
        return filteredStudents;
    }

    @Override
    public ObservableList<StudentCourseProgress> getStudentCourseProgressList() {
        return addressBook.getStudentCourseProgressList();
    }

    @Override
    public void updateFilteredStudentList(Predicate<Student> predicate) {
        requireNonNull(predicate);
        filteredStudents.setPredicate(predicate);
    }

    @Override
    public Optional<Student> getStudentById(Id id) {
        requireNonNull(id);
        return addressBook.getStudentList().stream()
                .filter(student -> student.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean hasDuplicateStudent(Student otherStudent, Student originalStudent) {
        requireAllNonNull(otherStudent, originalStudent);
        return addressBook.getStudentList().stream()
                .filter(student -> !student.equals(originalStudent))
                .anyMatch(student -> student.isSameStudent(otherStudent));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager otherModelManager)) {
            return false;
        }

        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredStudents.equals(otherModelManager.filteredStudents);
    }

}
