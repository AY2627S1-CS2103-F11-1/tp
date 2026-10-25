package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.student.Email;
import seedu.address.model.student.Id;
import seedu.address.model.student.Name;
import seedu.address.model.student.Student;

/** Jackson-friendly version of {@link Student}. */
class JsonAdaptedStudent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Student's %s field is missing!";
    public static final String LEGACY_FIELDS_MESSAGE =
            "Legacy student course data cannot be migrated automatically. "
                    + "Please back up and update the address book data manually.";

    private final String name;
    private final String id;
    private final String email;
    private final String legacyTutorialClass;
    private final String legacyNotes;

    public JsonAdaptedStudent(String name, String id, String email) {
        this(name, id, email, null, null);
    }

    /**
     * Creates an adapter while retaining legacy course fields for migration detection.
     */
    @JsonCreator
    public JsonAdaptedStudent(@JsonProperty("name") String name, @JsonProperty("id") String id,
            @JsonProperty("email") String email, @JsonProperty("class") String legacyTutorialClass,
            @JsonProperty("notes") String legacyNotes) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.legacyTutorialClass = legacyTutorialClass;
        this.legacyNotes = legacyNotes;
    }

    /** Converts a {@link Student} into this Jackson-friendly representation. */
    public JsonAdaptedStudent(Student source) {
        name = source.getName().toString();
        id = source.getId().toString();
        email = source.getEmail().toString();
        legacyTutorialClass = null;
        legacyNotes = null;
    }

    /**
     * Converts this Jackson-friendly representation into a {@link Student}.
     *
     * @throws IllegalValueException if any student field is missing or invalid.
     */
    public Student toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "name"));
        }
        if (id == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "id"));
        }
        if (email == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "email"));
        }
        if (legacyTutorialClass != null || legacyNotes != null) {
            throw new IllegalValueException(LEGACY_FIELDS_MESSAGE);
        }

        try {
            return new Student(
                    new Name(name),
                    new Id(id),
                    new Email(email));
        } catch (IllegalArgumentException exception) {
            throw new IllegalValueException(exception.getMessage());
        }
    }
}
