package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_EMAIL;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_ID;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_NAME;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_EMAIL;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_ID;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_ID;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Email;
import seedu.address.model.student.Id;
import seedu.address.model.student.Name;
import seedu.address.model.student.Student;

/**
 * Edits the details of an existing student in the address book.
 */
public class EditCommand extends Command {

    private static final String MESSAGE_UNCHANGED_FIELDS = "Fields already match current values: %s";
    private static final String MESSAGE_NO_CHANGES = "No changes made. The provided values are identical"
            + " to the existing student details.";
    private static final String FIELD_NAME = "Name";
    private static final String FIELD_STUDENT_ID = "Student ID";
    private static final String FIELD_EMAIL = "Email";

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of a student identified by their current student ID.\n"
            + "At least one field must be provided for updating.\n"
            + "Parameters: "
            + PREFIX_ID + " " + ARGUMENT_ID + " "
            + "[" + PREFIX_NAME + " " + ARGUMENT_NAME + "] "
            + "[" + PREFIX_NEW_ID + " NEW_ID] "
            + "[" + PREFIX_EMAIL + " " + ARGUMENT_EMAIL + "]\n"
            + "Example: " + COMMAND_WORD
            + " " + PREFIX_ID + " " + EXAMPLE_ID
            + " " + PREFIX_NAME + " " + EXAMPLE_NAME
            + " " + PREFIX_EMAIL + " " + EXAMPLE_EMAIL;

    public static final String MESSAGE_EDIT_STUDENT_SUCCESS = "Student with id %s updated successfully."
            + " \nChanged fields: %s";
    public static final String MESSAGE_NOT_EDITED = String.format(
            "At least one field to edit must be provided.\nAllowed fields: %s, %s, %s",
            PREFIX_NAME, PREFIX_NEW_ID, PREFIX_EMAIL);
    public static final String MESSAGE_DUPLICATE_STUDENT = "This student already exists in TAssist.";
    public static final String MESSAGE_STUDENT_NOT_FOUND = "Student with ID %s was not found.";

    private final Id studentId;
    private final EditStudentDescriptor descriptor;

    /**
     * @param studentId  of the student in the filtered student list to
     *                   edit
     * @param descriptor details to edit the student with
     */
    public EditCommand(Id studentId, EditStudentDescriptor descriptor) {
        requireNonNull(studentId);
        requireNonNull(descriptor);

        this.studentId = studentId;
        this.descriptor = new EditStudentDescriptor(descriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Student studentToEdit = model.getStudentById(studentId)
                .orElseThrow(() -> new CommandException(
                        String.format(MESSAGE_STUDENT_NOT_FOUND, studentId)));

        Student editedStudent = createEditedStudent(studentToEdit, descriptor);

        String changedFields = formatChangedFields(studentToEdit, editedStudent);
        String unchangedFields = formatUnchangedFields(studentToEdit, descriptor);

        if (changedFields.isEmpty()) {
            throw new CommandException(MESSAGE_NO_CHANGES);
        }

        if (model.hasDuplicateStudent(editedStudent, studentToEdit)) {
            throw new CommandException(MESSAGE_DUPLICATE_STUDENT);
        }

        model.setStudent(studentToEdit, editedStudent);
        model.updateFilteredStudentList(Model.PREDICATE_SHOW_ALL_STUDENTS);

        return createSuccessResult(
                editedStudent, studentToEdit.getId(), changedFields, unchangedFields);
    }

    private static CommandResult createSuccessResult(
            Student editedStudent, Id originalId,
            String changedFields, String unchangedFields) {

        String feedback = String.format(
                MESSAGE_EDIT_STUDENT_SUCCESS, originalId, changedFields);

        if (!unchangedFields.isEmpty()) {
            feedback += "\n" + String.format(
                    MESSAGE_UNCHANGED_FIELDS, unchangedFields);
        }

        return new CommandResult(feedback, false, false, editedStudent.getId());
    }

    private static String formatChangedFields(Student original, Student edited) {
        StringJoiner changes = new StringJoiner("\n");

        if (!original.getName().equals(edited.getName())) {
            changes.add(FIELD_NAME + ": " + edited.getName());
        }

        if (!original.getId().equals(edited.getId())) {
            changes.add(FIELD_STUDENT_ID + ": " + edited.getId());
        }

        if (!original.getEmail().equals(edited.getEmail())) {
            changes.add(FIELD_EMAIL + ": " + edited.getEmail());
        }

        return changes.toString();
    }

    private static String formatUnchangedFields(
            Student originalStudent, EditStudentDescriptor descriptor) {

        StringJoiner unchangedFields = new StringJoiner(", ");

        descriptor.getName().ifPresent(name -> {
            if (originalStudent.getName().equals(name)) {
                unchangedFields.add(FIELD_NAME);
            }
        });
        descriptor.getId().ifPresent(id -> {
            if (originalStudent.getId().equals(id)) {
                unchangedFields.add(FIELD_STUDENT_ID);
            }
        });
        descriptor.getEmail().ifPresent(email -> {
            if (originalStudent.getEmail().equals(email)) {
                unchangedFields.add(FIELD_EMAIL);
            }
        });

        return unchangedFields.toString();

    }

    /**
     * Creates and returns a {@code Student} with the details of {@code original}
     * edited with {@code descriptor}.
     */
    private static Student createEditedStudent(Student original, EditStudentDescriptor descriptor) {
        assert original != null;

        return original.copyWith(
                descriptor.getName().orElse(original.getName()),
                descriptor.getId().orElse(original.getId()),
                descriptor.getEmail().orElse(original.getEmail()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand otherEditCommand)) {
            return false;
        }

        return studentId.equals(otherEditCommand.studentId)
                && descriptor.equals(otherEditCommand.descriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("studentId", studentId)
                .add("descriptor", descriptor)
                .toString();
    }

    /**
     * Stores the details to edit the student with. Each non-empty field value will
     * replace the
     * corresponding field value of the student.
     */
    public static class EditStudentDescriptor {
        private Name name;
        private Id id;
        private Email email;

        public EditStudentDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditStudentDescriptor(EditStudentDescriptor toCopy) {
            setName(toCopy.name);
            setId(toCopy.id);
            setEmail(toCopy.email);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, id, email);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setId(Id id) {
            this.id = id;
        }

        public Optional<Id> getId() {
            return Optional.ofNullable(id);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditStudentDescriptor otherEditStudentDescriptor)) {
                return false;
            }

            return Objects.equals(name, otherEditStudentDescriptor.name)
                    && Objects.equals(id, otherEditStudentDescriptor.id)
                    && Objects.equals(email, otherEditStudentDescriptor.email);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("id", id)
                    .add("email", email)
                    .toString();
        }
    }
}
