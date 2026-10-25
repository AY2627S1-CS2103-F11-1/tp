package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.StudentCourseEnrolment;

/** Tests whether a student has an enrolment matching the selected class context. */
public class StudentMatchesEnrolmentPredicate implements Predicate<Student> {

    private final List<StudentCourseEnrolment> enrolments;
    private final CourseCode courseCode;
    private final Semester semester;
    private final Optional<TutorialClass> tutorialClass;

    /** Creates a predicate for a course-semester and an optional tutorial class. */
    public StudentMatchesEnrolmentPredicate(List<StudentCourseEnrolment> enrolments,
            CourseCode courseCode, Semester semester, Optional<TutorialClass> tutorialClass) {
        requireAllNonNull(enrolments, courseCode, semester, tutorialClass);
        enrolments.forEach(Objects::requireNonNull);
        this.enrolments = List.copyOf(enrolments);
        this.courseCode = courseCode;
        this.semester = semester;
        this.tutorialClass = tutorialClass;
    }

    @Override
    public boolean test(Student student) {
        requireNonNull(student);
        return enrolments.stream().anyMatch(enrolment -> enrolment.isFor(
                        student.getId(), courseCode, semester)
                && tutorialClass.map(enrolment.getTutorialClass()::equals).orElse(true));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof StudentMatchesEnrolmentPredicate otherPredicate
                && enrolments.equals(otherPredicate.enrolments)
                && courseCode.equals(otherPredicate.courseCode)
                && semester.equals(otherPredicate.semester)
                && tutorialClass.equals(otherPredicate.tutorialClass));
    }

    @Override
    public int hashCode() {
        return Objects.hash(enrolments, courseCode, semester, tutorialClass);
    }
}
