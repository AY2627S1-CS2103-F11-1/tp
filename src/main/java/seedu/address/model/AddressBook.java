package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.course.UniqueCourseList;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.enrolment.UniqueStudentCourseEnrolment;
import seedu.address.model.enrolment.exceptions.DuplicateStudentCourseEnrolmentException;
import seedu.address.model.enrolment.exceptions.StudentCourseEnrolmentNotFoundException;
import seedu.address.model.progress.StudentCourseProgress;
import seedu.address.model.progress.UniqueStudentCourseProgressList;
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;
import seedu.address.model.student.UniqueStudentList;
import seedu.address.model.student.exceptions.DuplicateStudentException;

/**
 * Wraps all data at the address-book level.
 * Duplicates are not allowed (by .isSameStudent comparison).
 */
public class AddressBook implements ReadOnlyAddressBook {

    private static final String MESSAGE_STUDENT_NOT_FOUND = "Student does not exist.";
    private static final String MESSAGE_COURSE_NOT_FOUND = "Course does not exist.";
    private static final String MESSAGE_PROGRESS_EXISTS = "Progress record already exists.";
    private static final String MESSAGE_PROGRESS_FOR_ENROLMENT_EXISTS =
            "Progress record already exists for this enrolment.";
    private static final String MESSAGE_PROGRESS_ID_CHANGE =
            "Progress record identity cannot be changed.";
    private static final String MESSAGE_NO_ENROLMENT = "A student must have at least one enrolment.";
    private static final String MESSAGE_ENROLMENT_STUDENT_MISMATCH = "Enrolment student ID does not match.";
    private static final String MESSAGE_PROGRESS_NOT_FOUND =
            "Progress record for the enrolment does not exist.";
    private static final String MESSAGE_ENROLMENT_ID_CHANGE =
            "Enrolment identity cannot be changed through setEnrolment.";

    private final UniqueStudentList students = new UniqueStudentList();
    private final UniqueCourseList courses = new UniqueCourseList();
    private final UniqueStudentCourseProgressList studentCourseProgresses = new UniqueStudentCourseProgressList();
    private final UniqueStudentCourseEnrolment enrolments = new UniqueStudentCourseEnrolment();

    public AddressBook() {
    }

    /**
     * Creates an AddressBook using the Students in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    // ===== Data replacement operations =====

    /**
     * Replaces the contents of the student list with {@code students}.
     * {@code students} must not contain duplicate students.
     */
    public void setStudents(List<Student> students) {
        this.students.setStudents(students);
    }

    /**
     * Replaces the contents of the course list with {@code courses}.
     * {@code courses} must not contain duplicate course-semester pairs.
     */
    public void setCourses(List<Course> courses) {
        this.courses.setCourses(courses);
    }

    /**
     * Replaces the existing address book data with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        students.setStudents(newData.getStudentList());
        courses.setCourses(newData.getCourseList());
        enrolments.setEnrolments(newData.getEnrolmentList());
        studentCourseProgresses.setProgresses(
                newData.getStudentCourseProgressList());
    }

    // ===== Student operations =====

    /**
     * Returns true if a student with the same identity as {@code student} exists in
     * the address book.
     */
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return students.contains(student);
    }

    /**
     * Adds a student to the address book.
     * The student must not already exist in the address book.
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Replaces {@code target} with {@code editedStudent}.
     * Updates associated enrolment and progress records if the student ID changes.
     * Existing tutorial classes, notes and deliverable statuses are preserved.
     *
     * The target must exist, and the edited student must not conflict with
     * another existing student.
     */
    public void setStudent(Student target, Student editedStudent) {
        requireAllNonNull(target, editedStudent);

        if (target.getId().equals(editedStudent.getId())) {
            students.setStudent(target, editedStudent);
            return;
        }

        // Prepare updated enrolments.
        UniqueStudentCourseEnrolment updatedEnrolments = new UniqueStudentCourseEnrolment();

        updatedEnrolments.setEnrolments(
                enrolments.asUnmodifiableObservableList());

        updatedEnrolments.updateStudentId(
                target.getId(), editedStudent.getId());

        // Prepare updated progress records.
        UniqueStudentCourseProgressList updatedProgresses = new UniqueStudentCourseProgressList();

        updatedProgresses.setProgresses(
                studentCourseProgresses.asUnmodifiableObservableList());

        updatedProgresses.updateStudentId(
                target.getId(), editedStudent.getId());

        // Validate the student replacement before committing related changes.
        UniqueStudentList updatedStudents = new UniqueStudentList();
        updatedStudents.setStudents(students.asUnmodifiableObservableList());
        updatedStudents.setStudent(target, editedStudent);

        // Commit the prepared changes.
        students.setStudents(updatedStudents.asUnmodifiableObservableList());
        enrolments.setEnrolments(updatedEnrolments.asUnmodifiableObservableList());
        studentCourseProgresses.setProgresses(
                updatedProgresses.asUnmodifiableObservableList());
    }

    /**
     * Removes a student and all associated enrolments and progress records.
     */
    public void removeStudent(Student student) {
        requireNonNull(student);

        students.remove(student);
        enrolments.removeForStudent(student.getId());
        studentCourseProgresses.removeForStudent(student.getId());
    }

    // ===== Course operations =====

    /** Returns the course identified by {@code courseCode} and {@code semester}. */
    public Optional<Course> getCourse(CourseCode courseCode, Semester semester) {
        requireAllNonNull(courseCode, semester);

        return courses.asUnmodifiableObservableList().stream()
                .filter(course -> course.getCourseCode().equals(courseCode)
                        && course.getSemester().equals(semester))
                .findFirst();
    }

    /**
     * Returns true if a course with the same course code and semester exists.
     */
    public boolean hasCourse(Course course) {
        requireNonNull(course);
        return courses.contains(course);
    }

    /**
     * Adds a course to the address book.
     * The course must not already exist for the same semester.
     */
    public void addCourse(Course course) {
        courses.add(course);
    }

    // ===== Progress operations =====

    /** Replaces the progress records with the supplied unique records. */
    public void setStudentCourseProgresses(List<StudentCourseProgress> studentCourseProgresses) {
        this.studentCourseProgresses.setProgresses(studentCourseProgresses);
    }

    /** Replaces an existing student-course progress record. */
    public void setStudentCourseProgress(StudentCourseProgress target, StudentCourseProgress updatedProgress) {
        requireAllNonNull(target, updatedProgress);
        if (!target.isFor(updatedProgress.getStudentId(), updatedProgress.getCourseCode(),
                updatedProgress.getSemester())) {
            throw new IllegalArgumentException(MESSAGE_PROGRESS_ID_CHANGE);
        }
        studentCourseProgresses.set(target, updatedProgress);
    }

    // ===== Enrolment operations =====

    /** Returns true if an equivalent enrolment exists. */
    public boolean hasEnrolment(StudentCourseEnrolment enrolment) {
        requireNonNull(enrolment);
        return enrolments.contains(enrolment);
    }

    /**
     * Adds an enrolment and initialises the student's deliverable progress.
     * The student and course-semester must exist.
     *
     * @throws IllegalArgumentException                 if the student or course
     *                                                  does not exist,
     *                                                  or if a progress record
     *                                                  already exists.
     * @throws DuplicateStudentCourseEnrolmentException if the enrolment already
     *                                                  exists.
     */
    public void addEnrolment(StudentCourseEnrolment enrolment) {
        requireNonNull(enrolment);

        boolean studentExists = students.asUnmodifiableObservableList().stream()
                .anyMatch(student -> student.getId().equals(enrolment.getStudentId()));

        if (!studentExists) {
            throw new IllegalArgumentException(MESSAGE_STUDENT_NOT_FOUND);
        }

        Course course = getCourse(enrolment.getCourseCode(), enrolment.getSemester())
                .orElseThrow(() -> new IllegalArgumentException(MESSAGE_COURSE_NOT_FOUND));

        if (enrolments.contains(enrolment)) {
            throw new DuplicateStudentCourseEnrolmentException();
        }

        StudentCourseProgress progress = new StudentCourseProgress(enrolment.getStudentId(), course);

        if (studentCourseProgresses.contains(progress)) {
            throw new IllegalArgumentException(
                    MESSAGE_PROGRESS_FOR_ENROLMENT_EXISTS);
        }

        enrolments.add(enrolment);
        studentCourseProgresses.add(progress);
    }

    /**
     * Adds a student with their initial enrolments and progress records.
     * All enrolments are validated before any records are added.
     *
     * @throws IllegalArgumentException                 if any enrolment is invalid.
     * @throws DuplicateStudentException                if the student already
     *                                                  exists.
     * @throws DuplicateStudentCourseEnrolmentException if enrolments are
     *                                                  duplicated.
     */
    public void addStudentWithEnrolments(Student student,
            List<StudentCourseEnrolment> initialEnrolments) {
        requireAllNonNull(student, initialEnrolments);
        initialEnrolments.forEach(Objects::requireNonNull);

        if (initialEnrolments.isEmpty()) {
            throw new IllegalArgumentException(MESSAGE_NO_ENROLMENT);
        }

        if (hasStudent(student)) {
            throw new DuplicateStudentException();
        }

        List<StudentCourseProgress> newProgresses = new ArrayList<>();

        for (StudentCourseEnrolment enrolment : initialEnrolments) {
            if (!enrolment.getStudentId().equals(student.getId())) {
                throw new IllegalArgumentException(MESSAGE_ENROLMENT_STUDENT_MISMATCH);
            }

            Course course = getCourse(enrolment.getCourseCode(), enrolment.getSemester())
                    .orElseThrow(() -> new IllegalArgumentException(MESSAGE_COURSE_NOT_FOUND));

            if (enrolments.contains(enrolment)) {
                throw new DuplicateStudentCourseEnrolmentException();
            }

            StudentCourseProgress progress = new StudentCourseProgress(student.getId(), course);

            if (studentCourseProgresses.contains(progress)) {
                throw new IllegalArgumentException(MESSAGE_PROGRESS_EXISTS);
            }

            newProgresses.add(progress);
        }

        UniqueStudentList updatedStudents = new UniqueStudentList();
        updatedStudents.setStudents(students.asUnmodifiableObservableList());
        updatedStudents.add(student);

        UniqueStudentCourseEnrolment updatedEnrolments = new UniqueStudentCourseEnrolment();

        List<StudentCourseEnrolment> allEnrolments = Stream.concat(
                enrolments.asUnmodifiableObservableList().stream(),
                initialEnrolments.stream()).toList();

        updatedEnrolments.setEnrolments(allEnrolments);

        UniqueStudentCourseProgressList updatedProgresses = new UniqueStudentCourseProgressList();

        List<StudentCourseProgress> allProgresses = Stream.concat(
                studentCourseProgresses.asUnmodifiableObservableList().stream(),
                newProgresses.stream()).toList();

        updatedProgresses.setProgresses(allProgresses);

        students.setStudents(updatedStudents.asUnmodifiableObservableList());
        enrolments.setEnrolments(updatedEnrolments.asUnmodifiableObservableList());
        studentCourseProgresses.setProgresses(
                updatedProgresses.asUnmodifiableObservableList());
    }

    /**
     * Replaces an enrolment while preserving its identity and progress record.
     *
     * @throws IllegalArgumentException if the enrolment identity changes.
     */
    public void setEnrolment(
            StudentCourseEnrolment target,
            StudentCourseEnrolment editedEnrolment) {
        requireAllNonNull(target, editedEnrolment);

        if (!target.isSameEnrolment(editedEnrolment)) {
            throw new IllegalArgumentException(
                    MESSAGE_ENROLMENT_ID_CHANGE);
        }

        enrolments.setEnrolment(target, editedEnrolment);
    }

    /**
     * Adds a student with one initial course enrolment.
     */
    public void addStudentWithEnrolment(
            Student student, StudentCourseEnrolment enrolment) {
        requireAllNonNull(student, enrolment);
        addStudentWithEnrolments(student, List.of(enrolment));
    }

    /**
     * Removes a student's enrolment and corresponding progress record.
     * Removes the student if no enrolments remain.
     *
     * @throws StudentCourseEnrolmentNotFoundException if the enrolment does not
     *                                                 exist.
     */
    public void removeEnrolment(Id studentId, CourseCode courseCode, Semester semester) {
        requireAllNonNull(studentId, courseCode, semester);

        boolean exists = enrolments.asUnmodifiableObservableList().stream()
                .anyMatch(enrolment -> enrolment.isFor(studentId, courseCode, semester));

        if (!exists) {
            throw new StudentCourseEnrolmentNotFoundException();
        }

        boolean progressExists = studentCourseProgresses.asUnmodifiableObservableList().stream()
                .anyMatch(progress -> progress.isFor(studentId, courseCode, semester));

        if (!progressExists) {
            throw new IllegalStateException(MESSAGE_PROGRESS_NOT_FOUND);
        }

        enrolments.removeForStudentCourse(studentId, courseCode, semester);
        studentCourseProgresses.removeForStudentCourse(studentId, courseCode, semester);

        boolean hasRemainingEnrolments = enrolments.asUnmodifiableObservableList().stream()
                .anyMatch(enrolment -> enrolment.getStudentId().equals(studentId));

        if (!hasRemainingEnrolments) {
            students.asUnmodifiableObservableList().stream()
                    .filter(student -> student.getId().equals(studentId))
                    .findFirst()
                    .ifPresent(students::remove);
        }
    }

    /**
     * Replaces all student-course enrolments with {@code enrolments}.
     * The supplied list must not contain null or duplicate enrolments.
     *
     * @throws DuplicateStudentCourseEnrolmentException if duplicate enrolments
     *                                                  exist.
     */
    public void setEnrolments(List<StudentCourseEnrolment> enrolments) {
        requireNonNull(enrolments);
        this.enrolments.setEnrolments(enrolments);
    }

    /**
     * Returns an unmodifiable view of all student-course enrolments.
     */
    @Override
    public ObservableList<StudentCourseEnrolment> getEnrolmentList() {
        return enrolments.asUnmodifiableObservableList();
    }

    // ===== Read-only views and object methods =====

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("students", students)
                .add("courses", courses)
                .add("enrolments", enrolments)
                .add("studentCourseProgresses", studentCourseProgresses)
                .toString();
    }

    @Override
    public ObservableList<Student> getStudentList() {
        return students.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Course> getCourseList() {
        return courses.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<StudentCourseProgress> getStudentCourseProgressList() {
        return studentCourseProgresses.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddressBook otherAddressBook)) {
            return false;
        }

        return students.equals(otherAddressBook.students)
                && courses.equals(otherAddressBook.courses)
                && enrolments.equals(otherAddressBook.enrolments)
                && studentCourseProgresses.equals(otherAddressBook.studentCourseProgresses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(students, courses, enrolments, studentCourseProgresses);
    }

}
