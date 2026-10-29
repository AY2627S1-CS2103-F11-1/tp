package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.progress.CompletionStatus;
import seedu.address.model.progress.StudentCourseProgress;

/** Tests whether a student has an enrolment matching the selected class context. */
public class StudentMatchesEnrolmentPredicate implements Predicate<Student> {

    private final List<StudentCourseEnrolment> enrolments;
    private final List<StudentCourseProgress> progressRecords;
    private final CourseCode courseCode;
    private final Semester semester;
    private final Optional<TutorialClass> tutorialClass;
    private final Optional<Deliverable> incompleteDeliverable;

    /** Creates a predicate for a course-semester and an optional tutorial class. */
    public StudentMatchesEnrolmentPredicate(List<StudentCourseEnrolment> enrolments,
            CourseCode courseCode, Semester semester, Optional<TutorialClass> tutorialClass) {
        this(enrolments, List.of(), courseCode, semester, tutorialClass, Optional.empty());
    }

    /** Creates a predicate with an optional incomplete-deliverable filter. */
    public StudentMatchesEnrolmentPredicate(List<StudentCourseEnrolment> enrolments,
            List<StudentCourseProgress> progressRecords, CourseCode courseCode, Semester semester,
            Optional<TutorialClass> tutorialClass, Optional<Deliverable> incompleteDeliverable) {
        requireAllNonNull(enrolments, courseCode, semester, tutorialClass);
        enrolments.forEach(Objects::requireNonNull);
        requireAllNonNull(progressRecords, incompleteDeliverable);
        progressRecords.forEach(Objects::requireNonNull);
        this.enrolments = List.copyOf(enrolments);
        this.progressRecords = List.copyOf(progressRecords);
        this.courseCode = courseCode;
        this.semester = semester;
        this.tutorialClass = tutorialClass;
        this.incompleteDeliverable = incompleteDeliverable;
    }

    @Override
    public boolean test(Student student) {
        requireNonNull(student);
        boolean hasMatchingEnrolment = enrolments.stream().anyMatch(enrolment -> enrolment.isFor(
                        student.getId(), courseCode, semester)
                && tutorialClass.map(enrolment.getTutorialClass()::equals).orElse(true));
        if (!hasMatchingEnrolment || incompleteDeliverable.isEmpty()) {
            return hasMatchingEnrolment;
        }
        Deliverable deliverable = incompleteDeliverable.orElseThrow();
        return progressRecords.stream()
                .filter(progress -> progress.isFor(student.getId(), courseCode, semester))
                .anyMatch(progress -> progress.getDeliverableStatuses().get(deliverable)
                        == CompletionStatus.NOT_DONE);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof StudentMatchesEnrolmentPredicate otherPredicate
                && enrolments.equals(otherPredicate.enrolments)
                && progressRecords.equals(otherPredicate.progressRecords)
                && courseCode.equals(otherPredicate.courseCode)
                && semester.equals(otherPredicate.semester)
                && tutorialClass.equals(otherPredicate.tutorialClass)
                && incompleteDeliverable.equals(otherPredicate.incompleteDeliverable));
    }

    @Override
    public int hashCode() {
        return Objects.hash(enrolments, progressRecords, courseCode, semester,
                tutorialClass, incompleteDeliverable);
    }
}
