
package seedu.address.model.enrolment;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.student.Id;
import seedu.address.model.student.Notes;
import seedu.address.model.student.TutorialClass;

/**
 * Represents a student's enrolment in a course for a specified semester.
 * Each enrolment contains course-specific tutorial class and project notes.
 *
 * Enrolments are uniquely identified by student ID, course code and semester.
 */
public class StudentCourseEnrolment {

    private final Id studentId;
    private final CourseCode courseCode;
    private final Semester semester;
    private final TutorialClass tutorialClass;
    private final Notes notes;

    /**
     * Constructs a student-course enrolment with the given details.
     */
    public StudentCourseEnrolment(Id studentId, CourseCode courseCode,
            Semester semester, TutorialClass tutorialClass, Notes notes) {
        requireAllNonNull(studentId, courseCode, semester, tutorialClass, notes);

        this.studentId = studentId;
        this.courseCode = courseCode;
        this.semester = semester;
        this.tutorialClass = tutorialClass;
        this.notes = notes;
    }

    /**
     * Returns a copy with the updated student ID, preserving all other details.
     */
    public StudentCourseEnrolment withStudentId(Id newStudentId) {
        requireAllNonNull(newStudentId);

        return new StudentCourseEnrolment(
                newStudentId, courseCode, semester, tutorialClass, notes);
    }

    /**
     * Returns a copy with the updated tutorial class, preserving all other details.
     */
    public StudentCourseEnrolment withTutorialClass(TutorialClass newTutorialClass) {
        requireAllNonNull(newTutorialClass);

        return new StudentCourseEnrolment(
                studentId, courseCode, semester, newTutorialClass, notes);
    }

    /**
     * Returns a copy with updated project notes, preserving all other details.
     */
    public StudentCourseEnrolment withNotes(Notes newNotes) {
        requireAllNonNull(newNotes);

        return new StudentCourseEnrolment(
                studentId, courseCode, semester, tutorialClass, newNotes);
    }

    public Id getStudentId() {
        return studentId;
    }

    public CourseCode getCourseCode() {
        return courseCode;
    }

    public Semester getSemester() {
        return semester;
    }

    public TutorialClass getTutorialClass() {
        return tutorialClass;
    }

    public Notes getNotes() {
        return notes;
    }

    /**
     * Returns true if this enrolment belongs to the specified
     * student and course-semester.
     */
    public boolean isFor(Id studentId, CourseCode courseCode, Semester semester) {
        requireAllNonNull(studentId, courseCode, semester);

        return this.studentId.equals(studentId)
                && this.courseCode.equals(courseCode)
                && this.semester.equals(semester);
    }

    /**
     * Returns true if both enrolments have the same student ID,
     * course code and semester.
     */
    public boolean isSameEnrolment(StudentCourseEnrolment other) {
        return other != null
                && isFor(other.studentId, other.courseCode, other.semester);
    }

    /**
     * Returns true if both enrolments have the same identity and data fields.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof StudentCourseEnrolment otherEnrolment)) {
            return false;
        }

        return studentId.equals(otherEnrolment.studentId)
                && courseCode.equals(otherEnrolment.courseCode)
                && semester.equals(otherEnrolment.semester)
                && tutorialClass.equals(otherEnrolment.tutorialClass)
                && notes.equals(otherEnrolment.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseCode, semester, tutorialClass, notes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("studentId", studentId)
                .add("courseCode", courseCode)
                .add("semester", semester)
                .add("tutorialClass", tutorialClass)
                .add("notes", notes)
                .toString();
    }
}
