package seedu.address.model.student;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a student in TAssist.
 * Guarantees: details are present and not null, field values are validated,
 * immutable.
 */

public class Student {

    // Identity fields
    private final Name name;
    private final Id id;
    private final Email email;
    private final TutorialClass tutorialClass;
    private final Optional<Notes> notes;

    public Student(Name name, Id id, Email email, TutorialClass tutorialClass) {
        requireAllNonNull(name, id, email, tutorialClass);

        this.name = name;
        this.id = id;
        this.email = email;
        this.tutorialClass = tutorialClass;
        this.notes = Optional.empty();
    }

    public Student(Name name, Id id, Email email, TutorialClass tutorialClass, Notes notes) {
        requireAllNonNull(name, id, email, tutorialClass, notes);

        this.name = name;
        this.id = id;
        this.email = email;
        this.tutorialClass = tutorialClass;
        this.notes = Optional.of(notes);
    }

    public Name getName() {
        return name;
    }

    public Id getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public TutorialClass getTutorialClass() {
        return tutorialClass;
    }

    public Optional<Notes> getNotes() {
        return notes;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSameStudent(Student otherStudent) {
        if (otherStudent == this) {
            return true;
        }
        return otherStudent != null
                && (otherStudent.getId().equals(getId())
                        || otherStudent.getEmail().equals(getEmail()));
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Student otherStudent)) {
            return false;
        }

        return name.equals(otherStudent.name)
                && id.equals(otherStudent.id)
                && email.equals(otherStudent.email)
                && tutorialClass.equals(otherStudent.tutorialClass)
                && notes.equals(otherStudent.notes);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, id, email, tutorialClass, notes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("id", id)
                .add("email", email)
                .add("class", tutorialClass)
                .add("notes", notes)
                .toString();
    }

}
