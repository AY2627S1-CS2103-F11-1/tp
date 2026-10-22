package seedu.address.testutil;



import seedu.address.logic.commands.EditCommand.EditStudentDescriptor;
import seedu.address.model.student.Email;
import seedu.address.model.student.Id;
import seedu.address.model.student.Name;
import seedu.address.model.student.Student;

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

    /** Retained for compatibility with legacy tests; class is enrolment-specific. */
    public EditStudentDescriptorBuilder withTutorialClass(String tutorialClass) {
        return this;
    }

    public EditStudentDescriptor build() {
        return descriptor;
    }
}
