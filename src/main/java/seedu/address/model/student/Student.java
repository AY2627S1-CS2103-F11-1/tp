package seedu.address.model.student;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

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
    private final NotesList notesList;

    /** Constructs a student without notes. */
    public Student(Name name, Id id, Email email, TutorialClass tutorialClass) {
        requireAllNonNull(name, id, email, tutorialClass);

        this.name = name;
        this.id = id;
        this.email = email;
        this.tutorialClass = tutorialClass;
        this.notesList = new NotesList();
    }

    /** Constructs a student with notes. */
    public Student(Name name, Id id, Email email, TutorialClass tutorialClass, NotesList notesList) {
        requireAllNonNull(name, id, email, tutorialClass, notesList);

        this.name = name;
        this.id = id;
        this.email = email;
        this.tutorialClass = tutorialClass;
        this.notesList = notesList;
    }

    /**
     * Returns a copy with the given core fields; notesList is carried over unchanged.
     */
    public Student copyWith(Name name, Id id, Email email, TutorialClass tutorialClass) {
        return new Student(name, id, email, tutorialClass, notesList);
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

    public NotesList getNotes() {
        return notesList;
    }

    /**
     * Returns true if both students have the same name.
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
                && email.equals(otherStudent.email)
                && tutorialClass.equals(otherStudent.tutorialClass)
                && notesList.equals(otherStudent.notesList);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, id, email, tutorialClass, notesList);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("id", id)
                .add("email", email)
                .add("class", tutorialClass)
                .add("notes", notesList)
                .toString();
    }

}
