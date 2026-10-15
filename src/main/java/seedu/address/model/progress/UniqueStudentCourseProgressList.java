package seedu.address.model.progress;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.progress.exceptions.DuplicateStudentCourseProgressException;
import seedu.address.model.student.Id;

/**
 * A list of progress records with at most one record for each student and course-semester pair.
 */
public class UniqueStudentCourseProgressList implements Iterable<StudentCourseProgress> {

    private final ObservableList<StudentCourseProgress> internalList = FXCollections.observableArrayList();
    private final ObservableList<StudentCourseProgress> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

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

    /** Removes every progress record that belongs to {@code studentId}. */
    public void removeForStudent(Id studentId) {
        requireNonNull(studentId);
        internalList.removeIf(progress -> progress.getStudentId().equals(studentId));
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
