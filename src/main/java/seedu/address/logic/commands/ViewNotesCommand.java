//@@author mizzfain
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_COURSE;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_ID;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_COURSE;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_ID;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;

import java.util.Objects;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.NotesList;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;

/** Displays notes recorded for a student in a specified course-semester. */
public class ViewNotesCommand extends Command {
    public static final String COMMAND_WORD = "view notes";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Displays notes for a student's course enrolment.\n"
            + "Parameters: " + PREFIX_ID + " " + ARGUMENT_ID + " "
            + PREFIX_COURSE + " " + ARGUMENT_COURSE + " "
            + PREFIX_SEMESTER + " " + ARGUMENT_SEMESTER + "\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_ID + " " + EXAMPLE_ID + " "
            + PREFIX_COURSE + " " + EXAMPLE_COURSE + " "
            + PREFIX_SEMESTER + " " + EXAMPLE_SEMESTER;
    public static final String MESSAGE_VIEW_NOTES_SUCCESS = "Viewing Notes for student %s (%s) for course %s:";
    public static final String MESSAGE_NO_NOTES = "No Notes recorded for student %s (%s) for course %s.";

    private final Id studentId;
    private final CourseCode courseCode;
    private final Semester semester;

    /** Creates a command that displays the selected enrolment's notes. */
    public ViewNotesCommand(Id studentId, CourseCode courseCode, Semester semester) {
        requireAllNonNull(studentId, courseCode, semester);
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.semester = semester;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Student student = StudentNotesCommandUtil.findStudentById(model, studentId);
        StudentCourseEnrolment enrolment = StudentNotesCommandUtil.findEnrolment(
                model, studentId, courseCode, semester);
        NotesList notesList = enrolment.getNotes();

        if (notesList.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_NO_NOTES, student.getName(),
                    student.getId(), enrolment.getCourseCode()), false, false);
        } else {
            return new CommandResult(String.format(MESSAGE_VIEW_NOTES_SUCCESS,
                    student.getName(), student.getId(), enrolment.getCourseCode()),
                    new StudentNotesListView(student, notesList));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ViewNotesCommand otherCommand)) {
            return false;
        }
        return studentId.equals(otherCommand.studentId)
                && courseCode.equals(otherCommand.courseCode)
                && semester.equals(otherCommand.semester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseCode, semester);
    }
}
