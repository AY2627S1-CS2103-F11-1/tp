
package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Id;
import seedu.address.model.student.Notes;
import seedu.address.model.student.TutorialClass;

/**
 * Jackson-friendly version of {@link StudentCourseEnrolment}.
 */
class JsonAdaptedStudentCourseEnrolment {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Student-course enrolment's %s field is missing!";

    private final String studentId;
    private final String courseCode;
    private final String semester;
    private final String tutorialClass;
    private final String notes;

    /**
     * Constructs a JSON-adapted enrolment from the supplied fields.
     */
    @JsonCreator
    public JsonAdaptedStudentCourseEnrolment(
            @JsonProperty("studentId") String studentId,
            @JsonProperty("courseCode") String courseCode,
            @JsonProperty("semester") String semester,
            @JsonProperty("tutorialClass") String tutorialClass,
            // TODO: Change to List
            @JsonProperty("notes") String notes) {

        this.studentId = studentId;
        this.courseCode = courseCode;
        this.semester = semester;
        this.tutorialClass = tutorialClass;
        this.notes = notes;
    }

    /**
     * Converts a model enrolment into its JSON-adapted representation.
     */
    public JsonAdaptedStudentCourseEnrolment(StudentCourseEnrolment source) {
        this(
                source.getStudentId().toString(),
                source.getCourseCode().toString(),
                source.getSemester().toString(),
                source.getTutorialClass().toString(),
                source.getNotes().toString());
    }

    /**
     * Converts this JSON-adapted enrolment into its model representation.
     *
     * @throws IllegalValueException if any field is missing or invalid.
     */
    public StudentCourseEnrolment toModelType() throws IllegalValueException {
        if (studentId == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "studentId"));
        }
        if (courseCode == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "courseCode"));
        }
        if (semester == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "semester"));
        }
        if (tutorialClass == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "tutorialClass"));
        }
        if (notes == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "notes"));
        }

        try {
            Notes modelNotes = notes.isEmpty() ? new Notes() : new Notes(notes);

            return new StudentCourseEnrolment(
                    new Id(studentId),
                    new CourseCode(courseCode),
                    new Semester(semester),
                    new TutorialClass(tutorialClass),
                    modelNotes);
        } catch (IllegalArgumentException exception) {
            throw new IllegalValueException(exception.getMessage());
        }
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getSemester() {
        return semester;
    }

    public String getTutorialClass() {
        return tutorialClass;
    }

    public String getNotes() {
        return notes;
    }
}
