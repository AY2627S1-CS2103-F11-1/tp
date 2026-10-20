
package seedu.address.model.enrolment;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.exceptions.DuplicateStudentCourseEnrolmentException;
import seedu.address.model.enrolment.exceptions.StudentCourseEnrolmentNotFoundException;
import seedu.address.model.student.Id;

/**
 * A list of student-course enrolments that enforces uniqueness and disallows
 * null elements.
 *
 * Two enrolments are considered duplicates if they have the same student ID,
 * course code and semester, regardless of their tutorial class or project
 * notes.
 *
 * Supports adding, replacing and removing enrolments.
 *
 * @see StudentCourseEnrolment#isSameEnrolment(StudentCourseEnrolment)
 */
public class UniqueStudentCourseEnrolment implements Iterable<StudentCourseEnrolment> {

    private final ObservableList<StudentCourseEnrolment> internalList = FXCollections.observableArrayList();
    private final ObservableList<StudentCourseEnrolment> internalUnmodifiableList = FXCollections
            .unmodifiableObservableList(internalList);

    /**
     * Returns true if an enrolment with the same identity as {@code toCheck}
     * exists.
     */
    public boolean contains(StudentCourseEnrolment toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameEnrolment);
    }

    /**
     * Adds {@code enrolment} to the list.
     * The enrolment must not already exist in the list.
     *
     * @throws DuplicateStudentCourseEnrolmentException if a duplicate enrolment
     *                                                  exists.
     */
    public void add(StudentCourseEnrolment enrolment) {
        requireNonNull(enrolment);

        if (contains(enrolment)) {
            throw new DuplicateStudentCourseEnrolmentException();
        }

        internalList.add(enrolment);
    }

    /**
     * Replaces {@code target} with {@code editedEnrolment}.
     * The target must exist, and the replacement must not conflict with another
     * enrolment.
     *
     * @throws StudentCourseEnrolmentNotFoundException  if the target does not
     *                                                  exist.
     * @throws DuplicateStudentCourseEnrolmentException if the replacement has a
     *                                                  duplicate identity.
     */
    public void setEnrolment(StudentCourseEnrolment target, StudentCourseEnrolment editedEnrolment) {
        requireAllNonNull(target, editedEnrolment);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new StudentCourseEnrolmentNotFoundException();
        }

        if (!target.isSameEnrolment(editedEnrolment) && contains(editedEnrolment)) {
            throw new DuplicateStudentCourseEnrolmentException();
        }

        internalList.set(index, editedEnrolment);
    }

    /**
     * Removes the enrolment identified by {@code studentId}, {@code courseCode} and
     * {@code semester}.
     *
     * @throws StudentCourseEnrolmentNotFoundException if no matching enrolment
     *                                                 exists.
     */
    public void removeForStudentCourse(Id studentId, CourseCode courseCode, Semester semester) {
        requireAllNonNull(studentId, courseCode, semester);

        boolean removed = internalList.removeIf(
                enrolment -> enrolment.isFor(studentId, courseCode, semester));

        if (!removed) {
            throw new StudentCourseEnrolmentNotFoundException();
        }
    }

    /**
     * Removes all enrolments belonging to {@code studentId}.
     */
    public void removeForStudent(Id studentId) {
        requireNonNull(studentId);

        internalList.removeIf(enrolment -> enrolment.getStudentId().equals(studentId));
    }

    /**
     * Updates the student ID of all enrolments belonging to {@code oldId},
     * preserving their course, semester, tutorial class and project notes.
     *
     * @throws DuplicateStudentCourseEnrolmentException if updating the ID
     *                                                  would create duplicate
     *                                                  enrolments.
     */
    public void updateStudentId(Id oldId, Id newId) {
        requireAllNonNull(oldId, newId);

        if (oldId.equals(newId)) {
            return;
        }

        List<StudentCourseEnrolment> updatedEnrolments = internalList.stream()
                .map(enrolment -> enrolment.getStudentId().equals(oldId)
                        ? enrolment.withStudentId(newId)
                        : enrolment)
                .toList();

        setEnrolments(updatedEnrolments);
    }

    /**
     * Replaces all enrolments with those in {@code replacement}.
     */
    public void setEnrolments(UniqueStudentCourseEnrolment replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces all enrolments with {@code enrolments}.
     * The supplied list must contain no null or duplicate enrolments.
     *
     * @throws DuplicateStudentCourseEnrolmentException if duplicate enrolments are
     *                                                  found.
     */
    public void setEnrolments(List<StudentCourseEnrolment> enrolments) {
        requireNonNull(enrolments);
        enrolments.forEach(Objects::requireNonNull);

        if (!enrolmentsAreUnique(enrolments)) {
            throw new DuplicateStudentCourseEnrolmentException();
        }

        internalList.setAll(enrolments);


    }

    /**
     * Returns an unmodifiable observable view of the enrolment list.
     */
    public ObservableList<StudentCourseEnrolment> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    /**
     * Returns an iterator that does not support modifying the enrolment list.
     */
    @Override
    public Iterator<StudentCourseEnrolment> iterator() {
        return internalUnmodifiableList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UniqueStudentCourseEnrolment otherUniqueEnrolment)) {
            return false;
        }

        return internalList.equals(otherUniqueEnrolment.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code enrolments} contains no duplicate enrolment
     * identities.
     */
    private boolean enrolmentsAreUnique(List<StudentCourseEnrolment> enrolments) {
        for (int i = 0; i < enrolments.size() - 1; i++) {
            for (int j = i + 1; j < enrolments.size(); j++) {
                if (enrolments.get(i).isSameEnrolment(enrolments.get(j))) {
                    return false;
                }
            }
        }

        return true;
    }
}
