package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.progress.CompletionStatus;
import seedu.address.model.progress.StudentCourseProgress;
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;

/**
 * Base command for changing a student's deliverable completion status.
 */
public abstract class StudentProgressCommand extends Command {

    static final String MESSAGE_STUDENT_ID_NOT_FOUND = "No student found with ID '%s'.";
    static final String MESSAGE_STUDENT_NAME_NOT_FOUND = "No student found with name '%s'.";
    static final String MESSAGE_MULTIPLE_STUDENTS_WITH_NAME = "Multiple students found with this name. Please use the student's ID instead.";
    static final String MESSAGE_COURSE_NOT_FOUND = "Course %s %s has not been initialised.";
    static final String MESSAGE_DELIVERABLE_NOT_FOUND = "Deliverable '%s' does not exist in %s %s.";
    static final String MESSAGE_PROGRESS_NOT_FOUND = "No progress record exists for this student and course.";

    private final Optional<Id> studentId;
    private final Optional<String> studentName;
    private final CourseCode courseCode;
    private final Semester semester;
    private final List<Deliverable> deliverables;
    private final CompletionStatus targetStatus;

    StudentProgressCommand(Id studentId, CourseCode courseCode, Semester semester,
            List<Deliverable> deliverables, CompletionStatus targetStatus) {
        this.studentId = Optional.of(requireNonNull(studentId));
        this.studentName = Optional.empty();
        this.courseCode = requireNonNull(courseCode);
        this.semester = requireNonNull(semester);
        this.deliverables = List.copyOf(deliverables);
        this.targetStatus = requireNonNull(targetStatus);
    }

    StudentProgressCommand(String studentName, CourseCode courseCode, Semester semester,
            List<Deliverable> deliverables, CompletionStatus targetStatus) {
        this.studentId = Optional.empty();
        this.studentName = Optional.of(requireNonNull(studentName));
        this.courseCode = requireNonNull(courseCode);
        this.semester = requireNonNull(semester);
        this.deliverables = List.copyOf(deliverables);
        this.targetStatus = requireNonNull(targetStatus);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Student student = findStudent(model, studentId, studentName);
        Course course = findCourse(model);
        validateDeliverables(course);
        StudentCourseProgress progress = findProgress(model, student);
        validateCurrentStatuses(progress);

        StudentCourseProgress updatedProgress = progress;
        for (Deliverable deliverable : deliverables) {
            updatedProgress = updatedProgress.withStatus(deliverable, targetStatus);
        }
        model.setStudentCourseProgress(progress, updatedProgress);
        List<StudentCourseProgress> allProgress = model.getStudentCourseProgressList().stream()
                .filter(candidate -> candidate.getStudentId().equals(student.getId()))
                .toList();
        return new CommandResult(
                String.format(getSuccessMessage(), formatDeliverables(), student.getName()),
                new StudentDeliverablesView(student, allProgress));
    }

    static Student findStudent(Model model, Optional<Id> studentId, Optional<String> studentName)
            throws CommandException {
        requireAllNonNull(model, studentId, studentName);

        if (studentId.isPresent()) {
            return model.getAddressBook().getStudentList().stream()
                    .filter(student -> student.getId().equals(studentId.get()))
                    .findFirst()
                    .orElseThrow(() -> new CommandException(
                            String.format(MESSAGE_STUDENT_ID_NOT_FOUND, studentId.get())));
        }

        List<Student> matchingStudents = model.getAddressBook().getStudentList().stream()
                .filter(student -> student.getName().fullName.equalsIgnoreCase(studentName.orElseThrow()))
                .toList();
        if (matchingStudents.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_STUDENT_NAME_NOT_FOUND, studentName.orElseThrow()));
        }
        if (matchingStudents.size() > 1) {
            throw new CommandException(MESSAGE_MULTIPLE_STUDENTS_WITH_NAME);
        }
        return matchingStudents.getFirst();
    }

    private Course findCourse(Model model) throws CommandException {
        return model.getAddressBook().getCourseList().stream()
                .filter(course -> course.getCourseCode().equals(courseCode) && course.getSemester().equals(semester))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_COURSE_NOT_FOUND, courseCode, semester)));
    }

    private void validateDeliverables(Course course) throws CommandException {
        for (Deliverable deliverable : deliverables) {
            if (!course.getDeliverables().contains(deliverable)) {
                throw new CommandException(String.format(MESSAGE_DELIVERABLE_NOT_FOUND,
                        deliverable, courseCode, semester));
            }
        }
    }

    private StudentCourseProgress findProgress(Model model, Student student) throws CommandException {
        return model.getStudentCourseProgressList().stream()
                .filter(progress -> progress.isFor(student.getId(), courseCode, semester))
                .findFirst()
                .orElseThrow(() -> new CommandException(MESSAGE_PROGRESS_NOT_FOUND));
    }

    private void validateCurrentStatuses(StudentCourseProgress progress) throws CommandException {
        for (Deliverable deliverable : deliverables) {
            if (progress.getStatus(deliverable) == targetStatus) {
                throw new CommandException(String.format(getAlreadyInStatusMessage(), deliverable));
            }
        }
    }

    private String formatDeliverables() {
        return deliverables.stream().map(Deliverable::toString).reduce((first, second) -> first + ", " + second)
                .orElseThrow();
    }

    abstract String getSuccessMessage();

    abstract String getAlreadyInStatusMessage();

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof StudentProgressCommand otherCommand)) {
            return false;
        }
        return studentId.equals(otherCommand.studentId)
                && studentName.equals(otherCommand.studentName)
                && courseCode.equals(otherCommand.courseCode)
                && semester.equals(otherCommand.semester)
                && deliverables.equals(otherCommand.deliverables)
                && targetStatus == otherCommand.targetStatus;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(studentId, studentName, courseCode, semester, deliverables, targetStatus);
    }
}
