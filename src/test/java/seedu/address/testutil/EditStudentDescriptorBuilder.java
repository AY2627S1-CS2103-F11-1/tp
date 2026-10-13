package seedu.address.testutil;



import seedu.address.logic.commands.EditCommand.EditStudentDescriptor;
import seedu.address.model.student.Email;
import seedu.address.model.student.Id;
import seedu.address.model.student.Name;
import seedu.address.model.student.Notes;
import seedu.address.model.student.Student;
import seedu.address.model.student.TutorialClass;

/**
 * A utility class to help with building EditStudentDescriptor objects.
 */
public class EditStudentDescriptorBuilder {

    private EditStudentDescriptor descriptor;

    public EditStudentDescriptorBuilder() {
        descriptor = new EditStudentDescriptor();
    }

    public EditStudentDescriptorBuilder(EditStudentDescriptor descriptor) {
        this.descriptor = new EditStudentDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditStudentDescriptor} with fields containing
     * {@code student}'s details
     */
    public EditStudentDescriptorBuilder(Student student) {
        descriptor = new EditStudentDescriptor();
        descriptor.setName(student.getName());
        descriptor.setId(student.getId());
        descriptor.setEmail(student.getEmail());
        descriptor.setTutorialClass(student.getTutorialClass());
        student.getNotes().ifPresent(descriptor::setNotes);
    }

    /**
     * Sets the {@code Name} of the {@code EditStudentDescriptor} that we are
     * building.
     */
    public EditStudentDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Id} of the {@code EditStudentDescriptor} that we are
     * building.
     */
    public EditStudentDescriptorBuilder withId(String id) {
        descriptor.setId(new Id(id));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditStudentDescriptor} that we are
     * building.
     */
    public EditStudentDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code TutorialClass} of the {@code EditStudentDescriptor} that we
     * are building.
     */
    public EditStudentDescriptorBuilder withTutorialClass(String tutorialClass) {
        descriptor.setTutorialClass(new TutorialClass(tutorialClass));
        return this;
    }

    /**
     * Parses the {@code notes} into a {@code Notes} object and sets it to the
     * {@code EditStudentDescriptor}
     * that we are building.
     */
    public EditStudentDescriptorBuilder withNotes(String... notes) {
        descriptor.setNotes(notes.length == 0 ? null : new Notes(String.join(", ", notes)));
        return this;
    }

    public EditStudentDescriptor build() {
        return descriptor;
    }
}
