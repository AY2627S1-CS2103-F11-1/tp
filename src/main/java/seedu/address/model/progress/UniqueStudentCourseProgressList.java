package seedu.address.model.progress;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.progress.exceptions.DuplicateStudentCourseProgressException;
import seedu.address.model.progress.exceptions.StudentCourseProgressNotFoundException;
import seedu.address.model.student.Id;

/**
 * A list of progress records with at most one record for each student and
 * course-semester pair.
 */
public class UniqueStudentCourseProgressList implements Iterable<StudentCourseProgress> {

    private final ObservableList<StudentCourseProgress> internalList = FXCollections.observableArrayList();
    private final ObservableList<StudentCourseProgress> internalUnmodifiableList = FXCollections
            .unmodifiableObservableList(internalList);

    /** Returns whether an equivalent progress record already exists. */
    public boolean contains(StudentCourseProgress progress) {
        requireNonNull(progress);
        return internalList.stream().anyMatch(existing -> existing.isFor(
                progress.getStudentId(), progress.getCourseCode(), progress.getSemester()));
    }

    /** Adds a progress record that does not already exist. */
    public void add(StudentCourseProgress progress) {
        requireNonNull(progress);
        if (contains(progress)) {
            throw new DuplicateStudentCourseProgressException();
        }
        internalList.add(progress);
    }

    /** Replaces all progress records with the supplied unique records. */
    public void setProgresses(List<StudentCourseProgress> progresses) {
        requireAllNonNull(progresses);
        if (!areUnique(progresses)) {
            throw new DuplicateStudentCourseProgressException();
        }
        internalList.setAll(progresses);
    }

    /**
     * Replaces an existing progress record with an updated record for the same
     * identity.
     */
    public void set(StudentCourseProgress target, StudentCourseProgress updatedProgress) {
        requireAllNonNull(target, updatedProgress);
        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new StudentCourseProgressNotFoundException();
        }
        if (!target.isFor(updatedProgress.getStudentId(), updatedProgress.getCourseCode(),
                updatedProgress.getSemester()) && contains(updatedProgress)) {
            throw new DuplicateStudentCourseProgressException();
        }
        internalList.set(index, updatedProgress);
    }

    /** Removes every progress record that belongs to {@code studentId}. */
    public void removeForStudent(Id studentId) {
        requireNonNull(studentId);
        internalList.removeIf(progress -> progress.getStudentId().equals(studentId));
    }

    /**
     * Removes progress belonging to the specified student and course-semester.
     */
    public void removeForStudentCourse(
            Id studentId, CourseCode courseCode, Semester semester) {
        requireAllNonNull(studentId, courseCode, semester);

        internalList.removeIf(progress -> progress.isFor(studentId, courseCode, semester));
    }

    /**
     * Updates the student ID of all progress records belonging to {@code oldId},
     * preserving their completion statuses.
     */
    public void updateStudentId(Id oldId, Id newId) {
        requireAllNonNull(oldId, newId);

        if (oldId.equals(newId)) {
            return;
        }

        List<StudentCourseProgress> updatedProgresses = internalList.stream()
                .map(progress -> progress.getStudentId().equals(oldId)
                        ? progress.withStudentId(newId)
                        : progress)
                .toList();

        setProgresses(updatedProgresses);
    }

    /** Returns the backing list as an unmodifiable observable list. */
    public ObservableList<StudentCourseProgress> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<StudentCourseProgress> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        return other instanceof UniqueStudentCourseProgressList otherList
                && internalList.equals(otherList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    private boolean areUnique(List<StudentCourseProgress> progresses) {
        for (int i = 0; i < progresses.size() - 1; i++) {
            for (int j = i + 1; j < progresses.size(); j++) {
                StudentCourseProgress first = progresses.get(i);
                StudentCourseProgress second = progresses.get(j);
                if (first.isFor(second.getStudentId(), second.getCourseCode(), second.getSemester())) {
                    return false;
                }
            }
        }
        return true;
    }
}
