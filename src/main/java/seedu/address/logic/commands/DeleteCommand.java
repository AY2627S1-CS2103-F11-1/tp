package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;

/**
 * Deletes a student identified by their student ID or name.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a student from TAssist.\n"
            + "Parameters: /id ID or /name NAME\n"
            + "Examples: " + COMMAND_WORD + " /id A1234567Z\n"
            + "          " + COMMAND_WORD + " /name John Doe";

    public static final String MESSAGE_DELETE_STUDENT_SUCCESS = "Successfully deleted student %s (%s).";
    public static final String MESSAGE_STUDENT_ID_NOT_FOUND =
            "No student found with ID '%s'. Please check the student ID and try again.";
    public static final String MESSAGE_STUDENT_NAME_NOT_FOUND =
            "No student found with name '%s'. Please check the student name and try again.";
    public static final String MESSAGE_MULTIPLE_STUDENTS_WITH_NAME =
            "Multiple students found with this name. Please delete the student using their id.";

    private final Optional<Id> studentId;
    private final Optional<String> studentName;

    /** Creates a command that deletes the student with the given ID. */
    public DeleteCommand(Id studentId) {
        this.studentId = Optional.of(requireNonNull(studentId));
        this.studentName = Optional.empty();
    }

    /** Creates a command that deletes the student with the given name. */
    public DeleteCommand(String studentName) {
        this.studentId = Optional.empty();
        this.studentName = Optional.of(requireNonNull(studentName));
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Student studentToDelete = studentId.isPresent()
                ? findStudentById(model, studentId.get())
                : findStudentByName(model, studentName.orElseThrow());

        model.deleteStudent(studentToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_STUDENT_SUCCESS,
                studentToDelete.getName(), studentToDelete.getId()));
    }

    private Student findStudentById(Model model, Id id) throws CommandException {
        return model.getAddressBook().getStudentList().stream()
                .filter(student -> student.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_STUDENT_ID_NOT_FOUND, id)));
    }

    private Student findStudentByName(Model model, String name) throws CommandException {
        List<Student> matchingStudents = model.getAddressBook().getStudentList().stream()
                .filter(student -> student.getName().fullName.equalsIgnoreCase(name))
                .toList();

        if (matchingStudents.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_STUDENT_NAME_NOT_FOUND, name));
        }
        if (matchingStudents.size() > 1) {
            throw new CommandException(MESSAGE_MULTIPLE_STUDENTS_WITH_NAME);
        }
        return matchingStudents.getFirst();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeleteCommand otherDeleteCommand)) {
            return false;
        }
        return studentId.equals(otherDeleteCommand.studentId)
                && studentName.equals(otherDeleteCommand.studentName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("studentId", studentId)
                .add("studentName", studentName)
                .toString();
    }
}
