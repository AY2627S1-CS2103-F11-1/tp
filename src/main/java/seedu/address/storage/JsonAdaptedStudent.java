package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.student.Email;
import seedu.address.model.student.Id;
import seedu.address.model.student.Name;
import seedu.address.model.student.Notes;
import seedu.address.model.student.Student;
import seedu.address.model.student.TutorialClass;

/** Jackson-friendly version of {@link Student}. */
class JsonAdaptedStudent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Student's %s field is missing!";

    private final String name;
    private final String id;
    private final String email;
    private final String tutorialClass;
    private final String notes;

    @JsonCreator
    public JsonAdaptedStudent(@JsonProperty("name") String name, @JsonProperty("id") String id,
            @JsonProperty("email") String email, @JsonProperty("class") String tutorialClass,
            @JsonProperty("notes") String notes) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.tutorialClass = tutorialClass;
        this.notes = notes;
    }

    /** Converts a {@link Student} into this Jackson-friendly representation. */
    public JsonAdaptedStudent(Student source) {
        name = source.getName().toString();
        id = source.getId().toString();
        email = source.getEmail().toString();
        tutorialClass = source.getTutorialClass().toString();
        notes = source.getNotes().map(Notes::toString).orElse(null);
    }

    /** Converts this Jackson-friendly representation into a {@link Student}. */
    public Student toModelType() throws IllegalValueException {
        if (name == null || id == null || email == null || tutorialClass == null) {
            throw new IllegalValueException(MISSING_FIELD_MESSAGE_FORMAT);
        }
        try {
            Name modelName = new Name(name);
            Id modelId = new Id(id);
            Email modelEmail = new Email(email);
            TutorialClass modelClass = new TutorialClass(tutorialClass);
            return notes == null
                    ? new Student(modelName, modelId, modelEmail, modelClass)
                    : new Student(modelName, modelId, modelEmail, modelClass, new Notes(notes));
        } catch (IllegalArgumentException exception) {
            throw new IllegalValueException(exception.getMessage());
        }
    }
}
