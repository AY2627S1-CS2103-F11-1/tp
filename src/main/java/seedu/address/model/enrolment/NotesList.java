package seedu.address.model.enrolment;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Stores a Student's Notes in a List */
public class NotesList {
    public static final String NOT_JSON_ARRAY_ERROR_MESSAGE = "NotesList must be a JSON array.";
    public static final String NOT_JSON_STRING_ERROR_MESSAGE = "Notes must be a JSON string.";
    public static final String INVALID_JSON_ARRAY_ERROR_MESSAGE = "NotesList contains invalid JSON.";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final List<Notes> notesList;

    /**
     * Creates an empty NotesList.
     */
    public NotesList() {
        this.notesList = new ArrayList<>();
    }

    /**
     * Creates a NotesList of only the provided Notes.
     * @param notes Notes to be added into NotesList.
     */
    public NotesList(Notes notes) {
        requireNonNull(notes);
        this.notesList = new ArrayList<>();
        this.notesList.add(notes);
    }

    /**
     * Creates a NotesList given a List of Notes.
     * @param notesList List of Notes to be converted into NotesList.
     */
    public NotesList(List<Notes> notesList) {
        requireNonNull(notesList);
        this.notesList = new ArrayList<>(notesList);
    }

    /**
     * Creates a NotesList from JSON String representation.
     * @param notesListJsonString NotesList JSON String to be converted into NotesList.
     */
    public NotesList(String notesListJsonString) {
        requireNonNull(notesListJsonString);
        this.notesList = new ArrayList<>();

        if (notesListJsonString.isBlank()) {
            return;
        }

        try {
            JsonNode root = MAPPER.readTree(notesListJsonString);

            if (root == null || !root.isArray()) {
                throw new IllegalArgumentException(NOT_JSON_ARRAY_ERROR_MESSAGE);
            }

            for (JsonNode entry : root) {
                if (!entry.isTextual()) {
                    throw new IllegalArgumentException(NOT_JSON_STRING_ERROR_MESSAGE);
                }

                notesList.add(new Notes(entry.textValue()));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(INVALID_JSON_ARRAY_ERROR_MESSAGE, e);
        }
    }

    /**
     * Adds a Notes to the NotesList.
     * Adds Notes to a copy and returns the copy.
     * Does not change old NotesList, ensuring immutability.
     * @param notes
     */
    public NotesList add(Notes notes) {
        requireNonNull(notes);
        List<Notes> notesListCopy = new ArrayList<>(notesList);
        notesListCopy.add(notes);
        return new NotesList(notesListCopy);
    }

    /**
     * Returns the number of notes in NotesList.
     */
    public int size() {
        return notesList.size();
    }

    /**
     * Checks if NotesList has no Notes.
     */
    public boolean isEmpty() {
        return notesList.isEmpty();
    }

    /**
     * Displays the number of Notes.
     * Used in {@code StudentCard}
     */
    public String displaySize() {
        return "You have " + size() + " notes.";
    }

    /**
     * Displays all the Notes.
     * Used in {@code ViewNotesCommand}
     */
    public String viewNotes() {
        String finalString = "";
        int counter = 1;

        for (Notes notes : notesList) {
            finalString += counter + ". " + notes + "\n";
            counter++;
        }
        return finalString;
    }

    /**
     * Converts NotesList into JSON String.
     */
    @Override
    public String toString() {
        List<String> notesAsStrings = notesList.stream()
                .map(Notes::toString)
                .collect(Collectors.toList());

        try {
            return MAPPER.writeValueAsString(notesAsStrings);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to save notes list.", e);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof NotesList otherNotesList)) {
            return false;
        }

        return notesList.equals(otherNotesList.notesList);
    }

    @Override
    public int hashCode() {
        return notesList.hashCode();
    }
}
