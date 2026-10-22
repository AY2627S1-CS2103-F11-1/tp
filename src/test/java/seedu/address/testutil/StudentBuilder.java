package seedu.address.testutil;



import seedu.address.model.student.Email;
import seedu.address.model.student.Id;
import seedu.address.model.student.Name;
import seedu.address.model.student.Notes;
import seedu.address.model.student.NotesList;
import seedu.address.model.student.Student;
import seedu.address.model.student.TutorialClass;

/** A utility class for building {@link Student} objects in tests. */
public class StudentBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_ID = "A1234567Y";
    public static final String DEFAULT_EMAIL = "amy@u.nus.edu";
    public static final String DEFAULT_TUTORIAL_CLASS = "T07";

    private Name name;
    private Id id;
    private Email email;
    private TutorialClass tutorialClass;
    private NotesList notesList;

    /** Creates a builder with valid default student details. */
    public StudentBuilder() {
        name = new Name(DEFAULT_NAME);
        id = new Id(DEFAULT_ID);
        email = new Email(DEFAULT_EMAIL);
        tutorialClass = new TutorialClass(DEFAULT_TUTORIAL_CLASS);
        notesList = new NotesList();
    }

    /** Creates a builder using the details of {@code studentToCopy}. */
    public StudentBuilder(Student studentToCopy) {
        name = studentToCopy.getName();
        id = studentToCopy.getId();
        email = studentToCopy.getEmail();
        tutorialClass = studentToCopy.getTutorialClass();
        notesList = studentToCopy.getNotes();
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

    /** Sets the student's tutorial class. */
    public StudentBuilder withTutorialClass(String tutorialClass) {
        this.tutorialClass = new TutorialClass(tutorialClass);
        return this;
    }

    /** Sets the student's notes as an empty NotesList */
    public StudentBuilder withNotes() {
        this.notesList = new NotesList();
        return this;
    }

    /** Sets the student's notes given a String of Notes to add. */
    public StudentBuilder withNotes(String notes) {
        this.notesList = new NotesList(new Notes(notes));
        return this;
    }

    /** Sets the student's notes given a NotesList. */
    public StudentBuilder withNotes(NotesList notesList) {
        this.notesList = notesList;
        return this;
    }

    /** Builds a student with the configured details. */
    public Student build() {
        return notesList == null
                ? new Student(name, id, email, tutorialClass)
                : new Student(name, id, email, tutorialClass, notesList);
    }
}
