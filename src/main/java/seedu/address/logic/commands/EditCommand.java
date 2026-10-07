package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Email;
import seedu.address.model.student.Id;
import seedu.address.model.student.Name;
import seedu.address.model.student.Notes;
import seedu.address.model.student.Student;
import seedu.address.model.student.TutorialClass;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            // + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            // + "[" + PREFIX_ADDRESS + "ADDRESS] "
            // + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            // + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@u.nus.edu";

    public static final String MESSAGE_EDIT_STUDENT_SUCCESS = "Edited student: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_STUDENT = "This student already exists in TAssist.";

    private final Index index;
    private final EditStudentDescriptor editStudentDescriptor;

    /**
     * @param index                 of the person in the filtered person list to
     *                              edit
     * @param editStudentDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditStudentDescriptor editStudentDescriptor) {
        requireNonNull(index);
        requireNonNull(editStudentDescriptor);

        this.index = index;
        this.editStudentDescriptor = new EditStudentDescriptor(editStudentDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredStudentList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Student personToEdit = lastShownList.get(index.getZeroBased());
        Student editedPerson = createEditedStudent(personToEdit, editStudentDescriptor);

        if (!personToEdit.isSameStudent(editedPerson) && model.hasStudent(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_STUDENT);
        }

        model.setStudent(personToEdit, editedPerson);
        model.updateFilteredStudentList(Model.PREDICATE_SHOW_ALL_STUDENTS);
        return new CommandResult(String.format(MESSAGE_EDIT_STUDENT_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editStudentDescriptor}.
     */
    private static Student createEditedStudent(Student studentToEdit, EditStudentDescriptor editStudentDescriptor) {
        assert studentToEdit != null;

        Name updatedName = editStudentDescriptor.getName().orElse(studentToEdit.getName());
        Id updatedId = editStudentDescriptor.getId().orElse(studentToEdit.getId());
        Email updatedEmail = editStudentDescriptor.getEmail().orElse(studentToEdit.getEmail());
        TutorialClass updatedTutorialClass = editStudentDescriptor.getTutorialClass()
                .orElse(studentToEdit.getTutorialClass());

        Optional<Notes> updatedNotes = editStudentDescriptor.getNotes().isPresent()
                ? editStudentDescriptor.getNotes().get() == null ? Optional.empty()
                        : Optional.of(editStudentDescriptor.getNotes().get())
                : studentToEdit.getNotes();
        if (updatedNotes.isPresent()) {
            return new Student(updatedName, updatedId, updatedEmail, updatedTutorialClass, updatedNotes.get());
        } else {
            return new Student(updatedName, updatedId, updatedEmail, updatedTutorialClass);
        }
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

        return index.equals(otherEditCommand.index)
                && editStudentDescriptor.equals(otherEditCommand.editStudentDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editStudentDescriptor", editStudentDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will
     * replace the
     * corresponding field value of the person.
     */
    public static class EditStudentDescriptor {
        private Name name;
        private Id id;
        private Email email;
        private TutorialClass tutorialClass;
        private Notes notes;

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
            setTutorialClass(toCopy.tutorialClass);
            setNotes(toCopy.notes);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, id, email, tutorialClass, notes);
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

        public void setTutorialClass(TutorialClass tutorialClass) {
            this.tutorialClass = tutorialClass;
        }

        public Optional<TutorialClass> getTutorialClass() {
            return Optional.ofNullable(tutorialClass);
        }

        public void setNotes(Notes notes) {
            this.notes = notes;
        }

        public Optional<Notes> getNotes() {
            return Optional.ofNullable(notes); // Wraps it in Optional cleanly
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
                    && Objects.equals(email, otherEditStudentDescriptor.email)
                    && Objects.equals(tutorialClass, otherEditStudentDescriptor.tutorialClass)
                    && Objects.equals(notes, otherEditStudentDescriptor.notes);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("id", id)
                    .add("email", email)
                    .add("tutorialClass", tutorialClass)
                    .add("notes", notes)
                    .toString();
        }
    }
}
