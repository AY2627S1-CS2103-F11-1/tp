package seedu.address.testutil;



import seedu.address.model.student.Email;
import seedu.address.model.student.Id;
import seedu.address.model.student.Name;
import seedu.address.model.student.Student;

/** A utility class for building {@link Student} objects in tests. */
public class StudentBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_ID = "A1234567Y";
    public static final String DEFAULT_EMAIL = "amy@u.nus.edu";

    private Name name;
    private Id id;
    private Email email;

    /** Creates a builder with valid default student details. */
    public StudentBuilder() {
        name = new Name(DEFAULT_NAME);
        id = new Id(DEFAULT_ID);
        email = new Email(DEFAULT_EMAIL);
    }

    /** Creates a builder using the details of {@code studentToCopy}. */
    public StudentBuilder(Student studentToCopy) {
        name = studentToCopy.getName();
        id = studentToCopy.getId();
        email = studentToCopy.getEmail();
    }

    /** Sets the student's name. */
    public StudentBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /** Sets the student's ID. */
    public StudentBuilder withId(String id) {
        this.id = new Id(id);
        return this;
    }

    /** Sets the student's email. */
    public StudentBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /** Retained for compatibility with legacy tests; course data belongs to enrolments. */
    public StudentBuilder withTutorialClass(String tutorialClass) {
        return this;
    }

    /** Retained for compatibility with legacy tests; notes belong to enrolments. */
    public StudentBuilder withNotes(String... notes) {
        return this;
    }

    /** Builds a student with the configured details. */
    public Student build() {
        return new Student(name, id, email);
    }
}
