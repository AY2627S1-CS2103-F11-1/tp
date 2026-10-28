package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.course.Course;
import seedu.address.model.course.Deliverable;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.progress.StudentCourseProgress;
import seedu.address.model.student.Student;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_STUDENT = "Students list contains duplicate student(s).";
    public static final String MESSAGE_DUPLICATE_COURSE = "Courses list contains duplicate course-semester pair(s).";
    public static final String MESSAGE_DUPLICATE_PROGRESS =
            "Student course progress list contains duplicate record(s).";
    public static final String MESSAGE_DUPLICATE_ENROLMENT =
            "Student enrolment list contains duplicate record(s).";
    public static final String MESSAGE_INVALID_ENROLMENT =
            "Enrolment references a student or course that does not exist.";
    public static final String MESSAGE_INVALID_PROGRESS = "Progress record does not match an existing enrolment.";
    public static final String MESSAGE_MISMATCHED_PROGRESS_DELIVERABLES =
            "Progress record deliverables do not match the associated course.";

    private final List<JsonAdaptedStudent> students = new ArrayList<>();
    private final List<JsonAdaptedCourse> courses = new ArrayList<>();
    private final List<JsonAdaptedStudentCourseProgress> studentCourseProgresses = new ArrayList<>();
    private final List<JsonAdaptedStudentCourseEnrolment> enrolments = new ArrayList<>();

    /**
     * Constructs a serializable address book from the supplied JSON data.
     * Missing collections are treated as empty.
     */
    @JsonCreator
    public JsonSerializableAddressBook(
            @JsonProperty("students") List<JsonAdaptedStudent> students,
            @JsonProperty("courses") List<JsonAdaptedCourse> courses,
            @JsonProperty("studentCourseProgresses") List<JsonAdaptedStudentCourseProgress> studentCourseProgresses,
            @JsonProperty("enrolments") List<JsonAdaptedStudentCourseEnrolment> enrolments) {

        if (students != null) {
            this.students.addAll(students);
        }
        if (courses != null) {
            this.courses.addAll(courses);
        }
        if (studentCourseProgresses != null) {
            this.studentCourseProgresses.addAll(studentCourseProgresses);
        }
        if (enrolments != null) {
            this.enrolments.addAll(enrolments);
        }
    }

    /**
     * Converts a {@code ReadOnlyAddressBook} into a JSON-serializable
     * representation.
     *
     * @param source The address book whose data is to be serialized.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        students.addAll(source.getStudentList().stream()
                .map(JsonAdaptedStudent::new)
                .toList());

        courses.addAll(source.getCourseList().stream()
                .map(JsonAdaptedCourse::new)
                .toList());

        studentCourseProgresses.addAll(source.getStudentCourseProgressList().stream()
                .map(JsonAdaptedStudentCourseProgress::new)
                .toList());

        enrolments.addAll(source.getEnrolmentList().stream()
                .map(JsonAdaptedStudentCourseEnrolment::new)
                .toList());
    }

    /**
     * Converts this JSON-serializable address book into an {@code AddressBook}.
     *
     * @throws IllegalValueException if the stored data contains invalid,
     *                               duplicate or inconsistent records.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();

        for (JsonAdaptedStudent jsonStudent : students) {
            Student student = jsonStudent.toModelType();

            if (addressBook.hasStudent(student)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_STUDENT);
            }

            addressBook.addStudent(student);
        }

        for (JsonAdaptedCourse jsonCourse : courses) {
            Course course = jsonCourse.toModelType();

            if (addressBook.hasCourse(course)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_COURSE);
            }

            addressBook.addCourse(course);
        }

        List<StudentCourseEnrolment> modelEnrolments = new ArrayList<>();

        for (JsonAdaptedStudentCourseEnrolment jsonEnrolment : enrolments) {
            StudentCourseEnrolment enrolment = jsonEnrolment.toModelType();

            if (modelEnrolments.stream().anyMatch(enrolment::isSameEnrolment)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_ENROLMENT);
            }

            boolean studentExists = addressBook.getStudentList().stream()
                    .anyMatch(student -> student.getId().equals(enrolment.getStudentId()));

            boolean courseExists = addressBook.getCourseList().stream()
                    .anyMatch(course -> course.getCourseCode().equals(enrolment.getCourseCode())
                            && course.getSemester().equals(enrolment.getSemester()));

            if (!studentExists || !courseExists) {
                throw new IllegalValueException(MESSAGE_INVALID_ENROLMENT);
            }

            modelEnrolments.add(enrolment);
        }

        List<StudentCourseProgress> modelProgresses = new ArrayList<>();

        for (JsonAdaptedStudentCourseProgress jsonProgress : studentCourseProgresses) {
            StudentCourseProgress progress = jsonProgress.toModelType();

            if (modelProgresses.stream().anyMatch(existing -> existing.isFor(
                    progress.getStudentId(), progress.getCourseCode(), progress.getSemester()))) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PROGRESS);
            }

            boolean hasEnrolment = modelEnrolments.stream()
                    .anyMatch(enrolment -> enrolment.isFor(
                            progress.getStudentId(),
                            progress.getCourseCode(),
                            progress.getSemester()));

            if (!hasEnrolment) {
                throw new IllegalValueException(MESSAGE_INVALID_PROGRESS);
            }

            Course course = addressBook.getCourse(progress.getCourseCode(), progress.getSemester()).orElseThrow();
            Set<Deliverable> courseDeliverables = Set.copyOf(course.getDeliverables());
            if (courseDeliverables.size() != progress.getDeliverableStatuses().size()
                    || !courseDeliverables.equals(progress.getDeliverableStatuses().keySet())) {
                throw new IllegalValueException(MESSAGE_MISMATCHED_PROGRESS_DELIVERABLES);
            }

            modelProgresses.add(progress);
        }

        // Ensure every enrolment has a corresponding progress record.
        for (StudentCourseEnrolment enrolment : modelEnrolments) {
            boolean hasProgress = modelProgresses.stream()
                    .anyMatch(progress -> progress.isFor(
                            enrolment.getStudentId(),
                            enrolment.getCourseCode(),
                            enrolment.getSemester()));

            if (!hasProgress) {
                throw new IllegalValueException(MESSAGE_INVALID_PROGRESS);
            }
        }

        addressBook.setEnrolments(modelEnrolments);
        addressBook.setStudentCourseProgresses(modelProgresses);

        return addressBook;
    }
}
