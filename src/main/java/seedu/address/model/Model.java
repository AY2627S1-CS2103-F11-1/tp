package seedu.address.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.progress.StudentCourseProgress;
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluates to true */
    Predicate<Student> PREDICATE_SHOW_ALL_STUDENTS = unused -> true;

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a student with the same identity as {@code student} exists in
     * the address book.
     */
    boolean hasStudent(Student student);

    /**
     * Deletes the given student.
     * The student must exist in the address book.
     */
    void deleteStudent(Student target);

    /**
     * Adds the given student.
     * {@code student} must not already exist in the address book.
     */
    void addStudent(Student student);

    /**
     * Replaces the given student {@code target} with {@code editedStudent}.
     * {@code target} must exist in the address book.
     * The student identity of {@code editedStudent} must not be the same as another
     * existing student in the address book.
     */
    void setStudent(Student target, Student editedStudent);

    /**
     * Returns true if a course for the same course code and semester exists.
     */
    boolean hasCourse(Course course);

    /**
     * Adds the given course and its deliverables.
     * The course must not already exist for the same semester.
     */
    void addCourse(Course course);

    /**
     * Replaces all stored student-course progress records.
     */
    void setStudentCourseProgresses(List<StudentCourseProgress> studentCourseProgresses);

    /** Replaces an existing student-course progress record. */
    void setStudentCourseProgress(StudentCourseProgress target, StudentCourseProgress updatedProgress);

    /** Returns an unmodifiable view of the filtered student list */
    ObservableList<Student> getFilteredStudentList();

    /** Returns an unmodifiable view of all student-course progress records. */
    ObservableList<StudentCourseProgress> getStudentCourseProgressList();

    /**
     * Updates the filter of the filtered student list to filter by the given
     * {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredStudentList(Predicate<Student> predicate);

    /** Returns the student with the given id, if present. */
    Optional<Student> getStudentById(Id id);

    /**
     * Returns true if {@code candidate} clashes with any student other than
     * {@code original}.
     */
    boolean hasDuplicateStudent(Student otherStudent, Student currentStudent);

    /**
     * Returns true if the specified student-course enrolment exists.
     */
    boolean hasEnrolment(StudentCourseEnrolment enrolment);

    /**
     * Returns the course matching the specified course code and semester,
     * or an empty Optional if no matching course exists.
     */
    Optional<Course> getCourse(CourseCode courseCode, Semester semester);

    /**
     * Returns students enrolled in the specified course-semester, preserving the
     * main student-list order.
     */
    List<Student> getStudentsEnrolledIn(CourseCode courseCode, Semester semester);

    /**
     * Adds a student with their initial enrolment and deliverable progress.
     * The student must not already exist, and the course must exist.
     */
    void addStudentWithEnrolment(Student student, StudentCourseEnrolment enrolment);

    /**
     * Adds a student with one or more initial course enrolments.
     */
    void addStudentWithEnrolments(
            Student student, List<StudentCourseEnrolment> enrolments);

    /**
     * Adds an enrolment for an existing student and initialises its progress.
     */
    void addEnrolment(StudentCourseEnrolment enrolment);

    /**
     * Replaces an existing enrolment while preserving its identity.
     */
    void setEnrolment(
            StudentCourseEnrolment target,
            StudentCourseEnrolment editedEnrolment);

    /**
     * Removes a student's enrolment and its associated progress.
     * Removes the student if they have no remaining enrolments.
     */
    void removeEnrolment(Id studentId, CourseCode courseCode, Semester semester);
}
