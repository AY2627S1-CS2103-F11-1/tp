package seedu.address.model.student;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a student in TAssist.
 * A student has a global identity and may be enrolled in
 * multiple course-semester combinations.
 *
 * Guarantees: details are present, non-null, validated and immutable.
 */

public class Student {

    // Identity fields
    private final Name name;
    private final Id id;
    private final Email email;

    /**
     * Constructs a student with the given details.
     */
    public Student(Name name, Id id, Email email) {
        requireAllNonNull(name, id, email);

        this.name = name;
        this.id = id;
        this.email = email;
    }

    /**
     * Returns a copy with the given student details.
     */
    public Student copyWith(Name name, Id id, Email email) {
        return new Student(name, id, email);
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

    /**
     * Returns true if both students share the same ID or email.
     * This defines a weaker notion of equality between two students.
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
     * Returns true if both students have the same identity and data fields.
     * This defines a stronger notion of equality between two students.
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
                && email.equals(otherStudent.email);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, id, email);
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
