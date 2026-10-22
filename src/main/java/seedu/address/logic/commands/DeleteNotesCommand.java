package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.NotesList;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;

/** Deletes notes recorded for a student. */
public class DeleteNotesCommand extends Command {

    public static final String COMMAND_WORD = "delete notes";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes all notes for the student identified by their student ID.\n"
            + "Parameters: " + PREFIX_ID + " ID " + PREFIX_COURSE + " COURSE "
            + PREFIX_SEMESTER + " SEMESTER\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_ID + " A1234567Z "
            + PREFIX_COURSE + " CS2103T " + PREFIX_SEMESTER + " AY26/27-S1";
    public static final String MESSAGE_DELETE_NOTES_SUCCESS = "Deleted notes for student %s (%s).";
    public static final String MESSAGE_NO_NOTES = "This student has no notes to delete.";

    private final Id studentId;
    private final CourseCode courseCode;
    private final Semester semester;

    /** Creates a command that deletes a student's notes. */
    public DeleteNotesCommand(Id studentId, CourseCode courseCode, Semester semester) {
        this.studentId = requireNonNull(studentId);
        this.courseCode = requireNonNull(courseCode);
        this.semester = requireNonNull(semester);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Student studentToEdit = StudentNotesCommandUtil.findStudentById(model, studentId);
        StudentCourseEnrolment enrolment = StudentNotesCommandUtil.findEnrolment(
                model, studentId, courseCode, semester);
        if (enrolment.getNotes().isEmpty()) {
            throw new CommandException(MESSAGE_NO_NOTES);
        }

        model.setEnrolment(enrolment, enrolment.withNotes(new NotesList()));

        return new CommandResult(String.format(MESSAGE_DELETE_NOTES_SUCCESS,
                studentToEdit.getName(), studentToEdit.getId()), false, false, studentToEdit.getId());
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof DeleteNotesCommand otherCommand
                && studentId.equals(otherCommand.studentId)
                && courseCode.equals(otherCommand.courseCode)
                && semester.equals(otherCommand.semester));
    }
}
