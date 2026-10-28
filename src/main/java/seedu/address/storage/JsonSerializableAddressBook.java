package seedu.address.storage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.course.Course;
import seedu.address.model.course.Deliverable;
import seedu.address.model.enrolment.Notes;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.progress.CompletionStatus;
import seedu.address.model.progress.StudentCourseProgress;
import seedu.address.model.student.Student;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_STUDENT = "Students list contains duplicate student(s).";
    public static final String MESSAGE_DUPLICATE_COURSE = "Courses list contains duplicate course-semester pair(s).";
    public static final String MESSAGE_DUPLICATE_PROGRESS = "Student course progress list contains duplicate record(s).";
    public static final String MESSAGE_DUPLICATE_ENROLMENT = "Student enrolment list contains duplicate record(s).";
    public static final String MESSAGE_INVALID_ENROLMENT = "Enrolment references a student or course that does not exist.";
    public static final String MESSAGE_INVALID_PROGRESS = "Progress record does not match an existing enrolment.";
    public static final String MESSAGE_INVALID_PROGRESS_COURSE = "Progress record references a course that does not exist.";
    public static final String MESSAGE_INVALID_NOTE_DELIVERABLE = "Note references a deliverable that does not belong to its course.";
    private final List<JsonAdaptedStudent> students = new ArrayList<>();
    private final List<JsonAdaptedCourse> courses = new ArrayList<>();
    private final List<JsonAdaptedStudentCourseProgress> studentCourseProgresses = new ArrayList<>();
    private final List<JsonAdaptedStudentCourseEnrolment> enrolments = new ArrayList<>();
    private boolean wasReconciled;

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
        boolean reconciliationOccurred = false;

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

            Course course = addressBook.getCourse(enrolment.getCourseCode(), enrolment.getSemester()).orElseThrow();
            for (Notes note : enrolment.getNotes().getNotes()) {
                if (!course.getDeliverables().contains(note.getDeliverable())) {
                    throw new IllegalValueException(String.format(
                            "%s Invalid deliverable: %s.", MESSAGE_INVALID_NOTE_DELIVERABLE,
                            note.getDeliverable()));
                }
            }

            modelEnrolments.add(enrolment);
        }

        List<StudentCourseProgress> modelProgresses = new ArrayList<>();

        for (JsonAdaptedStudentCourseProgress jsonProgress : studentCourseProgresses) {
            StudentCourseProgress loadedProgress = jsonProgress.toModelType();

            Course course = addressBook.getCourse(
                    loadedProgress.getCourseCode(), loadedProgress.getSemester()).orElse(null);
            if (course == null) {
                throw new IllegalValueException(String.format(
                        "%s Course: %s, Semester: %s.",
                        MESSAGE_INVALID_PROGRESS_COURSE,
                        loadedProgress.getCourseCode(), loadedProgress.getSemester()));
            }

            if (modelProgresses.stream().anyMatch(existing -> existing.isFor(
                    loadedProgress.getStudentId(), loadedProgress.getCourseCode(), loadedProgress.getSemester()))) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PROGRESS);
            }

            boolean hasEnrolment = modelEnrolments.stream()
                    .anyMatch(enrolment -> enrolment.isFor(
                            loadedProgress.getStudentId(),
                            loadedProgress.getCourseCode(),
                            loadedProgress.getSemester()));

            if (!hasEnrolment) {
                throw new IllegalValueException(MESSAGE_INVALID_PROGRESS);
            }

            Map<Deliverable, CompletionStatus> reconciledStatuses = new LinkedHashMap<>();
            for (Deliverable deliverable : course.getDeliverables()) {
                CompletionStatus status = loadedProgress.getDeliverableStatuses().entrySet().stream()
                        .filter(entry -> entry.getKey().equals(deliverable))
                        .map(Map.Entry::getValue)
                        .findFirst()
                        .orElse(CompletionStatus.NOT_DONE);
                reconciledStatuses.put(deliverable, status);
            }

            StudentCourseProgress progress = loadedProgress;
            if (!reconciledStatuses.equals(loadedProgress.getDeliverableStatuses())) {
                reconciliationOccurred = true;
                progress = StudentCourseProgress.fromStatuses(
                        loadedProgress.getStudentId(), loadedProgress.getCourseCode(), loadedProgress.getSemester(),
                        reconciledStatuses);
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
        wasReconciled = reconciliationOccurred;

        return addressBook;
    }

    /** Returns whether loading repaired any progress records. */
    @JsonIgnore
    public boolean wasReconciled() {
        return wasReconciled;
    }
}
