package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_COURSE;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_DELIVERABLE;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_ID;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_COURSE;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_DELIVERABLE;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_ID;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELIVERABLE;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.Notes;
import seedu.address.model.enrolment.NotesList;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;

/** Adds or replaces the notes for an existing student. */
public class NotesCommand extends Command {

    public static final String COMMAND_WORD = "notes";

    // Notes must be the final parameter to check for required parameters
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds notes for a deliverable for the specified student and course-semester.\n"
            + "Parameters: " + PREFIX_ID + " " + ARGUMENT_ID + " "
            + PREFIX_COURSE + " " + ARGUMENT_COURSE + " "
            + PREFIX_SEMESTER + " " + ARGUMENT_SEMESTER + " "
            + PREFIX_DELIVERABLE + " " + ARGUMENT_DELIVERABLE + " "
            + PREFIX_NOTES + " NOTES\n"
            + "Note: " + PREFIX_NOTES + " must be the final parameter.\n"
            + "Example: " + COMMAND_WORD
            + " " + PREFIX_ID + " " + EXAMPLE_ID
            + " " + PREFIX_COURSE + " " + EXAMPLE_COURSE
            + " " + PREFIX_SEMESTER + " " + EXAMPLE_SEMESTER
            + " " + PREFIX_DELIVERABLE + " " + EXAMPLE_DELIVERABLE
            + " " + PREFIX_NOTES + " Discussed inheritance";

    public static final String MESSAGE_UPDATE_NOTES_SUCCESS = "Updated notes for student %s (%s).";

    public static final String MESSAGE_COURSE_NOT_FOUND = "Course %s for semester %s does not exist. "
            + "Initialise it first.";
    public static final String MESSAGE_DELIVERABLE_NOT_IN_COURSE = "The specified deliverable is not part of the course.";

    private final Id studentId;
    private final Notes notes;
    private final CourseCode courseCode;
    private final Semester semester;

    /**
     * Creates a command to add a note to the specified student's enrolment.
     */
    public NotesCommand(
            Id studentId, CourseCode courseCode, Semester semester, Notes notes) {
        requireAllNonNull(studentId, courseCode, semester, notes);

        this.studentId = studentId;
        this.courseCode = courseCode;
        this.semester = semester;
        this.notes = notes;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Student student = StudentNotesCommandUtil.findStudentById(model, studentId);

        StudentCourseEnrolment enrolment = StudentNotesCommandUtil.findEnrolment(
                model, studentId, courseCode, semester);

        Course course = model.getCourse(courseCode, semester).orElseThrow();
        if (course.getDeliverables().stream()
                .noneMatch(deliverable -> deliverable.equals(notes.getDeliverable()))) {
            throw new CommandException(MESSAGE_DELIVERABLE_NOT_IN_COURSE);
        }

        NotesList updatedNotes = enrolment.getNotes().add(notes);

        model.setEnrolment(enrolment, enrolment.withNotes(updatedNotes));

        return new CommandResult(
                String.format(MESSAGE_UPDATE_NOTES_SUCCESS,
                        student.getName(), student.getId()),
                false, false, student.getId());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof NotesCommand otherNotesCommand)) {
            return false;
        }

        return studentId.equals(otherNotesCommand.studentId)
                && courseCode.equals(otherNotesCommand.courseCode)
                && semester.equals(otherNotesCommand.semester)
                && notes.equals(otherNotesCommand.notes);
    }
}
